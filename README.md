# Zabook - Mạng Xã Hội

Zabook là một mạng xã hội đơn giản được phát triển với mục tiêu giúp người dùng kết nối, chia sẻ bài viết và gửi lời mời kết bạn. Ứng dụng cung cấp các tính năng như đăng ký, đăng nhập, gửi lời mời kết bạn, và quản lý các mối quan hệ bạn bè.

## Tính Năng

- **Đăng ký và đăng nhập**: Người dùng có thể tạo tài khoản và đăng nhập vào hệ thống.
- **Gửi và chấp nhận lời mời kết bạn**: Người dùng có thể gửi và nhận lời mời kết bạn từ những người khác.
- **Chia sẻ bài viết**: Người dùng có thể chia sẻ các bài viết, xem bài viết từ bạn bè.
- **Đăng bài viết : Người dùng có thể đăng các bài viết của mình
- **Tương tác với bài viết : Người dùng có thể like, comment, share vào bài viết của người khác
- **Nhắn tin: Người dùng có thể nhắn tin qua lại với nhau
- **Quản lý hồ sơ cá nhân**: Người dùng có thể chỉnh sửa hồ sơ cá nhân của mình.

## Cài Đặt

### Yêu cầu hệ thống

- **Java 20**
- **MongoDB** (sử dụng MongoDB làm cơ sở dữ liệu)
- **Maven** (hoặc **Gradle** nếu bạn sử dụng)
- **Tomcat**

### Hướng Dẫn Cài Đặt

1. **Clone dự án về máy của bạn**:

    ```bash
    git clone https://github.com/ThanhAn333/Social_Network_Project
    ```

2. **Cài đặt các phụ thuộc**:

    Di chuyển vào thư mục dự án và cài đặt các phụ thuộc với Maven:

    ```bash
    cd zabook
    mvn clean install
    ```

3. **Cấu hình MongoDB**:

    Đảm bảo rằng bạn đã cài đặt MongoDB và chạy server MongoDB trên máy của mình. Bạn có thể chỉnh sửa cấu hình MongoDB trong `application.properties` hoặc `application.yml` nếu cần.
    
        spring.application.name=Zabook


        server.port=2610

        spring.thymeleaf.prefix=classpath:/templates/
        spring.web.resources.static-locations=classpath:/static/


        spring.thymeleaf.suffix=.html
        spring.thymeleaf.mode=HTML
        spring.thymeleaf.encoding=UTF-8
        spring.thymeleaf.cache=false

        spring.mail.host=smtp.gmail.com
        spring.mail.port=587
        spring.mail.username=nguyenthanhan26.qngai@gmail.com
        spring.mail.password=dwoa swhj iitp rlzl
        spring.mail.protocol=smtp
        spring.mail.properties.mail.smtp.auth=true
        spring.mail.properties.mail.smtp.starttls.enable=true


        spring.data.mongodb.uri=mongodb+srv://nguyenthanhan26qngai:Nguyenthanhan123!@nguyenthanhan.acqmn.mongodb.net/Zabook-Network-Social?retryWrites=true&w=majority

        spring.data.mongodb.database=Zabook-Network-Social
        spring.data.mongodb.auto-index-creation=true

        upload.directory=src/main/resources/static/images/data
        spring.servlet.multipart.max-file-size=10MB
        spring.servlet.multipart.max-request-size=10MB

        spring.websocket.enabled=true


4. **Chạy ứng dụng**:

    Sau khi cài đặt, bạn có thể chạy ứng dụng bằng cách sử dụng lệnh Maven:

    ```bash
    mvn spring-boot:run
    ```

    Hoặc chạy file JAR đã build:

    ```bash
    java -jar target/zabook-1.0.0.jar
    ```

5. **Truy cập vào ứng dụng**:

    Ứng dụng sẽ được chạy trên `http://localhost:2610`. Bạn có thể truy cập vào trang web này qua trình duyệt.

## Các Công Nghệ Sử Dụng

- **Spring Boot**: Được sử dụng để xây dựng ứng dụng backend.
- **Spring Security**: Đảm bảo bảo mật cho ứng dụng.
- **websocket : Nhắn tin và thông báo
- **Spring Data MongoDB**: Kết nối và làm việc với cơ sở dữ liệu MongoDB.
- **Thymeleaf**: Dùng làm template engine cho giao diện người dùng.
- **Bootstrap**: Cung cấp các thành phần giao diện người dùng đẹp và dễ sử dụng.
- **Font Awesome**: Dùng cho biểu tượng và các thành phần UI khác.

## Cấu Trúc Dự Án

Dưới đây là cấu trúc thư mục của dự án:

.vscode
Zabook
.mvn/wrapper
bin
src
main
java/Zabook
Until
configs
controllers
dto
models
repository
services
ZabookApplication.java
resources
META-INF
bootstrap/pitnik-MXH
static
templates
application.properties
test/java/Zabook
.gitattributes
.gitignore
mvnw
mvnw.cmd
pom.xml
.project
README.md



## Đóng Góp

1. Fork dự án và tạo một nhánh mới:
   
    ```bash
    git checkout -b feature/new-feature
    ```

2. Thực hiện thay đổi và commit:
   
    ```bash
    git commit -am 'Add new feature'
    ```

3. Push nhánh lên GitHub:

    ```bash
    git push origin feature/new-feature
    ```

4. Tạo một Pull Request (PR) để đóng góp vào dự án chính.

## Liên Hệ

- **Email**: nguyenthanhan26.qngai@gmail.com
- **GitHub**: https://github.com/ThanhAn333/Social_Network_Project

## Giấy Phép

Dự án này được cấp phép dưới Giấy phép MIT - Xem file [LICENSE](LICENSE) để biết thêm chi tiết.
