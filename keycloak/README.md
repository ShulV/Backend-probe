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

### 1) Authorization code (AC) flow
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

### 2) PKCE (proof key for code exchange) flow
- access type = public
- Для работы с PKCE понадобятся специальные генераторы параметров code_verifier и code_challenge
  В обычном приложении эти коды создаются в коде с помощью библиотек-утилит.
1) получение ссылки авторизации с кодами:
```
http://localhost:8180/realms/my-todoapp-realm/protocol/openid-connect/auth?response_type=code&client_id=my-todoapp-client-id&redirect_uri=http://localhost:8010/redirect&scope=openid profile&state=k2fTcNERUEgzEoyK&code_challenge=47DEQpj8HBSa-_TImW-5JCeuQeRkm5NMpJWZG3hSuFU&code_challenge_method=S256
```
ответ - форма авторизации:
```
GET: http://localhost:8180/realms/my-todoapp-realm/protocol/openid-connect/auth?response_type=code&client_id=my-todoapp-client-id&redirect_uri=http://localhost:8010/redirect&scope=openid%20profile&state=k2fTcNERUEgzEoyK&code_challenge=47DEQpj8HBSa-_TImW-5JCeuQeRkm5NMpJWZG3hSuFU&code_challenge_method=S256
```

2) После авторизации
```
GET: http://localhost:8010/redirect?state=k2fTcNERUEgzEoyK&session_state=d37b0fea-807c-4f26-9a97-e8af5e9f9c50&iss=http%3A%2F%2Flocalhost%3A8180%2Frealms%2Fmy-todoapp-realm&code=31e09195-50be-41ab-b1af-eecac832ef09.d37b0fea-807c-4f26-9a97-e8af5e9f9c50.522b7fd4-863f-476d-8964-13f26839298d
```
3) Получение access token
```
POST: http://localhost:8180/realms/my-todoapp-realm/protocol/openid-connect/token
```
с параметрами в body: grant_type, client_id, redirect_uri, code, code_verifier

### 3) Client Credentials flow:
Общение между серверами (m2m, server to server, backend to backend), без клиента и фронта
- включить настройку confidential в KeyCloak, чтобы можно было использовать secret для получения access token
- включить галочку Service Account Enabled (чтобы включился режим Client Credentials) – иначе запрос на получение access token будет возвращаться с ошибкой “не могу выполнить запрос”
- желательно отключить Standart Flow Enabled (хотя и со включенным CC будет работать), т.к. на не нужен будет Code для получения access token

1) Получение at:
```
http://localhost:8180/realms/my-todoapp-realm/protocol/openid-connect/token
```
с параметрами в body: grant_type, client_id, client_secret, scope