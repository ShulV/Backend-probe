### В каком порядке запускать приложения, чтобы “все друг друга увидели”:

1) config – с него считывают настройки все приложения
2) eureka server – для регистрации сервисов
3) все клиентские приложения поочередно
4) api gateway – шлюз, который должен распознать все сервисы, порты и сервер Eureka

### Существует негласное правило (полуофициальное) – какие порты желательно присваивать для разных типов приложений

Порты:
- Приложения 8080, 8081 и далее
- Eureka Server 8761
- API Gateway 8765
- Config Server 8888

### Совместимость Spring Cloud Spring Boot
<table>
    <thead>
        <tr>
            <td>Release Train</td>
            <td>Spring Boot Generation</td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>2023.0.x aka Leyton</td>
            <td>3.2.x</td>
        </tr>
    </tbody>
</table>

### Spring Cloud Config
 - настроен на работу с основной веткой main (приватный репо с настройками)
 - localhost:8888/eureka-client/default откроет файл с настройками в браузере
    файл eureka-client.properties из приватного репозитория
    P.S. вместо default можно указывать названия profile, если конфигов несколько (дев, прод)


### Spring Boot actuator
- включаем actuator
    management.endpoint.gateway.enabled=true
- какие endpoints хотим включить
management.endpoint.web.exposure.include=*
- добавляем зависимость
  implementation 'org.springframework.boot:spring-boot-starter-actuator'

Из интересного: с помощью этого инструмента можно посмотреть метрики, бины в контексте, роуты и др