### 사용한 기술 및 프레임워크 
- Spring Boot(API Server)
- Spring Security(Security)
- JPA (ORM)
- Redis (Cache)
- Mysql


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
> Spring Security을 사용한 보안 적용 뿐만 아니라 디스코드 서버 인증,캘린더 공유용 URL 생, 로그인 암호화용 로직을 구현했습니다. 

### Srping Security config
```JAVA
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	http.cors()
	.and()
		.csrf().disable()
		.exceptionHandling()
		.authenticationEntryPoint(unAuthorizedHandler)
	.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	.and()
            	.authorizeRequests() 
            	.antMatchers("/calendar/share/url/**","/auth/**").permitAll()
            	.antMatchers("/**")
            	.authenticated()
        .and()
        .formLogin().disable().headers().frameOptions().disable();
	;	
	http.addFilterBefore(new JwtAuthenticationFilter(jwTokenProvider(),redisService),UsernamePasswordAuthenticationFilter.class);
	return http.build();
  }
```
RESTful API 사용하기 위해 `.csrf().disable()`와 `SessionCreationPolicy.STATELESS` 설정을 했습니다. 또한 `UsernamePasswordAuthenticationFilter`에서 발생한 오류를 `.authenticationEntryPoint(unAuthorizedHandler)`에서 처리하도록 설정했습니다. 

#### 캘린더공유URL 생성 메서드 
URL을 공유해 URL을 
```JAVA
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
#### 로그인용 암호화화
```JAVA
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
> Redis에 최신 토큰 정보를 저장하여 토큰의 중복 생성 및 접근을 방지했습니다. 아래는 환경 설정과 구현 코드입니다.

#### 환경 설정
> Redis 캐시의 만료 시간과 AccessToken의 만료 시간을 설정하여, 
> 토큰의 재발급이 없을 경우 캐시가 삭제되고 사용자가 자동으로 로그아웃되도록 구현하였습니다. 
```JAVA
public class JwtProvider {
	private static final ChronoUnit RefreshTokenValidMinutes = ChronoUnit.DAYS;
	private static final Long AccessTokenTime = 14L;
}

@Configuration
public class RedisConfig {
	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,ResourceLoader loader) {
		RedisCacheConfiguration configuration = 
		RedisCacheConfiguration
			.defaultCacheConfig(loader.getClassLoader())
			.disableCachingNullValues()
			.entryTtl(Duration.ofDays(15L));
	}
}
```

#### 로그인
>
```JAVA
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {	
	String plainPassword = decryptPassword(loginRequest.getSecret());
	loginRequest.setSecret(plainPassword);
	//유저 인증 
	Member findMember = memberService.findByEmail(loginRequest);	
	Authentication authentication = createUsernamePasswordAuthenticationToken(findMember.getEmail(),findMember.getRole().toString());
		
	//중복 로그인 방지 
	if(isHaveUsernameRedis(findMember.getUsername())) {
		String username = findMember.getUsername();
			
		//redis에 저장한 토큰값을 가져온다.
		CustomToken redisToken = (CustomToken) redisService.getData(username,CustomToken.class);
			
		LoginResponse token = new LoginResponse(redisToken.getAccessToken(),redisToken.getRefreshToken());
		return ResponseEntity.status(HttpStatus.OK).body(token);
	} else {
		//엑세스 토큰 생성
		CustomToken customToken =  customTokenProvider.generateToken(authentication);
		//토큰 Redis 저장
		redisService.setData(authentication.getName(),customToken);
		LoginResponse token = new LoginResponse(customToken.getAccessToken(),customToken.getRefreshToken());
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
}
```
#### 토큰 발급
> 유효하지 않은 토큰 값으로부터 새로운 토큰을 발급하는 상황을 방지하였습니다.
```JAVA
@PostMapping("/refresh") 
public ResponseEntity<?> createRefreshToken(HttpServletRequest request) {
	String accessToken = getJwtFromRequest(request);
	String refreshToken = getRefreshJwtFromRequest(request);
		
	//Refresh Token 유효성 검사
	Claims claims = customTokenProvider.validateToken(refreshToken);
		
	String username = claims.getIssuer();
	String roles = customTokenProvider.extractRoles(claims);
	CustomToken customToken = (CustomToken) redisService.getData(username, CustomToken.class);
		
	//redis에 있는 Refresh Token만 사용할 수 있다.
	if(isNotRefreshTokenAlreadyUsed(refreshToken,customToken.getRefreshToken()) 
			&& isNotAccessTokenAlreadyUsed(accessToken,customToken.getAccessToken())) {
		throw new IllegalArgumentException("-110");
	}
		
	Authentication authentication = createUsernamePasswordAuthenticationToken(username,roles);
	CustomToken newCustomToken = (CustomToken) customTokenProvider.generateToken(authentication);
		
	//Redis에서 기존 Token을 삭제하고 새로 생성한 Token을 저장한다.
	redisService.deleteData(username);
	redisService.setData(authentication.getName(), newCustomToken);
		
	LoginResponse token = new LoginResponse(newCustomToken.getAccessToken(),newCustomToken.getRefreshToken());
	return ResponseEntity.status(HttpStatus.OK).body(token);
	}
```

