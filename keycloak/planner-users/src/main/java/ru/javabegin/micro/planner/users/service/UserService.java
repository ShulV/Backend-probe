package ru.javabegin.micro.planner.users.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabegin.micro.planner.users.dto.UserDTO;
import ru.javabegin.micro.planner.users.keycloak.KeycloakUtils;

import java.util.Collections;
import java.util.List;

// всегда нужно создавать отдельный класс Service для доступа к данным, даже если кажется,
// что мало методов или это все можно реализовать сразу в контроллере
// Такой подход полезен для будущих доработок и правильной архитектуры (особенно, если работаете с транзакциями)
@Service

// все методы класса должны выполниться без ошибки, чтобы транзакция завершилась
// если в методе возникнет исключение - все выполненные операции откатятся (Rollback)
@Transactional
public class UserService {

//    private final UserRepository repository; // сервис имеет право обращаться к репозиторию (БД)
    private final KeycloakUtils keycloakUtils;

    public UserService(
//            UserRepository repository,
            KeycloakUtils keycloakUtils) {
//        this.repository = repository;
        this.keycloakUtils = keycloakUtils;
    }

    // возвращает только либо 0 либо 1 объект, т.к. email уникален для каждого пользователя
//    public User findByEmail(String email) {
//        return repository.findByEmail(email);
//    }

    public Response add(UserDTO user) {
        try {
            return keycloakUtils.createKeycloakUser(user, Collections.singletonList("user"));

        } catch (Exception e) {
            System.out.println(e);// test
            return null;
        }
    }

    public List<UserRepresentation> getAll() {
        return keycloakUtils.getAll();
    }

//    public User update(User user) {
//        return repository.save(user); // метод save обновляет или создает новый объект, если его не было
//    }
//
//    public void deleteByUserId(Long id) {
//        repository.deleteById(id);
//    }
//
//    public void deleteByUserEmail(String email) {
//        repository.deleteByEmail(email);
//    }
//
//    public Optional<User> findById(Long id) {
//        return repository.findById(id); // т.к. возвращается Optional - можно получить объект методом get()
//    }
//
//    public Page<User> findByParams(String username, String password, PageRequest paging) {
//        return repository.findByParams(username, password, paging);
//    }

}
