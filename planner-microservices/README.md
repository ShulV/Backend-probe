# Microservices некоторая теория

## Динамическое обновление настроек spring cloud config 
- с помощью spring actuator
- POST-запрос: http://localhost:8765/planner-users/actuator/refresh
  запрос вернет разницу между настройками
- рекомендуется использоваться только для каких-ниб переменных в конфиге, для кредов БД вроде не робит, 
т.к. они читаются при старте

- Чтобы работало нужно:
  - @RefreshScope на главное application-классе приложения
  - Включаем actuator
management.endpoint.gateway.enabled=true
  - Какие endpoints хотим включить
management.endpoints.web.exposure.include=*

Пример ответа:
```json
[
    "config.client.version",
    "tmpVariable",
    "management.endpoint.gateway.enabled"
]
```

---

## Kafka

Нужно сначала установить kafka, настроить переменные среды (если винда), пути до zookeeper и server

Удобный запуск zookeeper и kafka server:
на винде можно создать такой батник
```shell
start zookeeper-server-start C:\Users\vshul\Progr\kafka\config\zookeeper.properties
timeout 10
start kafka-server-start C:\Users\vshul\Progr\kafka\config\server.properties
```

Создание топика из консоли
```shell
kafka-topics --create --topic my-test-topic --bootstrap-server localhost:9092 
```

Посмотреть топики
```shell
kafka-topics --list --bootstrap-server localhost:9092 
```

Отправка сообщений через консоль
```shell
C:\Users\vshul>kafka-console-producer --topic my-test-topic --bootstrap-server localhost:9092                       
>my first kafka message from Vitya!!!                                                                                   
>my second message...                                                                                                   
>three                                                                                                                  
> 
```

Получение сообщений через консоль
```shell
C:\Users\vshul>kafka-console-consumer --topic my-test-topic --from-beginning --bootstrap-server localhost:9092      
my first kafka message from Vitya!!!                                                                                    
my second message...                                                                                                    
three 
```

Spring Kafka (аналогично rabbitmq через SCS) или более низкоуровневый вариант от спринга

