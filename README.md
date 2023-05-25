### 구현 기능 정리
- RESTful 규약을 준수하여 URL 설계와 API Spec(HTTP Method, Status Code)을 만족하는 API 개발
- DES 암호화 알고리즘과 Base64를 이용해 공유 URL 생성하고 캘린더를 다른 사람과 공유할 수 있도록 제작
- 클라이언트에서 데이터 요청 시 JSON 에 RAW 데이터로 요청하는 문제점을  RSA 알고리즘을 사용해 패스워드 암호화 구현
- JWT를 확용해 보안을 적용하고 State-less 방식의 한계를 보완하기 위해 Redis를 이용해 DB 자원 접근을 최소화

>
### 개발 고민
- 레이드 계획 수정 메서드에서 Update 쿼리가 2번 이상 발생하는 문제 [해결과정](https://jade-frill-5b8.notion.site/update-e111eb551d2a4fdba2e2dfafaf5ca27e)

- 처음 설계 부터 개발 과정까지 이력을 작성하지 못한 것입니다. 단위 테스트로 개발을 진행해 문제점들을 바로 해결하고 이력으로 남기지 못 해 진행된 내용 파악이 늦어 개발 기간이 늘어났습니다. 
- API 문서를 처음 만들어서 처음 접할 때 쉽게 이해할 수 있도록 만들기 어려웠습니다. 

- GuildUser Entity에 길드명이 필요해서 컬럼을 추가했지만 Guild Entity의 길드명을 변경할 때 GuildUser의 길드명을 수정할 UPDATE 쿼리가 최대 50개 까지 발생했습니다. 
요청 시 Guild 와 GuildUser 각각 1번씩 2번의 SELECT 쿼리문이 발생하는 것이 비효율적이라고 생각한 것이 잘못되었습니다. 길드명은 자주 변경되는 컬럼이 아니고 Bulk를 사용해 문제를 보완했습니다. 

## Project Structure
> 1인 개발 프로젝트입니다. 
> Spring Boot로 API를 구현했습니다. 추후 프론트엔드(React) 개발 예정입니다. 
> 사용한 기술 스택입니다.
- Spring Boot(API Server)
- Spring Security(Security)
- JPA (ORM)
- Redis (Cache)
- Mysql

### 개발 중
> 2023.05.25 부터 시작
- Spring Batch (일정 관리) - 개발중
- Spring OAuth2.0 (구글, 카카오) - 예정
- DicodeBot (사용 중인 커뮤니티 서버 연동)


###
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
  - Repository : 
  - dto : request/response를 관리한다. 

## Spring Security(Security)
구조는 다음과 같습니다. 
- Session Creation Policy : STATELESS
- CSRF : disable
- Token Authentication Filter : JwtAuthenticationFilter.class


### 커스텀 암호화 
> 유저의 이메일과 패스워드 암호화에 RSA, 캘린더 공유에 DES 암호화를 사용했습니다. 
   
#### 캘린더공유URL 생성 메서드 
```
@PostMapping("/share/{id}")
public ResponseEntity<?> shreURLCreate(Principal principal,@PathVariable("id") Long id) {
	String username = principal.getName();
	Long memberId = memberService.findByEmail(username);
	CalendarShareURL calendarShareURL = new CalendarShareURL
		.Builder()
			.shareCalendarId(id)
			.memberId(memberId)
			.expiredTime(LocalDate.now().plusDays(30L))
			.build();
		
	try {
		ShareURL shareURL = new ShareURL();
		String rawData = objectMapper.writeValueAsString(calendarShareURL);
		String encode = desEncryption.encryptURL(rawData);
		
		shareURL.setUrl(encode);
			
		return ResponseEntity.status(HttpStatus.CREATED).body(shareURL);
	} catch (JsonProcessingException e) {
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorCode(HttpStatus.INTERNAL_SERVER_ERROR,"서버 장애 발생"));
	}
}


```
#### email, password 암호화용 공유키 전송
```
@GetMapping("/encrypt") 
public ResponseEntity<?> loginencrypt(){
	LoginEncryption loginEncryption = new LoginEncryption(); 
 
	loginEncryption.setAlgorithm(keyPair.getPublic().getAlgorithm());
	loginEncryption.setFormat(keyPair.getPublic().getFormat());
	loginEncryption.setEncoded(keyPair.getPublic().getEncoded());

	return ResponseEntity.status(HttpStatus.OK).body(loginEncryption);
}
```


## JPA (ORM)


## Redis (Cache)
> 

![제목 없는 다이어그램](https://github.com/kd0547/LOACalendar/assets/86393702/b0dac306-c6e1-4886-8f45-a4a8c5a61c64)

- IpUserDetailsToken <-> String 변환
```
public class RedisServiceImpl implements RedisService{
...
  @Override
  public void setData(String key, Object value) {
	  if(key == null || value == null) 
		  throw new NullPointerException();
		
	  String valueString = paserUtil.ObjectToJSON(value);
	  jwTokenRedisService.setData(key, valueString);
 }
...
  @Override
  public <T> Object getData(String key,Class<T> classes) {
	  String object = (String) jwTokenRedisService.getData(key, classes);
	  if(object == null) {
		  throw new IllegalStateException("-102");
	  }
	return paserUtil.JsonToObject(object, classes);
  }
  ...
}
```
- setData()메서드는 IpUserDetailsToken을 String으로 변환합니다. 
- 


## DicodeBot 연동 과정
>
>
![discode_연동](https://github.com/kd0547/LOACalendar/assets/86393702/d5978c47-1173-4619-86b7-e1976354a78e)


### 라이센스 키 생성 
> 공유할 캘린더의 ID와 이메일을 암호화해 디스코드 봇 인증용 라이센스 키를 생성합니다.
```
테스트 데이터 : {"email":"user","calendarID":1}
예상 결과 : E7910202-805D0654-30D4F579-F8116701-75B7AFC5-F7CF40BB-955E151B-F8848644
```
#### @Test
```
@Test
@WithMockUser
void issueLicenseTest() throws Exception {
	MvcResult result = mockMvc.perform(get("/license/1"))
	.andExpect(status().isOk())
	.andDo(print())
	.andReturn();
		
	String content = result.getResponse().getContentAsString();
		
	assertThat(TEST_data).isEqualTo(content);	
}
```
### 라이센스 키 검증 



- 


