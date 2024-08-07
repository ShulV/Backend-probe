package ru.javabegin.micro.planner.users.keycloak;

import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.javabegin.micro.planner.entity.User;
import ru.javabegin.micro.planner.users.dto.UserDTO;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakUtils {
    // Настройки из .properties
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String resourceClientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    // Ссылка на единственный экземпляр объекта KC
    private static Keycloak keycloak;

    public Keycloak getInstance() {
        if (keycloak == null) {
            Keycloak kc = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(resourceClientId)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
            return kc;
        }
        return keycloak;
    }

    public Response createKeycloakUser(UserDTO user, List<String> roles) {
        // Доступ к realm API
        RealmResource realmResource = getInstance().realm(realm);
        // Доступ к API для работы с пользователями
        UsersResource usersResource = realmResource.users();
        // Данные пароля - спец. объект-контейнер
        CredentialRepresentation credentialRepresentation = createCredentialRepresentation(user.getPassword());

        // Данные пользователя (можно задавать множество параметров, зависит от задачи). Спец. объект-контейнер.
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        kcUser.setRealmRoles(roles);
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));

        return usersResource.create(kcUser);
    }

    private CredentialRepresentation createCredentialRepresentation(String password) {
        CredentialRepresentation passwordRepresentation = new CredentialRepresentation();
        passwordRepresentation.setTemporary(false);// Не нужно изменять пароль после первого входа
        passwordRepresentation.setType(CredentialRepresentation.PASSWORD);
        passwordRepresentation.setValue(password);
        return passwordRepresentation;
    }
}
