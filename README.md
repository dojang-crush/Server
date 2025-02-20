# 도장깨기💥의 서버 레포지토리입니다
<br>

## 🌟 주요 기능
도장 깨기💥는 매일 똑같은 일상 속에서 벗어나고 싶은 사람들을 위해, 다양한 테마별로 장소를 추천하는 서비스 입니다. <br>
또한 해당 장소를 추천 받아 방문한 사람들은 사진과 글을 업로드하며 댓글을 남기며 그들만의 프라이빗한 기록을 공유할 수 있습니다.

<br>

## ✨ 기능 상세 설명
| 페이지 | 기능 |
| --- | --- |
| 로그인 페이지 | 소셜로그인 <br>회원 가입 (새 그룹 생성 혹은 기존그룹 가입) |
| 메인 페이지 - 타임라인 | 게시물 최신순으로 조회<br>게시물 좋아요<br>게시물 선택 시 상세페이지로 이동 |
| 메인 페이지 - 캘린더 | 날짜별 게시물 조회<br>게시물 선택 시 상세페이지로 이동 |
| 글 작성 및 수정 페이지 | 장소 태그 입력<br>게시글 작성<br>사진 첨부 |
| 게시물 상세 페이지 | 장소 태그, 사진, 댓글 확인 |
| 버킷 리스트 페이지 | 테마 리스트 확인<br>각 테마에 대해 추천하는 장소 리스트 확인<br>장소 선택 시 카카오 지도로 이동 |
| 위시 리스트 페이지 | 그룹원 간에 공유하는 위시 리스트 확인 가능<br>테마 별로 필터링된 위시리스트 조회<br>장소 선택 시 카카오 지도로 이동 |
| 그룹 페이지 | 그룹 인원, 그룹 사진 등 그룹 정보 확인<br>일반 설정 페이지에서 개인정보 수정 가능<br>개인 페이지에서 자신이 쓴 글 모아보기 가능 |

<br>

## 🗓️ 개발 기간
2024.07.03 ~ 2024.08.10

<br>

## 👩‍💻 팀원
|🐬문서영|🤓손민서|💜정유진|👻황혜진|
|---|---|---|---|
|![image](https://avatars.githubusercontent.com/u/105192908?v=4)|![image](https://avatars.githubusercontent.com/u/144665079?v=4)|![image](https://avatars.githubusercontent.com/u/104640725?v=4)|![image](https://avatars.githubusercontent.com/u/144921254?v=4)|
|🐬문서영|🤓손민서|💜정유진|👻황혜진|
|소셜로그인 구현| 장소 조회 API <br> 위시리스트 API |AWS 배포<br> 댓글 API |그룹 API<br>db데이터 추가<br>게시글 API <br>s3 이미지 연결



<br>

## 🪐 ERD
![](https://github.com/dojang-crush/Server/blob/93f9ea3abccbfcd93e5bc8bf2dd5b10a23979998/sws1_erd_ver2%20(2).png)

<br>

## 🚀 API 명세서
![](https://github.com/dojang-crush/Server/blob/4e5f36f272a6152f44c9a32fea0b5134dce03212/readme%20%EC%9E%91%EC%84%B1%EC%9A%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-08-06%2017.29.09.png)
![](https://github.com/dojang-crush/Server/blob/4e5f36f272a6152f44c9a32fea0b5134dce03212/readme%20%EC%9E%91%EC%84%B1%EC%9A%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-08-06%2017.29.26.png)
![](https://github.com/dojang-crush/Server/blob/4e5f36f272a6152f44c9a32fea0b5134dce03212/readme%20%EC%9E%91%EC%84%B1%EC%9A%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-08-06%2017.29.37.png)
![](https://github.com/dojang-crush/Server/blob/4e5f36f272a6152f44c9a32fea0b5134dce03212/readme%20%EC%9E%91%EC%84%B1%EC%9A%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-08-06%2017.29.45.png)
![](https://github.com/dojang-crush/Server/blob/081369e42f7053cb2b3fb4c2d56744b94bac1ee5/readme%20%EC%9E%91%EC%84%B1%EC%9A%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-08-06%2017.37.19.png)
![](https://github.com/dojang-crush/Server/blob/4e5f36f272a6152f44c9a32fea0b5134dce03212/readme%20%EC%9E%91%EC%84%B1%EC%9A%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-08-06%2017.30.21.png)
![](https://github.com/dojang-crush/Server/blob/4e5f36f272a6152f44c9a32fea0b5134dce03212/readme%20%EC%9E%91%EC%84%B1%EC%9A%A9/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-08-06%2017.30.31.png)



