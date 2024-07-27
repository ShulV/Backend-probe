package ru.javabegin.micro.planner.plannerutils.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

// класс конвертер из данных JWT в роли spring security
public class KCRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey("my-todoapp-client-id")) {
            return Collections.emptyList();
        }
        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("my-todoapp-client-id");
        Collection<String> roles = (Collection<String>) clientAccess.get("roles");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

}
