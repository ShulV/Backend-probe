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

##