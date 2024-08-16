# 📂 파일 확장자 차단

파일 첨부 시 문제가 될 수 있는 확장자를 차단하기 위한 시스템

## **요구사항**

- **고정 확장자**
    - 차단을 자주 하는 확장자 리스트
    - default는 unchecked, check 유무 저장
    - 저장 후에도 커스텀 확장자에는 표현되지 않음
- **커스텀 확장자**
    - 최대 입력 길이 20자
    - 최대 200개 추가
    - `추가`버튼과 `x`버튼을 통해 저장

## **Architecture**

### Tech Stack

- Open JDK 17, Spring Boot 3.3.2, Spring JPA, MySql8, Docker
- Javascript ES6, Vue3, vite, Bootstrap-vue-next
- AWS, Git

### Spring MVC - REST API

- Rest API를 사용한 비동기 통신 방식
- Example) /api/v1/blocking-extensions/{id}

  ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/cc7edd20-70ec-4af7-b4ef-7323fe8266da/309f3f45-4359-4e46-9c50-4791b57865cc/image.png)

### DB 구조

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/cc7edd20-70ec-4af7-b4ef-7323fe8266da/2a7ae69d-100a-495f-a7ff-ca10bae4b65e/image.png)

## 추가 고려사항

### 커스텀 확장자 중복 체크

1. 입력 확장자가 이미 저장된 확장자인 경우
    1. 고정 확장자인 경우 → return
    2. 커스텀 확장자인 경우 → return
2. 입력 확장자가 저장되지 않은 경우
    1. 고정 확장자인 경우 → 입력 확장자와 일치한 고정 확장자 check
    2. 커스텀 확장자인 경우 → 신규 등록

### `@RestControllerAdvice`를 활용한 공통 에러 코드 관리

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/cc7edd20-70ec-4af7-b4ef-7323fe8266da/6589a9bd-a820-45bf-b375-6f08307d5772/image.png)

### 쿠키를 토큰으로 활용

User Service가 없는 환경이므로, 초기 진입 시 SessionId를 쿠키에 저장에 token으로 활용

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/cc7edd20-70ec-4af7-b4ef-7323fe8266da/7726d169-628e-4f63-abe8-7b88044142ca/image.png)