#### 토큰 인증
> 요청에서 Redis에 보관 중인 AccessToken과 비교하여 해당 토큰이 사용 가능한지 확인하는 기능을 구현하였습니다.
 
```JAVA
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	private final CustomJwtProvider customTokenProvider;
	private final RedisService redisService;
	
	public JwtAuthenticationFilter(CustomJwtProvider customTokenProvider, RedisService redisService) {
		this.customTokenProvider = customTokenProvider;
		this.redisService = redisService;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		...
			String accessToken = getJwtFromRequest(request);
			Claims claims = customTokenProvider.validateToken(accessToken);
			String username = claims.getSubject();
			String role = customTokenProvider.extractRoles(claims);
		
			//redis에서 유저 데이터를 조회한다.
			CustomToken customToken = (CustomToken) redisService.getData(username, CustomToken.class);
			
			if(!accessToken.equals(customToken.getAccessToken()) ) {
				throw new InvalidTokenException("4");
			}
			UserDetails userDetails =  User.builder()
				.username(username)
				.roles(role)
				.build();
		
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		...
	}
}

```


## DicodeBot 연동 과정
>
>
//![discode_연동](https://github.com/kd0547/LOACalendar/assets/86393702/d5978c47-1173-4619-86b7-e1976354a78e)


### 라이센스 키 생성 
> 공유할 캘린더의 ID와 이메일을 암호화해 디스코드 봇 인증용 라이센스 키를 생성합니다.
```JAVA
@GetMapping("/{id}")
public ResponseEntity<?> issueLicense(Principal principal,@PathVariable Long id) {

	String email = principal.getName();
	Long calendarId = id;
		
	LicenseKeyDto keyDto = new LicenseKeyDto.Builder()
			.email(email)
			.calendarID(calendarId)
			.expiredTime(LocalDateTime.now().plusMinutes(30L))
			.build();
		
	String json = null;
	json = objectMapperUtil.writeValueAsString(keyDto);
	String key = license.Issue(json);
		
	return ResponseEntity.status(HttpStatus.OK).body(key);
}
```

```JAVA
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


### 알람 기능 
> 레이드 일정을 디스코드 서버에 전송하는 알람 기능을 스프링스케줄러를 이용해 구현했습니다.

#### SQL문 작성 
>  discode_info, calendar_detail, raid_plan, guild_user 테이블을 조인하여 알림 기능이 활성화된 일정 정보를 가져옵니다
```SQL
SELECT d.channel_id AS channelId,
	r.raid_start_date AS startDate,
        r.raid_start_time AS startTime,
        r.legion_raid AS legionRaid,
        c.guild_user_id AS guildUserId,
        c.raid_plan AS raidPlan,
        g.username AS username,
        g.level AS level,
        g.loa_class AS loaClass
FROM discode_info d
	INNER JOIN calendar_detail c ON d.calendar_id = c.calendar_id
        INNER JOIN raid_plan r ON c.raid_plan = r.raid_plan
        INNER JOIN guild_user g ON g.guild_user_id = c.guild_user_id
WHERE d.alarmyn = 'Y'
	AND r.raid_start_date = :startDateValue
        AND r.raid_start_time BETWEEN :startTimeValue AND :endTimeValue
```
| channelId | startDate | startTime | legionRaid | guildUserId | raidPlan | username | level | loaClass |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 비공개| 2023-06-23 | 11:00:00 | KOUKUSATON | 8 | 9 | 비공개 | 1601 | DESTROYER |
| 비공개 | 2023-06-23 | 11:00:00 | KOUKUSATON | 5 | 9 | 비공개 | 1560 | SCRAPPER |
| 비공개 | 2023-06-23 | 11:00:00 | KOUKUSATON | 3 | 9 | 비공개 | 1560 | BARD |
| 비공개 | 2023-06-23 | 11:00:00 | KOUKUSATON | 9 | 9 | 비공개 |1580 | AEROMANCER |

데이터 베이스에서 가져온 데이터를 raidPlan을 기준으로 AlarmDto 클래스를 만듭니다. 

>
#### 스케줄러 설정
```JAVA
@Component
public class AlarmScheduler {
	
	@Autowired
	private ThreadPoolTaskScheduler scheduler;
	
	@Autowired 
	private AlarmService alarmService;
	
	@Autowired
	private DiscodeSender discodeSender;
	
