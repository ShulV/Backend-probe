package ru.javabegin.micro.planner.todo.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.micro.planner.entity.Priority;
//import ru.javabegin.micro.planner.plannerutils.rest.resttemplate.UserRestBuilder;
import ru.javabegin.micro.planner.plannerutils.rest.webclient.UserWebClientBuilder;
import ru.javabegin.micro.planner.todo.search.PrioritySearchValues;
import ru.javabegin.micro.planner.todo.service.PriorityService;

import java.util.List;
import java.util.NoSuchElementException;


/*

Чтобы дать меньше шансов для взлома (например, CSRF атак): POST/PUT запросы могут изменять/фильтровать закрытые данные, а GET запросы - для получения незащищенных данных
Т.е. GET-запросы не должны использоваться для изменения/получения секретных данных

Если возникнет exception - вернется код  500 Internal Server Error, поэтому не нужно все действия оборачивать в try-catch

Используем @RestController вместо обычного @Controller, чтобы все ответы сразу оборачивались в JSON,
иначе пришлось бы добавлять лишние объекты в код, использовать @ResponseBody для ответа, указывать тип отправки JSON

Названия методов могут быть любыми, главное не дублировать их имена и URL mapping

*/


@RestController
@RequestMapping("/priority") // базовый URI

/*
В контроллере можно задать свои настройки CORS, отличные от глобальных (из класса SpringConfig)
@CrossOrigin(origins = "https://localhost:4200", allowCredentials = "true")
 */
public class PriorityController {

    // доступ к данным из БД
    private final PriorityService service;
    private final PriorityService priorityService;

    // микросервисы для работы с пользователями
//    private UserRestBuilder userRestBuilder;
    private final UserWebClientBuilder userWebClientBuilder;

    // автоматическое внедрение экземпляра класса через конструктор
    // не используем @Autowired ля переменной класса, т.к. "Field injection is not recommended "
    public PriorityController(PriorityService service
//            , UserRestBuilder userRestBuilder
            , UserWebClientBuilder userWebClientBuilder, PriorityService priorityService) {
        this.service = service;
//        this.userRestBuilder = userRestBuilder;
        this.userWebClientBuilder = userWebClientBuilder;
        this.priorityService = priorityService;
    }


    @PostMapping("/all")
    public List<Priority> findAll(@RequestBody Long userId) {
        return service.findAll(userId);
    }


    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {

        // проверка на обязательные параметры
        if (priority.getId() != null && priority.getId() != 0) {
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно, иначе может быть конфликт уникальности значения
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значение title
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значение color
        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        // если такой пользователь существует
        if (userWebClientBuilder.userExists(priority.getUserId())) { // вызываем микросервис из другого модуля
            return ResponseEntity.ok(priorityService.add(priority)); // возвращаем добавленный объект с заполненным ID
        }
        // если такой пользователь существует
//        if (userWebClientBuilder.userExists(priority.getUserId())) { // вызываем микросервис из другого модуля
//            return ResponseEntity.ok(service.add(priority)); // возвращаем добавленный объект с заполненным ID
//        }

        return new ResponseEntity("user id=" + priority.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }


    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority) {


        // проверка на обязательные параметры
        if (priority.getId() == null || priority.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значение title
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значение color
        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        // save работает как на добавление, так и на обновление
        service.update(priority);


        return new ResponseEntity(HttpStatus.OK); // просто отправляем статус 200 (операция прошла успешно)

    }

    // параметр id передаются не в BODY запроса, а в самом URL
    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody Long id) {

        Priority priority = null;

        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
        try {
            priority = service.findById(id);
        } catch (NoSuchElementException e) { // если объект не будет найден
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priority);
    }


    // для удаления используем типа запроса put, а не delete, т.к. он позволяет передавать значение в body, а не в адресной строке
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
        try {
            service.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK); // просто отправляем статус 200 (операция прошла успешно)
    }


    // поиск по любым параметрам PrioritySearchValues
    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues) {

        // проверка на обязательные параметры
        if (prioritySearchValues.getUserId() == null || prioritySearchValues.getUserId() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        // если вместо текста будет пусто или null - вернутся все категории
        return ResponseEntity.ok(service.find(prioritySearchValues.getTitle(), prioritySearchValues.getUserId()));
    }


}
