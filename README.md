# Two Fast Delivery

### 배달 주문 관리 백엔드 플랫폼

광화문 지역 음식점의 주문을 온라인으로 관리하고
주문 상태를 체계적으로 운영할 수 있도록 설계된 **배달 주문 관리 플랫폼 백엔드 프로젝트**

&nbsp;
# 🧭 Table of Contents

- [📌 프로젝트 개요](#-project-overview)
- [🧭 서비스 흐름](#-service-flow)
- [🏗 시스템 아키텍처](#-system-architecture)
- [🛠 기술 스택](#-tech-stack)
- [🎯 핵심 기능](#-core-features)
- [🏗 아키텍처 설계](#-architecture-design)
- [💡 트러블슈팅](#-troubleshooting)
- [👥 팀원 역할](#-team-members)
- [📄 API 문서](#-api-documentation)
- [⚙️ 실행 방법](#️-run-project)
- [🗄 ERD](#-erd)
- [📈 프로젝트를 통해 배운 점](#-what-i-learned)

&nbsp;
# 📌 Project Overview

| 항목    | 내용                                     |
| ----- | -------------------------------------- |
| 프로젝트명 | Two Fast Delivery                      |
| 개발 기간 | 2026.02.24 ~ 2026.03.12                |
| 개발 인원 | 6명                                     |
| 목표    | DDD 기반 구조 설계 및 실제 서비스 수준의 주문 관리 시스템 구현 |

&nbsp;


# 🧭 Service Flow

```
로그인
   ↓
상품 조회
   ↓
장바구니 담기
   ↓
주문 생성
   ↓
결제 요청
   ↓
결제 승인
   ↓
주문 상태 관리
   ↓
리뷰 작성
```
&nbsp;


# 🏗 System Architecture

<img width="1244" height="1024" alt="Image" src="https://github.com/user-attachments/assets/7d1b251b-6632-4408-a1f6-a65230a34d5a" />

외부 서비스 연동

* PostgreSQL : 데이터 저장
* AWS S3 : 이미지 저장
* Toss Payments : 결제 처리
* Google Gemini API : 상품 설명 생성
  
&nbsp;


# 🛠 Tech Stack

### Backend

* Java 21
* Spring Boot 3.x
* Spring Security
* Spring Data JPA
* Hibernate

### Database

* PostgreSQL
* Flyway

### Authentication

* JWT (Access / Refresh Token)

### External API

* Toss Payments API
* Gemini AI API

### Infra

* AWS S3

### Collaboration

* Git
* GitHub
* Notion
* Slack

### Documentation

* Swagger

  &nbsp;



# 🎯 Core Features

| 기능       | 설명                 |
| -------- | ------------------ |
| 회원 인증    | JWT 기반 인증 및 권한 관리  |
| 가게 관리    | 가게 등록 요청 / 수정 / 삭제 |
| 상품 관리    | 상품 CRUD / 옵션 관리    |
| 장바구니     | 상품 담기 / 조회 / 삭제    |
| 주문 관리    | 주문 생성 / 조회 / 취소    |
| 결제 시스템   | 결제 요청 / 승인 / 취소    |
| 주문 상태 관리 | 주문 상태 흐름 관리        |
| 리뷰 시스템   | 리뷰 작성 / 수정 / 삭제    |
| AI 기능    | Gemini 기반 상품 설명 생성 |

&nbsp;



# 🏗 Architecture Design

본 프로젝트는 **DDD 기반 구조 + 4계층 아키텍처**를 적용했습니다.

### 4 Layer Architecture

```
Presentation Layer
Controller / API

Application Layer
Service / UseCase

Domain Layer
Entity / Domain Logic

Infrastructure Layer
Repository / External API
```

### Domain Structure

```
domain
 ├ user
 ├ store
 ├ product
 ├ order
 ├ review
 ├ address
```

### 설계 목표

* 도메인 중심 설계
* 비즈니스 로직과 기술 구현 분리
* MSA 확장 가능 구조

&nbsp;

# 💡 Troubleshooting


## 1️⃣ JWT Refresh Token 관리

### 문제

Access Token 만료 시 재로그인 필요

### 해결

```
Access Token
Refresh Token
```

구조 도입

### 결과

보안 + 사용자 편의성 확보

---

## 2️⃣ 주문 금액 위변조 방지

### 문제

클라이언트 요청값을 그대로 사용할 경우
주문 금액을 조작할 수 있는 문제가 발생합니다.

### 해결

주문 생성 시

```
클라이언트 요청 금액 무시
↓
DB 상품 가격 조회
↓
서버에서 주문 금액 재계산
```

### 결과

결제 금액 위변조 방지

---

## 3️⃣ 주문 시점 가격 스냅샷 저장

### 문제

상품 가격 변경 시
과거 주문 금액이 변하는 문제가 발생합니다.

### 해결

주문 생성 시

```
OrderItem.price
```

에 **주문 시점 가격 저장**

### 결과

과거 주문 데이터 금액 불변성 유지

---

## 4️⃣ 주문 상태 머신 설계

### 문제

잘못된 주문 상태 변경 가능

### 해결

주문 상태 흐름 제한

```
주문 요청
→ 조리 중
→ 조리 완료
→ 배달 중
→ 배달 완료
```

### 결과

주문 상태 관리 안정성 확보

&nbsp;


# 👥 Team Members

| 이름  | 담당 도메인                 | 주요 기능                             |
| --- | ---------------------- | --------------------------------- |
| 곽찬홍 | User / Auth            | 회원가입, 로그인, 권한 관리                  |
| 양지호 | Store                  | 가게 등록 요청 / 수정 / 삭제                |
| 김강현 | Product                | 상품 CRUD / 옵션 / AI 설명              |
| 김혜린 | Order / Cart / Payment | 주문 생성 / 조회 / 취소 / 결제 요청 / 승인 / 취소 |
| 이영재 | Review                 | 리뷰 작성 / 신고                        |
| 박상은 | Address                | 배송지 CRUD                          |

&nbsp;

# 📄 API Documentation

Swagger

```
http://43.200.251.132:8080/swagger-ui/index.html
```

&nbsp;

# ⚙️ Run Project

### 환경 변수

```
DB_URL=
DB_USER=
DB_PASSWORD=

JSON_WEB_TOKEN_SECRET=
JSON_WEB_TOKEN_VALID_TIME=3600

GEMINI_API_KEY=
```
### 실행

```
./gradlew bootRun
```

&nbsp;

# 🗄 ERD

<img width="1244" height="2528" alt="sparta_2" src="https://github.com/user-attachments/assets/c0d8860c-93b6-4931-9ae0-234d69f455f7" />

&nbsp;

# 📈 What I Learned

* DDD 기반 구조 설계를 통해 도메인 중심 설계 경험
* 권한 관리 / 가게 / 상품 / 주문 / 결제 / 리뷰 도메인 간 협력 구조 설계
* 서버 검증 기반의 방어적 설계 경험
