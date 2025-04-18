# LOACalendar: 로스트아크 길드 캘린더 관리 시스템

LOACalendar는 로스트아크(Lost Ark) 게임을 즐기는 유저들이 길드 및 파티 단위의 레이드 일정을 손쉽게 조율할 수 있도록 돕는 웹 애플리케이션입니다. 기존에 Excel 스프레드시트로 복잡하게 관리하던 파티 일정을 웹 기반의 캘린더 UI를 통해 보다 직관적이고 실시간으로 관리할 수 있도록 만든 프로젝트입니다.

---

## 프로젝트 개요

- **프로젝트명:** LOACalendar
- **진행 기간:** 2023.08 ~ 2023.11 (1차 개발 종료) / 이후 개인 백엔드 학습 프로젝트로 확장 중

### 프로젝트 목적

- 커뮤니티 내 길드원 간의 레이드 일정을 쉽게 조율하고 시각적으로 확인할 수 있도록 함
- 디스코드, 모바일 환경 등 다양한 플랫폼에서도 일정을 간편하게 확인 및 공유 가능

---

## 주요 기능

| 분류 | 기능 설명 |
|------|------------|
| 인증/보안 | 회원가입, 로그인, JWT 기반 인증, Spring Security 적용 |
| 유저 관리 | 길드 가입, 역할 부여, 길드 내 권한 분리 및 구성원 관리 |
| 캘린더 기능 | 날짜별 레이드 등록, 참여 여부 표시, 계획 세부 사항 설정 |
| 레이드 관리 | 군단장 레이드, 어비스 레이드 등 다양한 레이드 타입 등록 지원 |
| 공유 기능 | 일정 링크 공유 및 추후 디스코드 Webhook 연동 예정 |
| 알림 기능 | (예정) 레이드 시작 전 알림 - Web Push / Discord Bot 연동 |
| 캐싱 처리 | Redis를 활용한 로그인 세션, 스케줄 데이터, 참여 인원 등 캐싱 처리 |
| 감사 로그 | (예정) 유저 활동 이력 로깅 기능 구현 예정 |

---

## 기술 스택 (Tech Stack)

| 구분 | 기술 |
|------|------|
| 언어 및 런타임 | Java 17 |
| 웹 프레임워크 | Spring Boot, Spring MVC |
| 인증/보안 | Spring Security, JWT |
| ORM | Spring Data JPA |
| 데이터 저장소 | MySQL (RDB), Redis (세션/캐시/큐) |
| 문서화 도구 | Swagger (Springdoc OpenAPI) |
| 인프라 환경 | Docker |
| 형상관리 및 협업 | Git, GitHub |

---

## ERD & 데이터베이스 설계

LOACalendar는 실제 길드 운영을 반영한 도메인 기반 모델로 설계되어 있으며, 사용자(User), 길드(Guild), 레이드(Raid), 일정(RaidSchedule), 참가자(RaidParticipant) 등을 중심으로 구성되어 있습니다.

### 사용자 (users)
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255),
    role ENUM('USER', 'ADMIN') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 길드 (guilds)
```sql
CREATE TABLE guilds (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 길드 멤버십 (guild_memberships)
```sql
CREATE TABLE guild_memberships (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    guild_id BIGINT NOT NULL,
    role ENUM('LEADER', 'MEMBER') NOT NULL,
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);
```

### 레이드 (raids)
```sql
CREATE TABLE raids (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    level INT,
    boss_name VARCHAR(255)
);
```

### 레이드 일정 (raid_schedules)
```sql
CREATE TABLE raid_schedules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    raid_id BIGINT NOT NULL,
    guild_id BIGINT NOT NULL,
    scheduled_at DATETIME NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (raid_id) REFERENCES raids(id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);
```

### 레이드 참가자 (raid_participants)
```sql
CREATE TABLE raid_participants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    raid_schedule_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(50),
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (raid_schedule_id) REFERENCES raid_schedules(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

---

### 관계 요약
- users 1:N guild_memberships / guilds 1:N guild_memberships
- users 1:N raid_participants / raid_schedules 1:N raid_participants
- guilds 1:N raid_schedules
- raids 1:N raid_schedules

---

## 설치 및 실행 방법

### 1. Git 클론 및 의존성 설치
```bash
git clone https://github.com/your-repo/LOACalendar.git
cd LOACalendar
```

### 2. Docker로 MySQL & Redis 실행
```bash
docker-compose up -d
```

### 3. Gradle 빌드 및 애플리케이션 실행
```bash
./gradlew build
./gradlew bootRun
```

- 기본 실행 주소: http://localhost:8080

---

## 참고 학습 자료
- 실전! 스프링 부트와 JPA 활용 1, 2

---

## 향후 개발 및 계획

| 기능 | 설명 |
|------|------|
| 알림 기능 | WebPush 또는 Discord Bot을 활용한 레이드 시작 전 알림 기능 도입 |
| OAuth2 로그인 | Discord, Google 기반 소셜 로그인 연동 |
| 프론트엔드 SPA 분리 | React 기반 프론트엔드 독립 분리 및 API 기반 연동 구성 |
| 사용자 통계 기능 | 레이드 참여 횟수, 포지션별 기여도 기반 통계 조회 기능 |
| 테스트 코드 보완 | JUnit, Mockito 등으로 단위 및 통합 테스트 확장 |
| 다국어(i18n) 지원 | 한국어, 영어, 일본어 UI 다국어 처리 구성 예정 |
| CI/CD 파이프라인 | GitHub Actions 기반 자동 테스트 및 Docker 배포 파이프라인 구축 |

---

