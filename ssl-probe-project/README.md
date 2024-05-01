# Небольшой конспект
KeyStore
----------------------------------------------------------------------------------------------------------------
ОБЩИЙ КОНТЕЙНЕР
name: main-ks
password: rootroot
----------------------------------------------------------------------------------------------------------------
ТЕСТОВЫЙ ДОВЕРЕННЫЙ СЕРВЕР СЕРТИФИКАЦИИ (ВЛОЖЕННЫЙ КОНТЕЙНЕР)
дата истечения сертификата (2124 год...)
common name: rootca
organization unit: xmap-backend
organization name: xmap
locality name: -
state name: -
country: -
extention: CA
authority key identifier (AKI): ключ удостоверяющего центра (уникальный)
basic constrints: subject is a CA (ограничения)
key usage:certificate signing, CRL sign (запрос на отзыв сертификата). (назначение ключа)
subject key identifier (SKI): общий ключ (в любом сертификате создается)
subject alternative name extension (SAN) (браузер проверяет адрес и сверяет его с этим ключом)
IP-address: 127.0.0.1
DNS: localhost
URI: localhost
alias: rootca
password: rootroot
----------------------------------------------------------------------------------------------------------------
(ВЛОЖЕННЫЙ КОНТЕЙНЕР)
дата истечения сертификата (2124 год...)
common name: xmap-ssl
organization unit: developer
organization name: xmap
locality name: -
state name: -
country: -
extention: SSL Server
authority key identifier (AKI): уникальный ключ
extended key usage: TLS Web server authentication (назначение ключа)
key usage: digital signature, key encipherment
subject key identifier (SKI): общий ключ (в любом сертификате создается)
subject alternative name extension (SAN)
IP-address: 127.0.0.1
DNS: localhost
URI: localhost
alias: xmap-ssl
password: rootroot
certificate hierarchy: rootca -> xmap-ssl
----------------------------------------------------------------------------------------------------------------
УСТАНОВКА УДОСТОВЕРЯЮЩЕГО ЦЕНТРА В ОС windows:
1) export certificate chain from entry 'rootca'
   entire chain, X.509
2) cert_name.cer ткрываем двумя кликами и кликаем "установить сертификат"
   текущий пользователь
   поместить все сертификаты в следующее хранилище -> Доверенные корневые центры сертификации

P.S. в гугл хроме можно првоерить эксортированный сертификат, гугл сам тянет с менеджера сертификатов сертификаты
certmgr
----------------------------------------------------------------------------------------------------------------
УСТАНОВКА СЕРТИФИКАТА В ОС windows:
1) export certificate chain from entry 'xmap-ssl'
   entire chain, X.509
2) cert_name.cer ткрываем двумя кликами и кликаем "установить сертификат"
   текущий пользователь
   поместить все сертификаты в следующее хранилище -> Личное
----------------------------------------------------------------------------------------------------------------
P.S. хром сам подсасывает сертифимкаты из ОС,
firefox , например, так не делает, нужно загружать сертификаты в браузер вручную
----------------------------------------------------------------------------------------------------------------
ЭКСПОРТ KeyPair для приложения SpringBoot
- нужно экспортировать xmap-ssl в приложение
  export key pair
  password: rootroot
  format: PKCS#12
  расширение .pfx или .p12 (то и то работает)

настройки в application.properties
#для SSL шифрования и использования keypair
server.ssl.enabled=true
server.ssl.key-store-type=PKCS12
server.ssl.key-store=classpath:ssl/xmap_ssl.pfx
server.ssl.key-store-password=rootroot

экспортировать xmap-ssl как keypair (.pfx)
положить в папку с ресурсами в /ssl (например)
ПРОВЕРИТЬ С ПОМОЩЬЮ ТЕСТ КОНТРОЛЛЕРА, ДОЛЖЕН РАБОТАТЬ ЗАПРОС ЧЕРЕЗ HTTPS, замочек в браузере закрыт и сертификат is valid.