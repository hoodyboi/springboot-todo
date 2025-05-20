# 🗂 Spring Boot To-Do 앱

Spring Boot + JPA + MySQL을 활용한 할 일 관리 백엔드 API입니다.

---

## ✅ 주요 기능
- ✅ 할 일 등록 (POST /todos)
- 📋 전체 목록 조회 (GET /todos)
- 🔄 완료 여부 수정 (PUT /todos/{id})
- ❌ 삭제 (DELETE /todos/{id})
- ↔️ 상태 토글 (PUT /todos/{id}/toggle)
- 🔍 키워드 검색 (GET /todos/search?keyword=...)
- 📌 정렬 (GET /todos/sorted?by=title|createdAt)
- 📄 페이징 처리 (GET /todos/page?page=0&size=3)
- 📊 완료/미완료 조건 조회
- 🧪 Swagger 연동 (`http://localhost:8080/swagger-ui/index.html`)

---

## 🛠 기술 스택
- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- MySQL
- Lombok
- Swagger (springdoc-openapi v2.1.0)
- Gradle

---

## 📁 프로젝트 구조
springstart/                                
└─ src/                                        
├─ main/                                        
│   └─ java/com/example/springstart/                                        
│       ├─ controller                                        
│       ├─ domain                                        
│       ├─ dto                                        
│       ├─ service                                        
│       ├─ repository                                        
│       └─ exception                                        
└─ resources/                                        
└─ application.properties                                        
                                        
---

## 🚧 개발 예정 기능
- [ ] ✅ JWT 로그인 인증
- [ ] ✅ 사용자별 할 일 관리
- [ ] ✅ 테스트 코드 작성
- [ ] ✅ Render 또는 AWS EC2 배포

---

## 🙋‍♂️ 개발자

- GitHub: [hoodyboi](https://github.com/hoodyboi)
- 프로젝트명: `springboot-todo`
