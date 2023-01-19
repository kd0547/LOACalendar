### 프로젝트 인원 및 기여도 
- 인원 : 1명 
- 기여 : 백엔드 전체

//ERD, 화면설계 파일 추가하기 

### 제작 기간
------------------
- 2022-12-11 ~ 2022-12-28:  ERD, 화면 설계 및 CRUD API 구현 
- 2022-12-29 ~ 2023-01-05:  CRUD API 구현 및 API 문서화 
- 2023-01-06 ~ 2023-01-10:  RefreshToken 추가 및 1차 리팩토링(공통 Exception 분리 및 상태코드 추가) 
- 2023-01-11 ~ 2023-01-19:  개발 중 놓쳤던 CRUD API의 문제을 파악하고 수정했습니다. 

### 구현 기능
- DES 암호화 알고리즘과 Base64를 이용해 공유 URL 생성하고 캘린더를 다른 사람과 공유할 수 있도록 제작
- 클라이언트에서 데이터 요청 시 JSON 에 RAW 데이터로 요청하는 문제점을 파악하고 RSA 알고리즘을 사용해 패스워드 암호화 구현
- JWT와 Redis를 이용해 인증 구현 
- 
- RESTful 기반의 CRUD API 제작 

### API URL
------------------
| url | API 설명 |
| ------------- | ------------- |
| /member  | https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/member  |
| /auth | https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/auth  |
| /calendar | https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/clendar |
| /plan | https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/plan |
| /guild | https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/guild |

### 개발 과정 중 생긴 문제와 해결 내용
- 레이드 계획 수정 메서드에서 Update 쿼리가 2번 발생하는 문제 
- Exception 발생 시 rollback되지 않고 저장되는 문제 
- 

### 아쉬운 점과 개선되어야할 점 
- 처음 설계 부터 개발 과정까지 이력을 작성하지 못한 것입니다. 단위 테스트로 개발을 진행해 문제점들을 바로 해결하고 이력으로 남기지 못 해 진행된 내용 파악이 늦어 개발 기간이 늘어났습니다. 
- API 문서를 처음 만들어서 처음 접할 때 쉽게 이해할 수 있도록 만들기 어려웠습니다. 
- GuildUser Entity에 길드명이 필요해서 컬럼을 추가했지만 Guild Entity의 길드명을 변경할 때 GuildUser의 길드명을 수정할 UPDATE 쿼리가 최대 50개 까지 발생했습니다. 
요청 시 Guild 와 GuildUser 각각 1번씩 2번의 SELECT 쿼리문이 발생하는 것이 비효율적이라고 생각한 것이 잘못되었습니다. 길드명은 자주 변경되는 컬럼이 아니고 Bulk를 사용해 문제를 보완했습니다. 
- 



### 다음 계획 
- 


### 개발 이력 
2023 - 01 - 19
- 레이드 계획 생성과 수정에서 참여 인원이 레이드에서 요구하는 시작 인원과 맞지 않으면 저장 또는 수정할 수 없도록 변경했습니다.
-
