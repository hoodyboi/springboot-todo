# 📝 Spring Boot Todo API

간단한 할 일 관리 API 서버입니다.  
Spring Boot, MySQL, JPA 기반으로 제작되었으며, 현재 EC2 서버에 배포되어 실행 중입니다.

---

## 🚀 배포 주소

- Swagger UI: [http://15.164.245.146:8080/swagger-ui/index.html](http://15.164.245.146:8080/swagger-ui/index.html)

---

## ✅ 주요 기능

- 할 일 등록 (POST /todos)
- 할 일 전체 조회 / 조건 조회 (GET /todos)
- 할 일 수정 (PUT /todos/{id})
- 할 일 삭제 (DELETE /todos/{id})
- 완료 여부 토글 (PATCH /todos/{id}/toggle)
- 완료된 할 일만 조회 (GET /todos/completed)
- 페이징, 정렬, 필터링 기능 포함

---

## 🔐 인증 기능 (JWT 기반)

- 회원가입 / 로그인 API 제공
- 로그인 시 JWT 발급 (Access + Refresh Token)
- JWT 토큰을 이용한 사용자 인증
- 인증된 사용자만 자신의 Todo 접근 가능
- 토큰 만료 시 `/api/reissue` 엔드포인트로 재발급 가능
- Swagger UI에서도 `Authorize` 버튼으로 JWT 인증 테스트 가능

---

## 🛠 기술 스택

| 영역 | 기술 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3 |
| 데이터베이스 | MySQL 8 (EC2 내부 설치) |
| ORM | Spring Data JPA |
| 문서화 | SpringDoc OpenAPI (Swagger UI) |
| 보안 | Spring Security + JWT |
| 배포 | AWS EC2 (Ubuntu 22.04) |
| 빌드 | Gradle (Jar 패키징) |

---

## 📁 프로젝트 구조

```
src
├── main
│   ├── java
│   │   └── com.example.springstart
│   │       ├── controller     # REST API 엔드포인트
│   │       ├── service        # 비즈니스 로직 처리
│   │       ├── repository     # JPA 인터페이스 (DB 접근)
│   │       ├── domain         # Entity 클래스
│   │       ├── jwt            # JWT 유틸/필터
│   │       ├── security       # 인증/인가 예외 처리
│   │       └── dto            # 요청/응답 DTO
│   └── resources
│       ├── application.properties
│       └── static / templates (필요 시)
├── test
│   └── com.example.springstart
│       └── controller         # 테스트 클래스
```

---

## 📄 API 명세 요약

| Method | URL | 설명 |
|--------|-----|------|
| `POST` | `/api/signup` | 회원가입 |
| `POST` | `/api/login` | 로그인 (JWT 발급) |
| `POST` | `/api/reissue` | Access Token 재발급 |
| `GET` | `/todos` | 전체 할 일 조회 (페이징/필터 지원) |
| `POST` | `/todos` | 새 할 일 등록 |
| `PUT` | `/todos/{id}` | 할 일 내용 수정 |
| `PATCH` | `/todos/{id}/toggle` | 완료 여부 토글 |
| `GET` | `/todos/completed` | 완료된 항목만 조회 |
| `DELETE` | `/todos/{id}` | 할 일 삭제 |

> ✅ 모든 API 요청/응답 구조는 Swagger UI에서 확인 가능

---

## 🐳 배포 방식

- 로컬에서 Gradle로 `.jar` 파일 빌드
- EC2(Ubuntu)에 SCP 또는 Git pull로 전송
- `java -jar` 명령어로 EC2에서 실행
- EC2 보안 그룹에서 8080 포트 오픈
- Swagger UI를 통해 실시간 API 테스트 가능

---

## ✅ 특징 요약

- JWT 기반 인증 + 권한 처리
- 예외 상황에 대한 통일된 JSON 응답 포맷 제공
- Swagger 문서 자동화 및 예시 연동
- 사용자별 데이터 분리 처리
- EC2 서버 실시간 배포 운영

---

## 🙋‍♂️ 개발자

- 이름: 최종민  
- GitHub: [https://github.com/hoodyboi](https://github.com/hoodyboi)  
- Email: hoodyboi@naver.com
