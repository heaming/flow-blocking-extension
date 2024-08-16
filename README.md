# 📂 파일 확장자 차단

파일 첨부 시 문제가 될 수 있는 확장자를 차단하기 위한 시스템

<br/>

## **요구사항**

- **고정 확장자**
    - 차단을 자주 하는 확장자 리스트
    - default는 unchecked, check 유무 저장
    - 저장 후에도 커스텀 확장자에는 표현되지 않음
- **커스텀 확장자**
    - 최대 입력 길이 20자
    - 최대 200개 추가
    - `추가`버튼과 `x`버튼을 통해 저장

<br/>

## **Architecture**
### Tech Stack

- Open JDK 17, Spring Boot 3.3.2, Spring JPA, MySql8, Docker
- Javascript ES6, Vue3, vite, Bootstrap-vue-next
- AWS, Git

<br/>

### Spring MVC - REST API

- Rest API를 사용한 비동기 통신 방식
- Example) /api/v1/blocking-extensions/{id}

![image](https://github.com/user-attachments/assets/a428c495-dbc8-4db7-a143-ea0c0edf2d51)

<br/>

### DB 구조

![image](https://github.com/user-attachments/assets/3a0dedb2-8b36-48a0-8be4-2a2deb132f51)

<br/>

## 추가 고려사항

### 커스텀 확장자 중복 체크

1. 입력 확장자가 이미 저장된 확장자인 경우
    1. 고정 확장자인 경우 → return
    2. 커스텀 확장자인 경우 → return
2. 입력 확장자가 저장되지 않은 경우
    1. 고정 확장자인 경우 → 입력 확장자와 일치한 고정 확장자 check
    2. 커스텀 확장자인 경우 → 신규 등록

<br/>

### `@RestControllerAdvice`를 활용한 공통 에러 코드 관리
![image](https://github.com/user-attachments/assets/86c6cc2a-b0dc-4bb6-8a71-c1024b076387)

<br/>

### 쿠키를 토큰으로 활용

User Service가 없는 환경이므로, 초기 진입 시 SessionId를 쿠키에 저장에 token으로 활용

![image](https://github.com/user-attachments/assets/ede04f60-034f-409a-af1f-d5a062a537f7)
