
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

### 커스텀 암호화 


구조는 다음과 같습니다. 
- Session Creation Policy : STATELESS



## JPA (ORM)


## Redis (Cache)
