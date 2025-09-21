# studyroom-reservation

<br>

## 시스템 구성도

<img width="1280" height="449" alt="image" src="https://github.com/user-attachments/assets/fa2e7008-7d2f-424d-8d05-d0bef81ed9e8" />

<br>


## ERD
<img width="749" height="164" alt="image" src="https://github.com/user-attachments/assets/d0b098f3-217b-409a-b178-45838933c7ba" />

<br>


## API 명세서

### 공통 응답 DTO - CommonResponse

```json
{
	"message" : "회의실 등록 완료",
	"response" :
			"응답 DTO"
}

```

### CommonResponse (only 메세지)

```json
{
	"message" : "예약 취소 완료",
}

```

### API
스웨거 URL - http://localhost:8080/swagger-ui/index.html#

## LLM(ChatGpt) 사용 구간

- API 명세서 생성 / 공통 응답 DTO를 기반으로 미션에 있는 요구사항을 API 명세서로 만들어줘
- docker-compose.yml, Dockerfile, init.sql 생성 / docker-compose.yml을 통해서 springboot랑 postgresql을 실행하는데, tstzrange + EXCLUDE USING gist로 겹침 금지 제약까지 제약조건을 추가해줘
- 인증/인가 어노테이션 @AuthAquired 구현 / 어노테이션으로 특정 메서드만 인증/인가를 구현하고 싶어
- roomControllerTest / 인증/인가 컨트롤러 테스트 코드 짜줘
- ReservationReq DTO 유효성 검사 / 필드 간 비교도 유효성 검사 가능해?
- @ResController에 속하는 메서드만 @AuthAquired 적용 / 컨트롤러에 속하는 메서드만 어노테이션 적용하고 싶어
- @AuthRequired 사용 시 request 속성에서 role,userId 가져오기 / ADMIN이랑 USER 둘 다 허용하고 싶어
- 가용성 조회 쿼리, 서비스 코드 / 회의실,예약 내역, 빈 시간대를 조회하는 코드를 짜줘


## ADR

<br>
중복 예약 방지 전략(PostgreSQL EXCLUDE 제약)

**Status:** 적용

**Context:**

- 회의실 예약 서비스에서 시간대가 겹치면 안됨
- 단일 값이 아닌, 시간 범위에 대한 제약 조건이 필요함

**Decision:**

- PostgreSQL의 `tstzrange` 타입과 `EXCLUDE USING gist` 제약을 활용하여 시간 범위 제약 조건 생성
- `btree_gist` 확장을 이용하여 동일한 회의실에 대한 제약조건 추가

**Consequences:**

- 장점: DB 레벨에서 동일 회의실 시간대 중복 예약 방지 가능, 범위와 같은 까다로운 제약조건을 간단하게 설정 가능
- 단점: 동시에 예약 요청이 몰릴 경우 데드락 발생
- 영향: 락 대기 시간 설정 등 성능 저하에 대한 마련책 필요

---

RBAC 간소화 방식(커스텀 어노테이션을 사용한 간단한 인증/인가)

**Status:** 적용

**Context:**

- API의 권한에 따른 비즈니스 로직 분기
- 프로젝트의 규모에 맞는 간단한 인증/인가 필요

**Decision:**

- Spring Security를 사용하지 않고, 단순 토큰 기반으로 인증/인가 구현
- 커스텀 어노테이션을 통해 권한이 필요한 컨트롤러 메서드만 적용

**Consequences:**

- 장점: 같은 기능을 권한 별로 제약을 쉽게 설정 가능, 필터/인터셉터가 없어 통합 테스트를 쉽게 수행 가능
- 단점: 단순 토큰 검증 방식이므로 보안 강화가 필요함
