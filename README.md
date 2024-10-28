![image](https://github.com/user-attachments/assets/9be2766a-7aed-4c24-a1db-16652bb706fd)
[![License](https://img.shields.io/badge/License-Apache%202.0-green)](https://opensource.org/license/apache-2-0)
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

## 📂 API Document
프로젝트의 API 명세는 아래 링크에서 확인하실 수 있습니다.
[HEARUS-SPRING-BACKEND/wiki](https://github.com/TEAM-Hearus/HEARUS-SPRING-BACKEND/wiki)

## 📄 라이선스
이 프로젝트는 Apache License 2.0 하에 배포됩니다. 
</br>
자세한 내용은 [LICENSE](https://github.com/TEAM-Hearus/HEARUS-SPRING-BACKEND/blob/main/LICENSE) 파일을 참조해주세요.

---

<p align="center">
  모두의 들을 권리를 위하여 Hearus가 함께하겠습니다
  </br></br>
  <img src="https://img.shields.io/badge/TEAM-Hearus-FF603D?style=for-the-badge" alt="TEAM-Hearus">
</p>
