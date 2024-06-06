
http://localhost:8080/admin/console  

docker:  
```bash
docker run -it --name admin -p 8084:8080 \
-v /home/docker/EASecurity/admin/conf:/app/conf \
-v /home/docker/EASecurity/admin/log:/app/log \
-d 128.0.0.2:5000/com.easecurity/easecurity-admin:0.1
```

http://localhost:8080/admin/gifCaptcha/?max=2&offset=2&sort=gkey&order=desc&search.gkey=2
search.gkey可以是并列多个，也可以是search的JSON对象
?expand=adminUserRoles&deep=true
有子对象时，一对一或者一对多

推荐：
./gradlew runCommand -Pargs="generate-vue com.easecurity.admin.auth.UserAdmin --force"
./gradlew runCommand -Pargs="generate-vue com.easecurity.admin.core.b.*"
./gradlew runCommand -Pargs="generate-async-controller com.easecurity.admin.auth.UserAdmin"
./gradlew runCommand -Pargs="generate-async-controller com.easecurity.admin.core.b.*"
不推荐（yml中有中文时有可能会报错）：
grails run-command generate-vue com.easecurity.admin.auth.UserAdmin
grails run-command generate-vue "com.easecurity.admin.core.b.*"
grails run-command generate-async-controller com.easecurity.admin.auth.UserAdmin
grails run-command generate-async-controller "com.easecurity.admin.core.b.*"

## Grails 5.3.6 Documentation

- [User Guide](https://docs.grails.org/5.3.6/guide/index.html)
- [API Reference](https://docs.grails.org/5.3.6/api/index.html)
- [Grails Guides](https://guides.grails.org/index.html)
---

## Feature views-json documentation

- [Grails JSON Views documentation](https://views.grails.org/)

## Feature grails-web-console documentation

- [Grails Web Console documentation](https://plugins.grails.org/plugin/sheehan/console)

## Feature cache documentation

- [Grails Cache Plugin documentation](https://grails-plugins.github.io/grails-cache/latest/guide/index.html)

## Feature testcontainers documentation

- [https://www.testcontainers.org/](https://www.testcontainers.org/)

## 逻辑删除

- [https://guides.grails.org/grails4/grails-logicaldelete/guide/index.html](https://guides.grails.org/grails4/grails-logicaldelete/guide/index.html)
- run the app in development environment.

```bash
grails run-app
```
- generate a war file 

```bash
grails war
```

- generate a runnable war file

```bash
grails package
java -Dgrails.env=dev -jar build/libs/grails4-animals-0.2.war
```

open `http://localhost:8080/animals` in browser

bootstraped user accounts:
- user: admin pw: admin
- user: user pw: user
