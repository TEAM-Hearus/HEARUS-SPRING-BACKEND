![image](https://github.com/user-attachments/assets/9be2766a-7aed-4c24-a1db-16652bb706fd)
[![License](https://img.shields.io/badge/License-Apache-2.0-green.svg)](https://opensource.org/license/apache-2-0)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](http://makeapullrequest.com)

## 프로젝트 소개
Hearus는 대학교 교내 청각장애 학우 대필지원 도우미 활동에서 느낀 문제들을 풀어내기 위해 시작되었습니다. </br>
청각장애 학우들이 더 나은 환경에서 학습하고, 비장애 학우들과의 교육적 불평등을 해소할 수 있도록 하기 위해 </br>
인공지능을 활용한 실시간 음성 텍스트 변환과 문제 생성, 하이라이팅 기능을 지닌 서비스입니다.

## MVP Model
![image](https://github.com/user-attachments/assets/6b86e0fc-93fa-4fc4-a77f-1750009f4488)
- Spring Boot 기반의 확장 가능한 아키텍처
- RESTful API 설계 및 구현
- Spring Security를 활용한 보안 및 인증 시스템 통합
- WebSocket 및 Socket.IO를 이용한 실시간 음성인식 서버 구축

## Entitiy-Relationship-Diagram
![image](https://github.com/user-attachments/assets/9f5b7d43-2c31-444d-bd72-578293aa8edc)

## 주요 기능
1. **실시간 음성-텍스트 변환**: Spring Boot에서 Socketio와 Web Socket을 활용하여 실시간 음성/텍스트 통신을 구현
2. **AI 기반 중요 단어 하이라이팅**, **AI 맞춤형 문제 생성**: 기능 요청에 대해 DB를 조회하고 적절한 형태로 변형하여 FastAPI에 요청
3. **시간표 형식의 스크립트 관리**: 시간표 형태로 데이터를 관리하기 위해 MariaDB, MongoDB간의 관계를 정의하고 제어
4. **소셜 로그인 기능**: Spring Security와 OAuth 2.0을 활용하여 Google, Naver, Kakao 의 로그인 API를 통한 사용자 인증 기능 구현
## 기술 스택
| Category | Technology |
|----------|------------|
| Language | Java OpenJDK 21.0.2 |
| Framework | Spring Boot |
| Databases | MariaDB, MongoDB |
| Real-time Communication | WebSocket, Socket.IO |
| Authentication | JWT, Spring security, OAuth2.0 |
| Development Tools | Lombok |
| API Documentation | Swagger UI |
| Template Engine | Thymeleaf |

## 시작하기
### 필수 요구사항
- Java OpenJDK 21.0.2
- Gradle
- MariaDB, MongoDB

### 설치 및 실행
1. 레포지토리 clone
   ```
   git clone https://github.com/TEAM-Hearus/HEARUS-SPRING-BACKEND
   ```

2. 프로젝트 디렉토리로 이동
   ```
   cd HEARUS-SPRING-BACKEND
   ```

3. Gradle을 사용하여 프로젝트 빌드
   ```
   ./gradlew build
   ```

4. 애플리케이션 실행
   ```
   java -jar build/libs/hearus-0.0.1-SNAPSHOT.jar
   ```

## API Document
![image](https://github.com/user-attachments/assets/420525e2-92cb-4bea-8357-a431928b0115)
최신 API의 경우 Postman Document를 통해 API 문서를 확인할 수 있습니다. </br>
각 API에 대한 example 또한 확인할 수 있습니다.
```
https://documenter.getpostman.com/view/27822864/2sA2r82ix2
```

</br>

## 📄 라이선스
이 프로젝트는 Apache License 2.0 하에 배포됩니다. 
</br>
자세한 내용은 [LICENSE](https://github.com/TEAM-Hearus/HEARUS-SPRING-BACKEND/blob/main/LICENSE) 파일을 참조해주세요.

## 🤝 기여하기

Hearus의 발전에 기여해 주셔서 감사합니다!
</br>
우리는 모든 형태의 기여를 환영합니다.

1. Issue 생성: 버그 리포트, 기능 제안, 또는 질문
2. Pull Request: 코드 개선, 문서화, 또는 새로운 기능 구현
3. Code Review: 다른 기여자의 PR을 리뷰하고 피드백 제공
4. Documentation: README, 위키, 또는 코드 주석 개선

자세한 기여 가이드라인은 [CONTRIBUTING.md](https://github.com/TEAM-Hearus/.github/blob/main/CONTRIBUTING.md)를 참조해 주세요. </br>
또한 프로젝트에 대한 문의사항이 있으시다면 [ISSUE](https://github.com/TEAM-Hearus/.github/tree/main/ISSUE_TEMPLATE)를 생성해 주세요.

---

<p align="center">
  모두의 들을 권리를 위하여 Hearus가 함께하겠습니다
  </br></br>
  <img src="https://img.shields.io/badge/TEAM-Hearus-FF603D?style=for-the-badge" alt="TEAM-Hearus">
</p>
