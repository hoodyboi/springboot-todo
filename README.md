# 📝 Spring Boot Todo API

간단한 할 일 관리 API 서버입니다.  
Spring Boot, MySQL, JPA 기반으로 제작되었으며, 현재 EC2 서버에 배포되어 실행 중입니다.

---

## 🚀 배포 주소

- Swagger UI: [http://15.164.245.146:8080/swagger-ui.html](http://15.164.245.146:8080/swagger-ui.html)

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

## 🛠 기술 스택

| 영역 | 기술 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3 |
| 데이터베이스 | MySQL 8 (EC2 내부 설치) |
| ORM | Spring Data JPA |
| 문서화 | SpringDoc OpenAPI (Swagger UI) |
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
│   │       └── dto            # 요청/응답 DTO
│   └── resources
│       ├── application.properties
│       └── static / templates (필요 시)
├── test
│   └── com.example.springstart
│       └── SpringstartApplicationTests.java
```

---

## 📄 API 명세 요약

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/todos` | 전체 할 일 조회 (페이징/필터 지원) |
| `POST` | `/todos` | 새 할 일 등록 |
| `PUT` | `/todos/{id}` | 할 일 내용 수정 |
| `PATCH` | `/todos/{id}/toggle` | 완료 여부 토글 |
| `GET` | `/todos/completed` | 완료된 항목만 조회 |
| `DELETE` | `/todos/{id}` | 할 일 삭제 |

> 자세한 명세는 Swagger UI 페이지 참고

---

## ⚙️ 실행 방법

```bash
# 1. 빌드 (테스트 생략)
./gradlew build -x test

# 2. 실행
java -jar build/libs/springstart-0.0.1-SNAPSHOT.jar

```
🙋‍♂️ 개발자
	•	이름: 최종민
	•	GitHub: https://github.com/hoodyboi
	•	Email: hoodyboi@naver.com
