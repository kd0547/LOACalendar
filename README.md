
## Project Structure
> 1인 개발 프로젝트입니다. 
> Spring Boot로 API를 구현했습니다. 추후 프론트엔드(React) 개발 예정입니다. 
> 사용한 기술 스택입니다.
- Spring Boot(API Server)
- Spring Security(Security)
- JPA (ORM)
- Redis (Cache)
- Mysql

- ERD : https://dbdiagram.io/d/64619089dca9fb07c4116924
- GitBoot : https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0


## Spring Boot(API Server)
> 클라이언트에서 요청한 데이터를 JSON으로 Response 한다. 

- config : 프로젝트 환경을 관리한다. 
- Exception : Custom Exception을 관리한다. 
- Security : Security, jwt 관련 기능들을 관리한다. 
- Web
  - Controller : API를 관리한다. 
  - Service : 비지니스로직을 관리한다. 
  - Repository : ㅓㅖㅁ
  - dto : request/response를 관리한다. 

## Spring Security(Security)
- CSRF : disable
- Token Authentication Filter : JwtAuthenticationFilter.class

### 커스텀 암호화 


구조는 다음과 같습니다. 
- Session Creation Policy : STATELESS



## JPA (ORM)


## Redis (Cache)



### 개발 환경
- 언어 : Java(JDK11),
- 서버 : Tomcat
- 프레임워크 : Spring Boot
- DB : Mysql, Redis
- API, 라이브러리 : JPA, Querydsl, Spring security



### 프로젝트 인원 및 기여도 
- 인원 : 1명 
- 기여 : 백엔드 전체



### 제작 기간
------------------

- 2022-12-11 ~ 2022-12-28:  ERD, 화면 설계 및 CRUD API 구현 
- 2022-12-29 ~ 2023-01-05:  CRUD API 구현 및 API 문서화 
- 2023-01-06 ~ 2023-01-10:  RefreshToken 추가 및 1차 리팩토링(공통 Exception 분리 및 상태코드 추가) 
- 2023-01-11 ~ 2023-xx-xx:  개발 중 놓쳤던 CRUD API의 문제을 파악하고 수정했습니다. 


### 구현 기능
- RESTful 규약을 준수하여 URL 설계와 API Spec(HTTP Method, Status Code)을 만족하는 API 개발
- DES 암호화 알고리즘과 Base64를 이용해 공유 URL 생성하고 캘린더를 다른 사람과 공유할 수 있도록 제작
- 클라이언트에서 데이터 요청 시 JSON 에 RAW 데이터로 요청하는 문제점을  RSA 알고리즘을 사용해 패스워드 암호화 구현
- JWT를 확용해 보안을 적용하고 State-less 방식의 한계를 보완하기 위해 Redis를 이용해 DB 자원 접근을 최소화
### 개발 과정 중 생긴 문제와 해결 내용
- 레이드 계획 수정 메서드에서 Update 쿼리가 2번 이상 발생하는 문제 [해결과정](https://jade-frill-5b8.notion.site/update-e111eb551d2a4fdba2e2dfafaf5ca27e)
- 
- 
### 아쉬운 점과 개선되어야할 점 
- 처음 설계 부터 개발 과정까지 이력을 작성하지 못한 것입니다. 단위 테스트로 개발을 진행해 문제점들을 바로 해결하고 이력으로 남기지 못 해 진행된 내용 파악이 늦어 개발 기간이 늘어났습니다. 
- API 문서를 처음 만들어서 처음 접할 때 쉽게 이해할 수 있도록 만들기 어려웠습니다. 
- GuildUser Entity에 길드명이 필요해서 컬럼을 추가했지만 Guild Entity의 길드명을 변경할 때 GuildUser의 길드명을 수정할 UPDATE 쿼리가 최대 50개 까지 발생했습니다. 
요청 시 Guild 와 GuildUser 각각 1번씩 2번의 SELECT 쿼리문이 발생하는 것이 비효율적이라고 생각한 것이 잘못되었습니다. 길드명은 자주 변경되는 컬럼이 아니고 Bulk를 사용해 문제를 보완했습니다. 
- 
### 다음 계획 
- Oauth2 인증을 이용해 카카오와 구글 등으로 가입이 가능하도록 할 것입니다. 
- 실제 서비스가 가능하도록 프론트 엔드 개발을 진행할 것입니다.
- 
### 개발 이력 
2023 - 01 - 25
- 레이드 계획 수정 메서드에서 Update 쿼리가 2번 이상 발생하는 문제 해결
- 다른 소유자의 데이터를 계획에 포함하는 문제와 존재하지 않는 ID로 접근 시 예외가 발생하지 않는 문제 해결
- 
2023 - 01 - 19
- 레이드 계획 생성과 수정에서 참여 인원이 레이드에서 요구하는 시작 인원과 맞지 않으면 저장 또는 수정할 수 없도록 변경했습니다.
