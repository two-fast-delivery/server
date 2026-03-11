<img width="1244" height="2528" alt="sparta_2" src="https://github.com/user-attachments/assets/f51d2bbe-9730-4f7e-b198-63b3808b72b2" />배달 주문 관리 커머스 플랫폼

프로젝트 기간
2026.02.24 ~ 2026.03.12

팀원 역할 분담

곽찬홍	회원(User) / 인증(Auth) / 관리자 회원 관리	회원가입, 로그인, 로그아웃, 이메일 인증, 회원 정보 수정, 회원 탈퇴, 회원 리스트 조회, 회원 상세 조회, 회원 권한 변경, 권한 변경 요청 및 승인/거절
양지호	가게(Store)	가게 리스트 조회, 가게 상세 조회, 가게 등록 요청, 가게 수정, 가게 삭제 요청, 가게 등록 승인/거절
김강현	상품(Product)	상품 조회, 상품 등록, 상품 수정, 상품 삭제, 상품 옵션 등록/수정/삭제, 상품 설명 AI 생성
김혜린	주문(Order) / 장바구니	주문 생성 및 결제, 주문 취소, 주문 조회, 주문 리스트 조회, 주문 상태 변경, 장바구니 조회/삭제/닫기
이영재	리뷰(Review)	리뷰 생성, 리뷰 조회, 리뷰 수정, 리뷰 삭제, 리뷰 신고, 리뷰 신고 조회, 리뷰 신고 승인/거절
박상은	배송지(Address) / 공통	배송지 생성, 배송지 조회, 배송지 리스트 조회, 배송지 수정, 배송지 삭제, 로그 생성

서비스 구성 및 실행 방법

프로젝트 목적

1. DDD 기반 백엔드 구조 설계 경험

본 프로젝트는 Domain Driven Design(DDD) 구조를 적용하여
도메인 중심의 백엔드 아키텍처를 설계하고 구현하는 것을 목표로 합니다.
도메인별로 계층을 분리하여
Presentation
Application
Domain
Infrastructure
구조를 적용함으로써 비즈니스 로직과 기술 구현을 분리하고
확장 가능한 구조를 만드는 경험을 목표로 합니다.

2. MSA 확장을 고려한 서비스 설계

서비스를 도메인 단위로 분리하여 설계함으로써
향후 MSA(Microservice Architecture) 환경으로 전환할 수 있는 구조를 학습합니다.
각 도메인은 독립적으로 관리될 수 있도록 설계하여
User
Store
Product
Order
Review
Address
등의 도메인을 서비스 단위로 확장 가능하게 설계했습니다.

3. 실제 서비스 수준의 API 설계 경험

배달 플랫폼을 기반으로
회원 관리
가게 관리
상품 관리
주문 및 결제
리뷰 관리
등 실제 서비스에서 사용되는 기능을 RESTful API 형태로 설계하고 구현하여
실제 서비스 구조에 가까운 백엔드 시스템을 경험하는 것을 목표로 합니다.

기술 스택
**Backend**
Java 21
Spring Boot 3.x
Spring Security
Spring Data JPA
Hibernate
**Database**
PostgreSQL
Flyway
**Authentication**
JWT (Access Token / Refresh Token)
**Payment**
Toss Payments API
**AI / External API**
Gemini AI API
Toss Payments API
**Storage / Infra**
AWS S3
**Dev Tools**
Git
GitHub
Postman
Notion
Slack
**Documentation**
Swagger / OpenAPI


📌 설계 특징

📄 API 문서
http://localhost:8080/swagger-ui/index.html](http://43.200.251.132:8080/swagger-ui/index.html)

🗄️ ERD
<img width="1244" height="2528" alt="sparta_2" src="https://github.com/user-attachments/assets/c0d8860c-93b6-4931-9ae0-234d69f455f7" />

