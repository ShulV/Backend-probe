package ru.javabegin.micro.planner.users.controller;

/*

Чтобы дать меньше шансов для взлома (например, CSRF атак): POST/PUT запросы могут изменять/фильтровать закрытые данные, а GET запросы - для получения незащищенных данных
Т.е. GET-запросы не должны использоваться для изменения/получения секретных данных

Если возникнет exception - вернется код  500 Internal Server Error, поэтому не нужно все действия оборачивать в try-catch

Используем @RestController вместо обычного @Controller, чтобы все ответы сразу оборачивались в JSON,
иначе пришлось бы добавлять лишние объекты в код, использовать @ResponseBody для ответа, указывать тип отправки JSON

Названия методов могут быть любыми, главное не дублировать их имена и URL mapping

*/

//import org.springframework.kafka.core.KafkaTemplate;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.micro.planner.entity.User;
import ru.javabegin.micro.planner.users.dto.AddingRolesDTO;
import ru.javabegin.micro.planner.users.dto.UserDTO;
import ru.javabegin.micro.planner.users.search.UserSearchValues;
import ru.javabegin.micro.planner.users.service.UserService;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/user") // базовый URI
public class ManageUserController {

    public static final String ID_COLUMN = "id"; // имя столбца id
    private final UserService userService; // сервис для доступа к данным (напрямую к репозиториям не обращаемся)

    // (для kafka spring-kafka низкоуровневый способ)
//    private final KafkaTemplate<String, Long> kafkaTemplate;
//    private final static String TOPIC_NAME = "my-test-topic";

    // микросервисы для работы с пользователями
//    private final UserWebClientBuilder userWebClientBuilder;

    // для отправки сообщения по требованию (реализовано с помощью функц. кода) (для RABBIT MQ SCS)
//    private final MessageFuncActions messageFuncActions;

    //для легаси отправки сообщений (для RABBIT MQ SCS)
//    private final MessageProducer messageProducer;

