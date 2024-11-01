# Back-end 과제
Java SpringBoot 기반으로 제작된 상품 - 브랜드/카테고리 api 

### Project 스펙
- Java : [Corretto 17](https://aws.amazon.com/ko/about-aws/whats-new/2021/09/amazon-corretto-17-now-available/)
- Spring Boot : **[3.3.5](https://spring.io/projects/spring-boot#support)**
- Spring Framework: 6.1.14
  - Apache Tomcat/10.1.31
- Gradle : Gradle 8.10.2

### Project Package Structure
- 도메인 구조로 설계
```text
└── src
├── main
│   ├── java
│   │   └── com
│   │       └── musinsa
│   │           ├── domain
│   │           │   └── product
│   │           │       ├── controller
│   │           │       │   └── brand
│   │           │       ├── dto
│   │           │       ├── entity
│   │           │       ├── repository
│   │           │       │   └── Custom
│   │           │       │       └── Impl
│   │           │       └── service
│   │           └── global
│   │               ├── common
│   │               │   ├── exception
│   │               │   └── response
│   │               ├── config
│   │               │   ├── jpa
│   │               │   │   └── queryDsl
│   │               │   ├── security
│   │               │   └── swagger
│   │               ├── enum
│   │               │   └── error
│   │               └── utils
│   └── resources
│       ├── static
│       └── templates
└── test
``` 

### TEST 
1. [swagger 바로가기](http://localhost:8080/swagger-ui/index.html)
2. gradle
```text
  gradlew test
```