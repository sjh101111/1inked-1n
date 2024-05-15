#  1inked-1n

## 목차
<div>
  
- [0.프로젝트 소개](#0-프로젝트-소개)
- [1.개발 환경 및 개발 일정](#1-개발-환경-및-개발-일정)
- [2.기능 명세서](#2-기능-명세서)
- [3.화면 설계서(Wireframe)](#3-화면-설계서wireframe)
- [4.구조도 - 프로젝트, 시스템](#4-구조도---프로젝트-시스템)
- [5.데이터베이스(ERD)](#5-데이터베이스erd)
- [6.API 명세서](#6-api-명세서)

</div>
-------------------------  


## 0. 프로젝트 소개
- 당신의 글로벌 커리어를 도와줄 SNS 서비스
- 글로벌 커리어를 꿈꾸는 사람들과 교류하고 정보를 받아보세요!

바야흐로 글로벌시대! 어학 연수, 유학, MBA, 해외 취업은 어떻게 준비하지? Resume는 어떻게 작성해야할지, 면접 질문은 어떻게 답해야할지.. 막막하셨나요?

저희가 도와드리겠습니다!
****

## 1. 개발 환경 및 개발 일정
### 개발 환경경
#### [프론트엔드] 
<div>
  <img alt="react" src ="https://img.shields.io/badge/React-61DAFB.svg?&style=for-the-badge&logo=React&logoColor=black"/> 
  <img alt="HTML" src ="https://img.shields.io/badge/html5-E34F26.svg?&style=for-the-badge&logo=html5&logoColor=white"/> 
  <img alt="CSS" src ="https://img.shields.io/badge/css3-1572B6.svg?&style=for-the-badge&logo=css3&logoColor=white"/> 
  <img alt="JavaScript" src ="https://img.shields.io/badge/JavaScript-F7DF1E.svg?&style=for-the-badge&logo=javascript&logoColor=black"/> 
  <img alt="tailwind" src ="https://img.shields.io/badge/tailwind-06B6D4.svg?&style=for-the-badge&logo=tailwindcss&logoColor=white"/> 
  <img alt="shadcn-ui" src ="https://img.shields.io/badge/shadcn/ui-000000.svg?&style=for-the-badge&logo=shadcnui&logoColor=white"/> 
  <img alt="Vite" src ="https://img.shields.io/badge/Vite-646CFF.svg?&style=for-the-badge&logo=Vite&logoColor=white"/>      
</div>

#### [백엔드] 
<div>
  <img alt="SpringBoot" src ="https://img.shields.io/badge/Spring Boot-6DB33F.svg?&style=for-the-badge&logo=Spring Boot&logoColor=white"/> 
  <img alt="SpringSecurity" src ="https://img.shields.io/badge/Spring Security-6DB33F.svg?&style=for-the-badge&logo=springsecurity&logoColor=white"/> 
  <img alt="Hibernate" src ="https://img.shields.io/badge/hibernate-59666C.svg?&style=for-the-badge&logo=hibernate&logoColor=white"/> 
  <img alt="JPA" src ="https://img.shields.io/badge/JPA-6DB33F.svg?&style=for-the-badge&logo=jpa&logoColor=white"/> 
  <img alt ="JAVA" src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">   
</div>

#### [DB] 
<img alt="MYSQL" src ="https://img.shields.io/badge/MySQL-4479A1.svg?&style=for-the-badge&logo=MySQL&logoColor=white"/>

#### [배포 및 클라우드]
<div>
  <img alt="AmazonEC2" src ="https://img.shields.io/badge/Amazon EC2-FF9900.svg?&style=for-the-badge&logo=Amazon EC2&logoColor=white"/> 
  <img alt="AmazonS3" src ="https://img.shields.io/badge/Amazon S3-569A31.svg?&style=for-the-badge&logo=Amazon S3&logoColor=white"/>  <img alt="AmazonRDS" src ="https://img.shields.io/badge/Amazon RDS-527FFF.svg?&style=for-the-badge&logo=Amazon RDS&logoColor=white"/>        
</div>

#### [CI/CD] 
<img alt="githubactions" src ="https://img.shields.io/badge/github actions-2088FF.svg?&style=for-the-badge&logo=githubactions&logoColor=white"/> <img alt="Amazon CodeDeploy" src ="https://img.shields.io/badge/Amazon CodeDeploy-6DB33F.svg?&style=for-the-badge&logo=Amazon CodeDeploy&logoColor=white"/>

#### [협업 도구]
<div>
    <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
    <img src="https://img.shields.io/badge/discord-5865F2?style=for-the-badge&logo=discord&logoColor=white">
</div>
<br>

### 개발일정
- 팀 빌딩 / 도메인 설정 : 4/24
- CI/CD 환경 구축 : 4/24 ~ 4/25
- 요구사항 분석 : 4/24 ~ 4/25
- 기능 명세서 작성 : 4/25 ~ 5/1
- DB 모델링 및 엔티티 설계 : 4/28 ~ 5/1
- UI/UX 설계 : 4/26 ~ 5/1
- 프론트 레이아웃 설계 : 4/26 ~ 5/1
- 구현 : 4/29 ~ 5/12
- 테스트 : 4/29 ~ 5/12
- 발표 준비 : 5/12 ~ 5/15

## 2. 기능 명세서
[1inked 1n 기능 정의서](https://www.notion.so/oreumi/277fcbe54a704fa5bdef217d0d61f6f0)

### 공통 - 헤더, 댓글, 대댓글 등 필수적이고 반복되는 기능 및 메인(랜딩)페이지
| 요구사항 | 페이지     | 구성요소                  | 입력                      | 출력                                | 설명                                                                              |
|:-----|:--------|:----------------------|:------------------------|:----------------------------------|:--------------------------------------------------------------------------------|
| 공통   | 헤더      | 로고                    |                         |                                   | 메인 homeFeedPage로 이동                                                             |
|      |         | 검색 입력                 | 키워드                     |                                   |                                                                                 |
|      |         | 검색 버튼(아이콘)            |                         |                                   | 검색 결과 서버에 요청                                                                    |
|      |         | 검색 결과                 |                         | 키워드에 해당하는 유저                      | *유저가 검색 기능(검색 입력, 버튼) 사용시 동적으로 랜더링<br/>*리스트형식                                   |
|      |         | Resume                |                         |                                   | Ai 자소서 첨삭 페이지로 이동                                                               |
|      |         | 시사 경제 정보 제공           |                         |                                   | 시사경제 정보 페이지로 이동                                                                 |
|      |         | 쪽지                    |                         |                                   | 채팅 페이지로 이동                                                                      |
|      |         | Create 버튼             |                         |                                   | 게시글 입력 활성화                                                                      |
|      |         | 게시글 내용 입력             | 게시글 내용 입력<br/>게시글 사진 첨부 |                                   | Create 버튼으로 활성화    <br/>게시글 저장 버튼으로 활성화                                         |
|      |         | 게시글 생성 버튼             |                         |                                   | Create 버튼으로 활성화  <br/>게시글 생성 요청                                                 |
|      |         | 유저 정보                 |                         | Username<br/>redirection: /mypage | 마이페이지로 이동<br/>로그인한 유저에게 활성화                                                     |
|      |         | Logout 버튼             |                         |                                   | 로그아웃 기능                                                                         |
|      | 메인 페이지  | 설명글(About us)         |                         | 서비스 설명 글                          | 타겟 고객 / 고객 니즈, 서비스 Value 등                                                      |
|      |         | Loing 버튼              |                         |                                   | 로그인 페이지로 이동                                                                     |
|      |         | Signup 버튼             |                         |                                   | 회원가입 페이지로 이동                                                                    |
|      |         | UserProfile           |                         |                                   | 사용자 사진<br/>*클릭 시 해당 유저의 userpage로 이동                                            |
|      |         | Username              |                         |                                   | 사용자 이름                                                                          |
|      |         | Identity              |                         |                                   | 회원가입 페이지로 이동                                                                    |
|      |         | 댓글                    |                         |                                   | 특정 게시물에 달린 모든 댓글 조회                                                             |
|      |         | 댓글 내용 입력              | 댓글 내용                   |                                   |                                                                                 |
|      |         | 댓글 수정 내용 입력           | 수정 댓글 내용                |                                   | 수정하는 댓글의 수정 버튼으로 활성화<br/>*기존 댓글이 입력창으로 변경/기존 댓글 내용 보전                           |                    
|      |         | 댓글 삭제 버튼              |                         | 해당 댓글 삭제                          | 댓글 삭제 요청<br/>댓글 소유자에게만 활성화                                                      |
|      |         | 댓글 수정 버튼              |                         |                                   | 댓글 수정 요청<br/>댓글 소유자에게만 활성화                                                      |
|      |         | 댓글 생성 버튼              |                         |                                   | '댓글 내용'에서 입력한 내용으로 댓글 생성 요청                                                     |
|      |         | 대댓글 버튼                |                         |                                   | 대댓글 내용 입력 활성화                                                                   |
|      |         | UserProfile           |                         | UserProfile                       | 사용자 사진<br/>*클릭 시 해당 유저의 userpage로 이동                                            |
|      |         | UserName              |                         | UserName                          | 사용자 이름                                                                          |
|      |         | Identity              |                         | Idnetity                          | 신분: 학교, 직장 등                                                                    |
|      |         | 대댓글                   |                         | 특정 게시글에 달린 모든 대댓글                 | 특정 댓글에 달린 모든 대댓글 조회                                                             |
|      |         | 대댓글 내용 입력             | 대댓글 내용                  |                                   | *댓글의 대댓글 버튼으로 활성화                                                               |
|      |         | 대댓글 수정 내용 입력          | 수정 대댓글 내용               |                                   | 수정하는 대댓글의 수정 버튼으로 활성화<br/>*기존 대댓글이 입력창으로 변경/기존 대댓글 내용 보전                        |
|      |         | 대댓글 삭제 버튼             |                         | 대댓글 삭제                            | 대댓글 삭제 서버에 요청<br/>*대댓글 소유자에게만 활성화                                               |
|      |         | 대댓글 수정 버튼             |                         |                                   | 대댓글 수정 서버에 요청<br/>*대댓글 소유자 에게만 활성화                                              |
|      |         | 대댓글 셍성 버튼             |                         |                                   | 대댓글 생성 서버에 요청<br/>*댓글의 대댓글 버튼으로 활성화                                             |
<br>

### 회원 관리 - 로그인, 회원가입, 비밀번호 찾기, 비밀번호 변경하기, 회원 탈퇴, 마이페이지
| 요구사항      | 페이지         | 구성요소       | 입력 | 출력                  | 설명                |
|:----------|:------------|:-----------|:---|:--------------------|:------------------|
| 회원관리 | 로그인     | Email 입력              | Email                   |                                   |                                                                                 |
|      |         | Password 입력           | Password                |                                   |                                                                                 |
|      |         | Login 버튼              |                         |                                   | 서버에 로그인 요청<br/> Email, Password값 DB와 불일치<br/>-> alert: '이메일 또는 비밀번호가 올바르지 않습니다' |
|      |         | Signup 버튼             |                         |                                   | 회원가입 페이지로 이동                                                                    |
|      |         | 비밀번호 찾기               |                         |                                   | 비밀번호 찾기 페이지로 이동                                                                 |
|      | 마이페이지   | UserProfile           |                         | UserProfile                       | 로그인한 사용자 사진                                                                     |
|      |         | UserProfile 수정 내용 입력  | UserProfile 수정 내용       |                                   | edit버튼으로 활성화<br/>*png파일                                                         |
|      |         | UserName              |                         | UserName                          | 사용자 이름                                                                          |
|      |         | Identity              |                         | Identity                          | 사용자 신분<br/>*학교, 직장 등                                                            |
|      |         | Identity 수정 내용 입력     | Identity 수정 내용          |                                   | edit버튼으로 활성화<br/>Identity -> Identity 수정 내용 입력으로 활성화<br/>기존 내용 보전               |
|      |         | Location              |                         | Location                          | 활동 지역                                                                           |
|      |         | Description           |                         | Description                       | 사용자 소개                                                                          |
|      |         | Description 수정 내용 입력  |                         |                                   | edit 버튼으로 활성화<br/>Description -> Description 수정 내용으로 활성화<br/>기존 내용 보전           |
|      |         | edit 버튼               |                         |                                   | save버튼 활성화                                                                      |
|      |         | save 버튼               |                         |                                   | edit버튼으로 활성화<br/>유저정보 수정 요청<br/>수정 완료 후 기존 내용 -> 수정 내용으로 변경 및 save버튼 다시 활성화     |
|      |         | 회원탈퇴버튼                |                         |                                   | 회원 탈퇴 페이지로 이동                                                                   |
|      |         | 비밀번호 변경하기             |                         |                                   | 비밀번호 변경하기 페이지로 이동                                                               |
|      |         | 게시글 조회 버튼             |                         |                                   | 내가 작성한 게시글과 해당하는 댓글 조회 요청                                                       |
|      |         | 게시글                   |                         | 내가 작성한 게시글                        |                                                                                 |
|      |         | Follow/Followee 조회 버튼 |                         |                                   | 나의 팔로우/팔로워 조회 요청                                                                |
|      |         | Follow/Followee       |                         | 팔로우/팔로워                           | 해당 유저의 userpage로 이동                                                             |
|      |         | My Resume 조회 버튼       |                         |                                   | 내가 저장한 resume 조회 요청                                                             |
|      |         | My Resume             |                         | 내가 저장한 Resume                     |                                                                                 |
|      | 비밀번호 찾기 | Email 입력              | Email                   |                                   | db와 일치하지 않으면<br/>: alert'입력 값이 잘못되었습니다'                                         |
|      |         | 비밀번호 찾기 질문(리스트)       | 비밀번호 찾기 질문              |                                   | db와 일치하지 않으면<br/>: alert'입력 값이 잘못되었습니다'                                         |
|      |         | 비밀번호 찾기 질문 답변         | 비밀 번호 찾기 질문 답변          |                                   |                                                                                 |
|      |         | NewPassword           | 새로운 비밀번호                |                                   |                                                                                 |
|      |         | 비밀번호 찾기 버튼            |                         |                                   | Email, 비밀번호 찾기 키워드가 db와 일치하지 않을 시<br/> db와 일치하지 않으면<br/>: alert'입력 값이 잘못되었습니다'  |
|      | 비밀번호 변경 | NewPassword           | 새로운 비밀번호                |                                   |                                                                                 |
|      |         | VerifyPassword        | 새로운 비밀반호                |                                   |                                                                                 |
|      |         | 변경 버튼                 |                         |                                   | 비밀번호 입력, 확인 값 불일치<br/>-> alert:'입력 값이 올바르지 않습니다.'                               |
|      | 회원 탈퇴   | password입력            | 기존 비밀번호                 |                                   |                                                                                 |
|      |         | 탈퇴 버튼                 |                         |                                   | 서버에 회원 탈퇴 요청                                                                    |
|      |         | 돌아가기 버튼               |                         |                                   | mypage로 이동                                                                      |
|      | 회원가입    | Username 입력           | Username                |                                   | Unique value<br/>중복 검사                                                          |
|      |         | Email 입력              | Email                   |                                   | Unique value<br/>중복 검사                                                          |
|      |         | password 입력           | Password                |                                   |                                                                                 |
|      |         | 비밀번호 찾기 질문(리스트)       | 비밀번호 찾기 질문              |                                   |                                                                                 |
|      |         | 비밀번호 찾기 질문 답변         | 비밀번호 찾기 질문 답변           |                                   |                                                                                 |
|      |         | 회원가입 버튼               |                         |                                   | 입력값 중복 시 <br/>-> alert:'입력값이 중복되었습니다'<br/>                                      |
|      |         | 이미 계정이 있으신가요?(버튼)     |                         |                                   | 로그인 페이지로 이동                                                                     |
<br>

### 외부 API 연동 - 네이버 뉴스 API 연동으로 인한 정보 제공 페이지
| 요구사항      | 페이지         | 구성요소       | 입력 | 출력                  | 설명                |
|:----------|:------------|:-----------|:---|:--------------------|:------------------|   
| 외부 API 연동 | 시사 경제 정보 제공 | 뉴스 콘텐츠     |    | Naver News Api Data | Naver News Api 연동 |
|           |             | 날짜순 정렬 버튼  |    | 날짜순으로 정렬된 뉴스 데이터    | 날짜순으로 정렬          |
|           |             | 키워드순 정렬 버튼 |    | 키워드별로 정렬된 뉴스 데이터    | 키워드별로 정렬          |
<br>

### 앨런 AI 연동 - AI가 유저의 Resume를 분석해주는 페이지
| 요구사항     | 페이지         | 구성요소       | 입력 | 출력                  | 설명                |
|:---------|:------------|:-----------|:---|:--------------------|:------------------|
| 앨런 AI 연동 | Resume Review | Resume 내용 입력 | Resume 내용 | | |
|          | | 첨삭 요청 버튼 | | | 서버에 resume 분석 요청 |
|          | | 앨런 AI 응답 | | Resume 내용에 대한 AI 분석값 | |
|          | | 저장 버튼 | | | 서버에 AI가 분석해준 Resume 저장 요청 |
<br>

### RestAPI(CRUD) - 내가 팔로우한 사람들의 게시글을 볼 수 있는 팔로우 피드 페이지, 쪽지를 주고받는 쪽지 페이지
| 요구사항          | 페이지         | 구성요소       | 입력 | 출력                  | 설명                |
|:--------------|:------------|:-----------|:---|:--------------------|:------------------|
| RestAPI(CRUD) | 팔로우 피드 | 게시글 | | 팔로우한 사람들의 게시글 | 유저정보, 댓글, 작성 날짜, 수정날짜 포함 |
|               | 유저페이지 | UserProfile | | UserProfile(사진)                   | 사용자 사진 |
|               | | UserName | | UserName | 사용자 이름 |
|               | | Identity | | Identity | 사용자 신분 |   
|               | | Description | | Description | 사용자 소개 |   
|               | | Location | | Location | |   
|               | | Follow 버튼 | | | 서버에 팔로우 요청 |   
|               | | 쪽지 보내기 버튼 | | | 쪽지 내용 입력 활성화 |   
|               | | 쪽지 내용 입력 | 쪽지 내용 | | 쪽지 보내기 버튼으로 활성화 |   
|               | | 전송 버튼 | | | 서버에 쪽지 생성 요청  <br/>상대방 쪽지함으로 쪽지 전송 |   
|               | | 게시글 조회 버튼 | | | 해당 유저가 작성한 모든 게시글 조회 요청 |   
|               | | 게시글 | | 해당 유저가 작성한 모든 게시글 | |   
|               | | Follow/Followee 조회 버튼 | | | 해당 유저의 Follow/Followee 조회 요청 |   
|               | | Follow/Followee | | 해당 유저의 Follow/Followee | 해당 유저가 저장한 Resume 조회 요청 |   
|               | | Resume 조회 버튼 | | | 해당 유저가 저장한 Resume 조회 요청 |   
|               | | Resume | | 해당 유저가 저장한 Resume | |   
|               | 쪽지 | 쪽지함 | | 상대 유저와의 쪽지 목록 조회 | |   
|               | | 쪽지 상세페이지 | | 상대유저와 주고받은 모든 쪽지 내용 | 쪽지함에서 클릭시 활성화 |   
|               | | 쪽지 보내기 버튼 | | | 쪽지 상세페이지에서만 활성화 |   
|               | | 삭제 버튼 | | | 상대방과의 쪽지 삭제 요청 |   
|               | | 쪽지 내용 입력 | 쪽지 내용 | | 쪽지 보내 버튼에 의해 활성화 |   
|               | | 쪽지 전송 버튼 | | | 서버에 쪽지 생성 요청 |


## 3. 화면 설계서(Wireframe)
[1inkend 1n 화면 설계(figma)](https://www.figma.com/design/4Ypf3cLJBIoLrJ3pAu1YRU/Final-Project?node-id=0-1&t=TKggU5nXcle1n5yc-0)

|||
|--------------------|---------------------|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/a9f09f49-c3b4-46ce-9542-a5dcae78f898"> <br/> 메인 페이지|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/3ce230b0-1707-4f48-ad35-5f9e47d781c9"> <br/> 로그인 화면|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/1c88a47b-539a-4242-b08c-86cfc83143b4"> <br/> 회원 가입 화면|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/a8868c5b-fa04-4481-a43d-811754426447"> <br/> 비밀번호 찾기 화면|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/0b180d0d-e8f1-4915-905d-d841b1cb2fe9"> <br/> 비밀번호 변경 화면|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/ada1d08a-3401-4f99-a3fe-51cfc7514894"> <br/> 회원 탈퇴 화면|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/01bf3798-c195-44f1-b89d-fd3b7d39abe0"> <br/> 게시글 메인 화면 |<img src="https://github.com/Garodden/1inked-1n/assets/143177939/e4fbd005-4b37-4428-a3c5-2e34cf2ca0db"> <br/> 게시글 작성 화면|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/88ee9e28-9bc1-4e17-bc56-a93dc4911aa0"> <br/> 게시글 수정 화면|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/1b3b549f-7f1b-45cf-adc5-bf6dd57293a6"> <br/> 쪽지 메인 화면|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/df3dbcc1-f30d-4167-b1cf-bb30c9ba1e4a"> <br/> 쪽지 보내기 화면 |<img src="https://github.com/Garodden/1inked-1n/assets/143177939/782ac7c3-cd4b-4c92-a497-6b27064e3f20"> <br/> 이력서 첨삭 화면|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/73ec97ce-4106-4d4c-acc8-fac6f2d7103e"> <br/> 뉴스 정보 화면 |<img src="https://github.com/Garodden/1inked-1n/assets/143177939/e7101420-5cff-476f-a13e-03cde129491e"> <br/> 마이 페이지 화면|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/0ca6ed8d-08c4-4293-8ac1-eb161c1dbaf7"> <br/>마이 페이지 아티클 조회 화면 |<img src="https://github.com/Garodden/1inked-1n/assets/143177939/4858e843-ae7a-48ff-9965-0e07a1809956"> <br/> 마이 페이지 댓글 조회 화면|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/adbe6c47-2231-4457-b535-814ace495f2a"> <br/> 마이 페이지 팔로우 조회 화면|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/7717eedb-04fc-4ddf-9345-0057c9150b96"> <br/> 마이 페이지 이력서 조회 화면|
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/f45f5916-9d6e-4d32-a806-f8d41891ffac"> <br/> 마이 페이지 이력서 상세 화면 |<img src="https://github.com/Garodden/1inked-1n/assets/143177939/0ece0d58-6af3-4a32-a564-28ff4dc01def"> <br/> 유저 페이지 화면 |
|<img src="https://github.com/Garodden/1inked-1n/assets/143177939/35700ad4-4c2b-4a24-9e14-b8cff4ff238a"> <br/> 공통 헤더 ||

## 4. 구조도 - 프로젝트, 시스템
### 프로젝트 구조도

<pre>
<code>
.
├─front # Frontend 관련 디렉토리
│  ├─.vite
│  │  └─deps
│  ├─public
│  └─src
│      ├─assets
│      ├─components
│      │  ├─Layout
│      │  ├─svg
│      │  └─ui
│      ├─lib
│      ├─routes
│      └─utils
├─main
│  ├─java
│  │  ├─com
│  │  │  └─example
│  │  │      └─oneinkedoneproject
│  │  │          ├─AllenAiApi # AllenApi 관련 클래스 패키지
│  │  │          ├─config # Swagger, Spring Security 관련 클래스 패키지
│  │  │          ├─controller # 컨트롤러 클래스 패키지
│  │  │          │  ├─AllenAi
│  │  │          │  ├─Article
│  │  │          │  ├─chat
│  │  │          │  ├─comment
│  │  │          │  ├─externalApi
│  │  │          │  ├─follow
│  │  │          │  ├─Image
│  │  │          │  ├─password
│  │  │          │  ├─resume
│  │  │          │  └─user
│  │  │          ├─domain # 도메인 클래스 패키지
│  │  │          ├─dto # dto 클래스 패키지
│  │  │          │  ├─article
│  │  │          │  ├─auth
│  │  │          │  ├─chat
│  │  │          │  ├─externalApi
│  │  │          │  ├─follow
│  │  │          │  ├─image
│  │  │          │  ├─resume
│  │  │          │  └─user
│  │  │          ├─filter # JWT 관련 클래스 패키지
│  │  │          ├─repository # 레포지토리 클래스 패키지
│  │  │          │  ├─article
│  │  │          │  ├─chat
│  │  │          │  ├─comment
│  │  │          │  ├─follow
│  │  │          │  ├─image
│  │  │          │  ├─password
│  │  │          │  ├─resume
│  │  │          │  └─user
│  │  │          ├─service # 서비스 클래스 패키지
│  │  │          │  ├─AllenAi
│  │  │          │  ├─article
│  │  │          │  ├─auth
│  │  │          │  ├─chat
│  │  │          │  ├─comment
│  │  │          │  ├─externalApi
│  │  │          │  ├─follow
│  │  │          │  ├─image
│  │  │          │  ├─password
│  │  │          │  ├─resume
│  │  │          │  └─user
│  │  │          └─utils #편의 기능(ex: UUID 추출) 관련 클래스 패키지
│  │  └─util #UUID 생성 관련 클래스 패키지
│  └─resources # application.yml 등 어플리케이션 관련 설정 클래스들
└─test
    └─java
        └─com
            └─example
                └─oneinkedoneproject
                    ├─AllenAi # Allen Api 테스트 클래스 패키지
                    ├─controller # 컨트롤러 테스트 클래스 패키지
                    │  └─follow
                    ├─filter # jwt 테스트 클래스 패키지
                    ├─repository # 레포지토리 테스트 클래스 패키지
                    │  ├─article
                    │  ├─chat
                    │  ├─comment
                    │  ├─follow
                    │  ├─image
                    │  ├─password
                    │  ├─resume
                    │  └─user
                    ├─service # 서비스 테스트 클래스 패키지
                    │  ├─chat
                    │  ├─follow
                    │  ├─password
                    │  ├─resume
                    │  └─user
                    └─utils # 편의기능 테스트 클래스 패키지
#appspec.yml : # CI/CD 관련 설정 클래스
#build.gradle : # 의존성과 같은 환경설정 클래스 (gradle로 작성)
</code>
</pre>


###  시스템 구성도

<p align="center">
  <img src="https://github.com/Garodden/1inked-1n/assets/143177939/2d6b46ae-f2df-4dea-8f8a-cd44a11cbb2f">
</p>

## 5. 데이터베이스(ERD)

<p align="center">
  <img src="https://github.com/Garodden/1inked-1n/assets/143177939/8ae0142c-98c3-451e-b438-9ae721266310">
</p>

## 6. API 명세서

### 공통 - 헤더, 댓글, 대댓글 등 필수적이고 반복되는 기능 및 메인(랜딩)페이지
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/515fb7bc-1f75-4ad1-8a8a-460edbcf87ef">
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/17c41459-f8ad-4a4f-9975-d2991beb1279">

### 회원 관리 - 로그인, 회원가입, 비밀번호 찾기, 비밀번호 변경하기, 회원 탈퇴, 마이페이지
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/c133f163-7d27-4f5e-8322-062f9131770e">
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/6d46ac86-627c-4abf-813a-703b5907b894">
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/41935ce9-a42c-41e2-bc01-a898a04ec210">

### 외부 API 연동 - 네이버 뉴스 API 연동으로 인한 정보 제공 페이지
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/087ccbe4-8dbc-4a4f-97b8-18eff3d74e97">

### 앨런 AI 연동 - AI가 유저의 Resume를 분석해주는 페이지
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/616d1a17-e2f2-45b6-85e3-30228fb82adb">

### RestAPI(CRUD) - 내가 팔로우한 사람들의 게시글을 볼 수 있는 팔로우 피드 페이지, 쪽지를 주고받는 쪽지 페이지
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/a591d279-7a18-41fb-82b8-932efd259a34">
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/905d8ddf-74d3-4e9c-9f92-7d421deac2f3">
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/2f62c928-bb45-49ce-bee7-40c4f93e44ef">
<img src="https://github.com/Garodden/1inked-1n/assets/143177939/c60d9eac-5ce5-4da1-8bd4-f8c4968d23e8">


## 기타 -📐 코딩 컨벤션

### 프론트엔드
[프론트 코딩컨벤션](https://github.com/Garodden/1inked-1n/wiki/Front-%EC%BD%94%EB%94%A9-%EC%BB%A8%EB%B2%A4%EC%85%98)

### 백엔드
[백엔드 코딩컨벤션](https://github.com/Garodden/1inked-1n/wiki/Backend-%EC%BD%94%EB%94%A9%EC%BB%A8%EB%B2%A4%EC%85%98)

### 기타
[기타 코딩 컨벤션](https://github.com/Garodden/1inked-1n/wiki/Other-%EC%BD%94%EB%94%A9%EC%BB%A8%EB%B2%A4%EC%85%98)