    // используем автоматическое внедрение экземпляра класса через конструктор
    // не используем @Autowired ля переменной класса, т.к. "Field injection is not recommended "
    public ManageUserController(UserService userService
//            , MessageProducer messageProducer
//            , UserWebClientBuilder userWebClientBuilder
//            , MessageFuncActions messageFuncActions
//            , KafkaTemplate<String, Long> kafkaTemplate
    ) {
        this.userService = userService;
//        this.kafkaTemplate = kafkaTemplate;
//        this.userWebClientBuilder = userWebClientBuilder;
//        this.messageFuncActions = messageFuncActions;
//        this.messageProducer = messageProducer;
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserRepresentation>> getAll() {
        List<UserRepresentation> userRepresentations = userService.getAll();
        return ResponseEntity.ok(userRepresentations);
    }

    // добавление
    @PostMapping("/add")
    public ResponseEntity<UUID> add(@RequestBody UserDTO user) {

        // проверка на обязательные параметры
        if (user.getId() != null) {
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно, иначе может быть конфликт уникальности значения
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значение
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }

        // добавляем пользователя
        Response response = userService.add(user);

        if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        if (response.getStatus() == HttpStatus.BAD_REQUEST.value()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (response.getStatus() == HttpStatus.FORBIDDEN.value()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UUID userId = UUID.fromString(CreatedResponseUtil.getCreatedId(response));

        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    @PostMapping("/add-roles") // Возможно тут не add, а change
    public ResponseEntity<Object> add(@RequestBody AddingRolesDTO addingRolesDTO) {
        userService.addRoles(addingRolesDTO.getUserId(), addingRolesDTO.getRoles());
        return ResponseEntity.status(HttpStatus.CREATED).body("Roles were added......");
    }

    //TODO update, delete, search не стал реализовывать. Вроде всё просто...
    //https://javabegin.ru/courses/avtorizaciya-s-pomoshhju-oauth2-predz-na-fevral-2022-g/lessons/drugie-operacii-po-polzovatelju/




//    @PostMapping("/add-with-test-data")
//    public ResponseEntity<Response> addWithInitData(@RequestBody UserDTO user) {
//        // проверка на обязательные параметры
//        if (user.getId() != null) {
//            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно, иначе может быть конфликт уникальности значения
//            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        // если передали пустое значение
//        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
//            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
//            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
//            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        // добавляем пользователя
//        Response userResponse = userService.add(user);
//
//        if (user != null) {
//
////            kafkaTemplate.send(TOPIC_NAME, user.getId());
//
//            // Функ отправка в очередь (RABBIT MQ SCS)
////            messageFuncActions.sendNewUserMessage(user.getId());
//
//            // Legacy отправка в очередь (RABBIT MQ SCS)
////            messageProducer.newUserAction(user.getId());
//
//            //заполняем начальные данные пользователя (в параллелном потоке)
////            userWebClientBuilder.initUserData(user.getId()).subscribe(result -> {
////                        System.out.println("user populated: " + result);
////                    }
////            );
//        }
//
//        return ResponseEntity.ok(userResponse); // возвращаем созданный объект со сгенерированным id
//    }

    // обновление
    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {

        // проверка на обязательные параметры
        if (user.getId() == null || user.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значение
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }


        // save работает как на добавление, так и на обновление
//        userService.update(user);//TODO

        return new ResponseEntity(HttpStatus.OK); // просто отправляем статус 200 (операция прошла успешно)

    }


    // для удаления используем типа запроса put, а не delete, т.к. он позволяет передавать значение в body, а не в адресной строке
    @PostMapping("/deletebyid")
    public ResponseEntity deleteByUserId(@RequestBody Long userId) {

        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
        try {
//            userService.deleteByUserId(userId);//TODO
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("userId=" + userId + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK); // просто отправляем статус 200 (операция прошла успешно)
    }

    // для удаления используем типа запроса put, а не delete, т.к. он позволяет передавать значение в body, а не в адресной строке
    @PostMapping("/deletebyemail")
    public ResponseEntity deleteByUserEmail(@RequestBody String email) {

        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
        try {
//            userService.deleteByUserEmail(email);//TODO
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK); // просто отправляем статус 200 (операция прошла успешно)
    }


    // получение объекта по id
    @PostMapping("/id")
    public ResponseEntity<User> findById(@RequestBody Long id) {

//        Optional<User> userOptional = userService.findById(id);//TODO

        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
//        try {
//            if (userOptional.isPresent()) {
//                return ResponseEntity.ok(userOptional.get());
//            }
//        } catch (NoSuchElementException e) { // если объект не будет найден
//            e.printStackTrace();
//        }

        return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    // получение уникального объекта по email
    @PostMapping("/email")
    public ResponseEntity<User> findByEmail(@RequestBody String email) { // строго соответствие email

        User user = null;

        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
        try {
//            user = userService.findByEmail(email);//TODO
        } catch (NoSuchElementException e) { // если объект не будет найден
            e.printStackTrace();
            return new ResponseEntity("email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(user);
    }



    // поиск по любым параметрам UserSearchValues
    @PostMapping("/search")
    public ResponseEntity<Page<User>> search(@RequestBody UserSearchValues userSearchValues) throws ParseException {

        // все заполненные условия проверяются условием ИЛИ - это можно изменять в запросе репозитория

        // можно передавать не полный email, а любой текст для поиска
        String email = userSearchValues.getEmail() != null ? userSearchValues.getEmail() : null;

        String username = userSearchValues.getUsername() != null ? userSearchValues.getUsername() : null;

//        // проверка на обязательные параметры - если они нужны по задаче
//        if (email == null || email.trim().length() == 0) {
//            return new ResponseEntity("missed param: user email", HttpStatus.NOT_ACCEPTABLE);
//        }

        String sortColumn = userSearchValues.getSortColumn() != null ? userSearchValues.getSortColumn() : null;
        String sortDirection = userSearchValues.getSortDirection() != null ? userSearchValues.getSortDirection() : null;

        Integer pageNumber = userSearchValues.getPageNumber() != null ? userSearchValues.getPageNumber() : null;
        Integer pageSize = userSearchValues.getPageSize() != null ? userSearchValues.getPageSize() : null;

        // направление сортировки
        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        /* Вторым полем для сортировки добавляем id, чтобы всегда сохранялся строгий порядок.
            Например, если у 2-х задач одинаковое значение приоритета и мы сортируем по этому полю.
            Порядок следования этих 2-х записей после выполнения запроса может каждый раз меняться, т.к. не указано второе поле сортировки.
            Поэтому и используем ID - тогда все записи с одинаковым значением приоритета будут следовать в одном порядке по ID.
         */

        // объект сортировки, который содержит стобец и направление
        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

        // объект постраничности
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        // результат запроса с постраничным выводом
//        Page<User> result = userService.findByParams(email, username, pageRequest);//TODO

        // результат запроса
        return ResponseEntity.ok(null);

    }


}
