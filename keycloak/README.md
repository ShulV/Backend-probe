# Keycloak

## Запуск keycloak windows

Прописал системные переменные, JAVA_HOME
прописал данные БД в конфиге keycloak.conf

```shell
kc.bat start-dev --http-port=8180
```

Чтобы проверить какие порты какими приложениями заняты (Windows):
```shell
netstat -aon
```

Создали пользователя в keycloak:
```shell
login: root
pwd: root
```

Каждое приложение – имеет свой Realm (рабочая область со всем данными и пользователями)

Создали свой realm с именем:
```shell
my-todoapp-realm
```

Настраиваем клиент (наше бэкенд приложение)

## **Access Type**

confidential:
- будет использоваться статичный secret из KeyCloak
- сокращается кол-во шагов на получение access token
- сервер, где формируется UI, хранит secret у себя в коде или в файле
- сервер, где формируется UI, должен гарантировать безопасное хранение secret
- используется для server-side-applications (UI формируется на сервере)

public:
- НЕ будет использоваться статичный secret, а будет каждый раз формироваться динамический
- сервер, где формируется UI, НЕ хранит secret у себя в коде или в файле
- сервер, где формируется UI, НЕ может гарантировать безопасное хранение secret
- увеличивается кол-во шагов для получения access token (также нагрузка на frontend-код, потому что потребуется выполнять крипто-функции)
- используется для client-side-applications (JS фреймфорки, Angular, React, Vue.js)

Создаем тестового пользователя для проверки flow:
имя: shulv
пароль: 123

## Flows:

МОЖНО ТЕСТИТЬ И ПРОБОВАТЬ FLOWS ПРЯМО НА ОФ. САЙТ https://www.oauth.com/playground/

### 1) Authorization code (AC)
настройки keycloak:
- standard flow enabled = true
- access type = confidential
- credentials -> secret = ***** (для)

State – это временное значение, которое клиентское приложение генерит из случайных символов.
Основное назначение – защита client application от мошенника.
В приложении мы должны генерить state, а затем сравнивать с тем, который возвращается в ответе (CSRF-защита).
1) Получение AC:
```
GET: http://localhost:8180/realms/my-todoapp-realm/protocol/openid-connect/auth?response_type=code&client_id=my-todoapp-client-id&state=sidyuf8s67dfisdgf&scope=openid profile&redirect_uri=http://localhost:8010/redirect
```
2) Получение по AC, access token'а
```
POST: http://localhost:8180/realms/my-todoapp-realm/protocol/openid-connect/token
```
с параметрами в body: grant_type, client_id, client_secret, redirect_uri, code

### 2) 