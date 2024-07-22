package ru.javabegin.micro.demo.commontest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity // включает механизм защиты адресов, которые настраиваются в SecurityFilterChain
// в старых версиях spring security нужно было наследовать от спец. класса WebSecurityConfigurerAdapter
// Подробнее https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter

@EnableMethodSecurity // включить возможность работы pre/post методов по ролям
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/test/anonymous").anonymous()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return jwt -> {
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess == null || !resourceAccess.containsKey("my-todoapp-client-id")) {
                return Collections.emptyList();
            }
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("my-todoapp-client-id");
            Collection<String> roles = (Collection<String>) clientAccess.get("roles");

            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        };
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // Removing the default "SCOPE_" prefix

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);
            authorities.addAll(Objects.requireNonNull(jwtGrantedAuthoritiesConverter().convert(jwt)));
            return authorities;
        });

        return jwtAuthenticationConverter;
    }
}
