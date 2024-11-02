# Back-end 과제
이 프로젝트는 Spring Boot(3.3.5) 와 H2 Database를 사용하여 
무신사 과제에서 요구하는 기능을 Application 입니다. 

## 주요 기능
- **구현 1)** [카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API](http://localhost:8080/swagger-ui/index.html#/%EC%83%81%ED%92%88%20-%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC/category)
- **구현 2)** [단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API](http://localhost:8080/swagger-ui/index.html#/%EC%83%81%ED%92%88%20-%20%EB%B8%8C%EB%9E%9C%EB%93%9C/brands)
- **구현 3)** [카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API](http://localhost:8080/swagger-ui/index.html#/%EC%83%81%ED%92%88%20-%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC/getCategory)
- **구현 4)** 
  - [브랜드 및 상품 추가 API](http://localhost:8080/swagger-ui/index.html#/%EC%83%81%ED%92%88%20-%20%EB%B8%8C%EB%9E%9C%EB%93%9C/createCategory)
  - [브랜드 및 상품 수정 API](http://localhost:8080/swagger-ui/index.html#/%EC%83%81%ED%92%88%20-%20%EB%B8%8C%EB%9E%9C%EB%93%9C/updateCategory)
  - [브랜드 및 상품 삭제 API](http://localhost:8080/swagger-ui/index.html#/%EC%83%81%ED%92%88%20-%20%EB%B8%8C%EB%9E%9C%EB%93%9C/deleteCategory)
---  
## Project 스펙
- Java : [Corretto 17](https://aws.amazon.com/ko/about-aws/whats-new/2021/09/amazon-corretto-17-now-available/)
- Spring Boot : **[3.3.5](https://spring.io/projects/spring-boot#support)**
- Spring Framework: 6.1.14
  - Apache Tomcat/10.1.31
- Gradle : 8.10.2
- **H2 Database** (임베디드)
---
## 실행 방법
### 0. JDK 설치
JDK : [Amazon Corretto 17 다운로드](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)

### 1. 프로젝트 클론
Git을 통해 프로젝트를 로컬에 클론
```bash
git clone https://github.com/kimkh144/musinsa.git 
```
### 2. 프로젝트 이동
```bash
cd musinsa
```
### 3. 프로젝트 의존성 설치, 애플리케이션 실행
```bash
./gradlew bootRun
```
### 4. Application 정상 동작 확인
- 기본 포트는 8080이며, 브라우저에서 아래 URL을 입력
  - swagger-ui : http://localhost:8080/swagger-ui/index.html
  - h2-console : http://localhost:8080/h2-console
    - h2-console 접속 정보 (메모리 방식)
      - JDBC URL: jdbc:h2:mem:/musinsa
      - User Name: sa
      - Password: musinsa
    - 테이블 스키마 application 시작 시 create 및 종료 시 drop **(ddl-auto: create-drop)**
      ```text
      jpa:
          database-platform: org.hibernate.dialect.H2Dialect
          generate-ddl: true
          hibernate:
              ddl-auto: create-drop
          defer-datasource-initialization: true
      ```
---
## 프로젝트 패키지 구조
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
---
## Database
데이터베이스는 **H2 Database**를 사용하여 Spring Boot 애플리케이션과 연동되며, 주요 테이블 및 필드는 다음과 같습니다.
### 1. `TB_PRODUCT_BRAND_CATEGORY` 테이블

상품의 브랜드, 카테고리, 가격 관리를 위한 테이블 입니다.

| 필드 이름        | 데이터 타입      | 설명       | 기본키 |
|--------------|-------------|----------|-----|
| `BRAND`      | VARCHAR (8) | 상품 브랜드 명 | PK  |
| `CATEGORY`   | VARCHAR(128) | 상품 카테고리 명 | PK  |
| `PRICE`      | BIGINT      | 상품 가격    |     |

- **기본 키(Primary Key)**: BRAND, CATEGORY 복합 키 구성
- **인덱스**: 미 적용

### 2. 데이터베이스 초기화 및 스키마 설정

`src/main/resources` 디렉토리 내의 
1. 스키마 생성 : `schema.sql` 
2. 기초 데이터 등록: `data.sql`
---
## 컨벤션
### 1. Packages
- **Standard**: 모든 패키지 이름은 소문자로 구성하고, 단어는 마침표(`.`)로 구분.
- **Structure**: 기본적으로 `com.[company].` 구조를 사용, 하위 패키지는 기능별로 구분.

### 2. Classes and Interfaces
- **Class Names**: CamelCase 사용, 첫문자 대문자
- **Interface Names**: 일반적으로 클래스처럼 CamelCase 사용, `I` 접두사를 사용하지 않음.

### 3. Methods
- **Method Names**: camelCase 사용, 동사형.
- **Handler Methods in Controllers**: 메서드명은 HTTP 메서드와 관련된 의미를 포함.
  - 예시: `getUser()`, `createUser()`, `updateOrder()`, `deleteProduct()`

### 4. Variables
- **Local Variables and Parameters**: camelCase 사용, 변수의 역할을 나타내는 의미 있는 이름을 사용.
- **Constants**: 전체 대문자 사용, 단어 사이에 밑줄(`_`)을 사용.
  - 예시: `MAX_RETRIES`, `DEFAULT_TIMEOUT`

### 5. Database Naming Conventions (H2 Database)
- **Table Names**: 대문자 사용. `TB_` 접두사를 사용, 여러 단어는 밑줄(`_`)로 구분.
  - 예시: `TB_PRODUCT_BRAND_CATEGORY`
- **Column Names**: 대문자 사용, 여러 단어는 밑줄(`_`)로 구분.
  - 예시: `USER_ID`, `ORDER`, `CATEGORY`
---
### 공통 RESPONSE Payload 구성
```text
{
    "meta": {},   # 페이지네이션 정보 제공
    "data": {},   # API 응답 항목 제공
    "error": {    # API 응답 에러 발생 시 error 정보 제공
        "code": 400,    # http 응답 코드
        "errorCode": "string",  # error code (커스텀)
        "message": "string",    # error 메시지 
        "validation": "string"  # validation 오류 발생 사유 (@Valid 오류건)
    }
}
```
---
### TEST 방법
1. [swagger 바로가기](http://localhost:8080/swagger-ui/index.html)
![swagger-ui.png](src/main/resources/images/swagger-ui.png)
2. gradle
```text
  gradlew test
```