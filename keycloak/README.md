# Keycloak

Запуск keycloak windows

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

...