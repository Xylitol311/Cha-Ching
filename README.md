# Cha-Ching

## E-commerce 프로젝트
프로젝트 주제를 이커머스(eCommerce)로 정했고 많은 사람들이 자유롭게 거래를 할 수 있는 플랫폼을 제작하는 것이 목표이기에 개발하려고 하는 ‘챠칭!’을 이용하는 유저들이
‘챠칭’의 사전적 의미처럼 금전적으로 만족감을 느끼며 신나는 거래가 될 수 있었으면 하는 의미에서 프로젝트명을 ‘챠칭’으로 정하게 되었다.

백엔드 개발을 중점으로 이커머스 시스템의 중요한 내용을 최대한 개발하고 관리할 수 있도록 구현하며 관리자와 사용자, 판매자로 권한을 나누어 유저 권한에 따른 사용에 차이를 두었다.

# 프로젝트 기능 및 설계

### 1 . 회원가입 기능(판매자, 구매자, 관리자)

- 필수 입력 정보
    - [아이디] (중복불가)
    - 이름
    - 핸드폰 번호
    - 비밀번호 - 비밀번호는 영문자, 특수문자, 숫자가 최소 한 글자 이상 포함된 8~12 사이의 글자여야 한다.
    - 이메일(google SMTP로 인증절차)

- 선택 입력 정보 - 생년월일

### 2. 로그인, 로그아웃 기능

- security와 jwt 토큰과 Redis를 이용한 로그인 로그아웃 구현

### 3. 개인정보 관리

#### 3-1. 회원 정보 관리

- 회원 정보 수정
  - 휴대폰 번호, 이름, 비밀번호, 생년월일 수정
    - 휴대폰 번호 중복 체크
    - 비밀번호 규칙 체크
- 아이디, 비밀번호 찾기 기능
  - 이메일로 전송된 링크를 통해 아이디 확인 및 비밀번호 변경 가능

#### 3-2. 배송지 CRUD 기능(구매자)

- 배송지 CRUD
    - 주소, 상세주소, 수신자 번호, 배송지 이름, 대표배송지
    - 카카오 주소 API 검색 기능 추가

#### 3-3. 배송지 정보

- 고객, 판매자 모두 개인정보 수정 가능
    - 핸드폰 번호 수정
    - 닉네임 수정
    - 비밀번호 수정(비밀번호 입력 받고 확인시 변경 가능)
    - 이름, 이메일은 수정 불가

- 아이디 찾기 절차
    - 가입했던 핸드폰번호와 이름 작성
    - 인증완료 후 아이디(이메일) 확인 가능

- 비밀번호 찾기 절차
    - 가입했던 이름과 아이디(이메일) 작성
    - 작성후 이메일로 인증코드 발송(가입됬던 이메일과 이름이 일치하지 않으면 에러)
    - 인증완료 후 비밀번호 재설정 가능

### 4. 1:1 문의

- 1:1 문의 CRUD
    - 문의는 제목, 내용, 유형, 날짜 정보가 포함

- 관리자 문의 답글 CRUD
    - 제목, 내용 필수 입력

- 관리자 문의 목록 확인
    - 전체, 답변 미등록 두가지 필터 조회

### 5. 장바구니

- 장바구니 추가
    - 상품 이름, 도착 예정 날짜, 금액, 쿠폰 및 할인정보, 수량, 옵션 정보
    - 상품 옵션 - 색상, 사이즈 옵션 선택가능

- 장바구니 수정, 삭제 , 목록 확인 가능

### 6. 관심내역

- 상품 찜하기
    - 관심있는 상품 찜하기

- 찜하기 내역 삭제, 목록 확인

### 7. 쿠폰

- 쿠폰 발급
    - 회원 가입시 10% 할인 쿠폰
    - 생일 기념 20% 할인 쿠폰
    - 쿠폰 발급시 보유회원아이디, 쿠폰명, 할인율, 만료 기간, 카테고리(선택사항)

- 보유쿠폰사용 및 목록 지난 쿠폰 사용내역 확인 가능

- 매일 자정 12시 쿠폰 만료 체크 및 생일인 회원 생일 축하 쿠폰 발급

### 8. 주문

- 주문 등록
    - 주문 하나당 쿠폰 하나 사용 가능
    - 수령인 정보와 배송인 정보 포함
    - 총금액, 쿠폰 할인율 적용한 총할인금액, 총결제금액 계산하여 금액 관련 정보 포함

- 주문 수정
    - 수령인 정보, 배송지 정보, 상품 수량 정보 수정 가능

- 주문은 주문 상품을 포함
    - 주문 상품은 상품 정보, 상품 옵션 정보, 주문 상품 상태를 포함

- 구매자의 상태 변경
    - 취소 신청, 교환신청, 환불신청, 구매확정 가능

- 판매자의 상태 변경
    - 배송중, 반품 완료, 교환 완료, 환불 완료 상태 변경 가능

- 주문 조회
    - 주문 상세 내역, 주문 목록, 주문 상태 목록, 주문 상태 변경 히스토리 조회

### 9. 리뷰

- 리뷰 작성 - 구매확정건에 대해서만 가능
- 회원 이름, 제목, 내용, 주문번호, 날짜, 평점 정보 포함
- 본인의 리뷰 수정 가능
- 리뷰 삭제 및 내역 확인
- 판매자는 리뷰에 답글 가능

### 10. 커뮤니티(자유게시판)

- 자유게시판 CRUD
- 게시판 좋아요 가능
- 게시글 댓글 CRUD 기능

### 11. 상품 및 판매

- 판매자 상품 등록 가능
- 상품명, 상품금액, 상품 설명, 카테고리, 할인 가격, 원산지, 할인여부
- 옵션 - 색상, 사이즈, 개수
- 상품 RUD

### 12. 관리자

- 판매 승인 여부를 결정
- 판매 취소 승인 가능
- 승인 거절, 취소 , 승인 등등 목록 확인 가능

### 13. 상품 검색

- 태그 검색
    - 등록된 최신순 검색
    - 가격순
    - 리뷰 개수순
    - 별점순

- 카테고리 검색
    - 등록된 최신순 검색
    - 가격순
    - 리뷰 개수순
    - 별점순

- 검색어로 검색
    - 등록된 최신순 검색
    - 가격순
    - 리뷰 개수순
    - 별점순
## ERD
![ERD.png](doc%2FERD.png)

## 기술 스택
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/query DSL-527FFF?style=for-the-badge">
  <img src="https://img.shields.io/badge/gradle-2D4999?style=for-the-badge&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/json%20web%20tokens-323330?style=for-the-badge&logo=json-web-tokens&logoColor=pink">
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
</div>