	private Map<String, ScheduledFuture<?>> jobMap = new HashMap<>();
	
	
	@Scheduled(cron = "0 30 23 * * *")
	public void mainTask() {
		LocalDate now = LocalDate.now().plusDays(1);
		LocalTime start = LocalTime.of(0, 0);
		LocalTime end = LocalTime.of(23, 59);
		
		List<AlarmDto> alarmDtos = alarmService.findAlarmsByTimeRange(now, start, end);
		Collections.sort(alarmDtos);
		
		
		runTask(now,alarmDtos);
	
	}

    public void runTask(LocalDate now,List<AlarmDto> alarmDtos) {
		AlarmExecutor alarmExecutor = new AlarmExecutor(alarmDtos,discodeSender);
		alarmExecutor.setNow(now);
		ScheduledFuture<?> scheduledFuture = scheduler.schedule(alarmExecutor, new CronTrigger("0 * * * * ?"));
    
    }
}
```


#### 스케줄러 실행
> 
```JAVA
@Override
public void run() {

	try {
		LocalDate today = LocalDate.now();
		if (today.isEqual(expiredtime)) {
			throw new InterruptedException("error");
		}
		if(alarmDtos.isEmpty()) {
			throw new InterruptedException("success");
		}			

		while (!alarmDtos.isEmpty()) {
			LocalTime now = LocalTime.now();
			AlarmDto alarmDto = alarmDtos.get(0);
				
			// 레이드 시간 < 현재시간 
			// ex) 12:00 < 현재시간
			if (checkTimeOver(now, alarmDto)) {
				alarmDto.setUsed(true);
				alarmDtos.remove(0);
				continue;
			}
				
			// 레이드 시간 - 15 <= 현재시간 < 레이드 시간 
			// ex) 12:00 < 현재시간 < 12:15
			if (checkAlarmTimeRange(now, alarmDto) && isNotUsed(alarmDto)) {
				discodeSender.channelMessageSender(alarmDto);
				alarmDto.setUsed(true);
				alarmDtos.remove(0);
				continue;
			}
				
			//  현재시간 < 레이드 시간 - 15
			//  현재시간 < 22:30 - 15분
			if(isTimeThresholdNotReached(now,alarmDto)) {
				throw new InterruptedException("success");
			}	
		}
	} catch (InterruptedException e) {
		String code = e.getMessage();
		if (code.equals("success")) {
	
		}

		if (code.equals("error")) {

		}
	}
}
```

### 구현 기능 정리
- RESTful 규약을 준수하여 URL 설계와 API Spec(HTTP Method, Status Code)을 만족하는 API 개발
- DES 암호화 알고리즘과 Base64를 이용해 공유 URL 생성하고 캘린더를 다른 사람과 공유할 수 있도록 제작
- 클라이언트에서 데이터 요청 시 JSON 에 RAW 데이터로 요청하는 문제점을  RSA 알고리즘을 사용해 패스워드 암호화 구현
- JWT를 확용해 보안을 적용하고 State-less 방식의 한계를 보완하기 위해 Redis를 이용해 DB 자원 접근을 최소화


### 목표 기능 
> 2023.05.25 부터 시작했습니다.
- 서버에 저장된 사용자의 일정을 디스코드 커뮤니티에 알림으로 전송하는 기능 
- DicodeBot (인증및 연동 기능)
- Spring OAuth2.0 (구글, 카카오) - 예정


### 개발 고민
- GuildUser의 정보를 로스트아크 홈페이지에서 받아와 존재하는 유저인지를 확인하는 기능을 구현해야할까 고민중입니다. 

- 레이드 계획 수정 메서드에서 Update 쿼리가 2번 이상 발생하는 문제 [해결과정](https://jade-frill-5b8.notion.site/update-e111eb551d2a4fdba2e2dfafaf5ca27e)
- 처음 설계 부터 개발 과정까지 이력을 작성하지 못한 것입니다. 단위 테스트로 개발을 진행해 문제점들을 바로 해결하고 이력으로 남기지 못 해 진행된 내용 파악이 늦어 개발 기간이 늘어났습니다. 
- API 문서를 처음 만들어서 처음 접할 때 쉽게 이해할 수 있도록 만들기 어려웠습니다. 
- GuildUser Entity에 길드명이 필요해서 컬럼을 추가했지만 Guild Entity의 길드명을 변경할 때 GuildUser의 길드명을 수정할 UPDATE 쿼리가 최대 50개 까지 발생했습니다. 
요청 시 Guild 와 GuildUser 각각 1번씩 2번의 SELECT 쿼리문이 발생하는 것이 비효율적이라고 생각한 것이 잘못되었습니다. 길드명은 자주 변경되는 컬럼이 아니고 Bulk를 사용해 문제를 보완했습니다. 
