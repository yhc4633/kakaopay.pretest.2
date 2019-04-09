## 개발 프레임 워크
> spring boot - 1.5.19.RELEASE

## 문제해결 전략

### 1) 데이터 구조
- **생태 관광**, **지역**, **테마**, **프로그램**으로 엔터티 구분

- 엔터티
<pre><code>
Ecotourism
    ecotourism_code | program_code
    
Region
    region_code | do | si | goon | gu | myun | ri | eub | dong | etc

Theme
    theme_code | name

Program
    program_code | name | intro | detail
    
Ecotourism_Region
    ecotourism_code | region_code
 
Ecotourism_Theme
    ecotourism_code | theme_code
</code></pre>

- 관계
1. Ecotourism : Region = N : N
2. Ecotourism : Theme = N : N
3. Ecotourism : Program = N : 1

- 응답 코드

| Code | State |
|:---|:---|
| 0 | SUCCESS |
| 1 | FAIL |
| 5001 | ERROR_WRONG_PARAMETER |
| 10001 | ERROR_NO_DATA |
| 10002 | ERROR_EXIST_FAILED_DATA |

### 2) API 개발
#### 1. 데이터 파일을 통해 저장 api
- 파일 row를 지역, 테마, 프로그램으로 분류 해 저장
- 지역 저장 시 시, 군, 구 등 세분화 하여 저장. 중복 확인
- 테마 저장 시 중복 확인
- 프로그램 저장 시 이름, 소개, 상세 설명을 묶어 하나의 프로그램으로 간주

#### 2. 테이터 조회 api
- 지역 코드를 이용해 지역 정보 조회
- 해당 지역 정보를 사용하는 프로그램 정보들 조회

#### 3. 테이터 추가 api
- 파일로 받았던 각 row를 개별로 받는 개념
- 개별 저장 시의 메인 로직은 파일로 저장 시와 동일

#### 4. 테이터 수정 api
- 수정 시 관광 고유 코드를 반드시 인자로 전달
- 등록시 입력한 모든 정보를 수정 할 수 있다
- 지역 및 테마는 새로 추가된 정보를 db 등록, 제외 된 정보를 다른 사용 처 확인 후 없으면 제거 
- 프로그램은 수정 발생 시 해당 관광 정보만 사용하는 프로그램이면 db 업데이트, 다른 관광 정보도 사용 시 db 등록 

#### 5. 서비스 지역 키워드로 프로그램명, 테마 출력 api
- 입력 받은 키워드에 해당하는 지역 정보 조회
- 해당 지역 정보를 사용하는 관광 정보를 조회해 프로그램 명과 테마 목록 출력

#### 6. 프로그램 소개 키워드로 서비스 지역 정보, 개수 출력 api
- 입력 받은 키워드에 해당되는 프로그램 정보들 조회
- 해당 프로그램들을 사용하는 관광 정보를 조회
- 지역 정보의 중복을 제외해 카운팅 하여 출력

#### 7. 프로그램 상세 정보 키워드로 키워드의 출현 빈도 수 출력 api
- 등록된 프로그램 정보들 조회
- 프로그램 상세 정보에서 해당 키워드 카운팅 하여 출력

#### 8. 지역명, 관광 키워드로 추천 프로그램 코드 출력 api 
- 지역명 기준 관광 정보들 조회
- 조회된 정보들에서 테마, 프로그램 소개, 프로그램 상세 정보를 사용해 가중치 계산
- 테마 50, 프로그램 소개 30, 프로그램 상세 정보 20의 비중을 두고 점수 계산
- 최종 점수가 가장 높은 관광 정보 출력
- 동점 시 임의 추천

## 빌드 및 실행 방법

#### 하위 정보 추가 후 빌드 및 서버 실행
<pre><code>
[local build 시]
 Arguments
 -Pprofile=local
 
[local 실행 시]
 VM Options
 -Dspring.profiles.active=local
 
 Program Arguments
 --spring.profiles.active=local
</code></pre>

#### 헤더 정보
> Content-Type : application/json

#### 인증 api
> 생태 관광 api 사용 전 반드시 토큰을 발급 받아 header에 사용해야 합니다

| Desc | Method | Header | Url | Parameter | Result |
|:---:|:---:|:---|:---|:---|:---|
| 가입 및 토큰 발급 | POST | - transactionId : string | localhost:10080/certify/signup | - id : string <br> - password : string | - token : string |
| 로그인 및 토큰 발급 | POST | - transactionId : string | localhost:10080/certify/signin | - id : string <br> - password : string | - token : string |
| 토큰 재발급 | PUT | - Authorization : 'Bearer Token'<br> - token : string <br> - transactionId : string | localhost:10080/certify/refresh | | - token : string |

#### 생태 관광 api

| Desc | Method | Header | Url | Parameter | Result |
|:---:|:---:|:---|:---|:---|:---|
| csv 파일 저장 | POST | - Authorization : 발급받은 token <br> - transactionId : string | localhost:10080/ecotourism/file/register | - ecotourismFile : multipart form data 형식으로 전달할 csv 파일 | - resultCode : int |
| 지역 코드로 조회 | GET | - Authorization : 발급받은 token <br> - transactionId : string | localhost:10080/ecotourism/tour/search | - regionCode : string (`reg_숫자` 형식 엄수) | - ecotourismCode : string <br> - programName : string <br> - theme : string <br> - region : string <br> - programIntro : string <br> - programDetail : string |
| 개별 등록 | POST | - Authorization : 발급받은 token <br> - transactionId : string | localhost:10080/ecotourism/tour/register | - programName (not null) : string <br> - theme (not null) : string <br> - region (not null) : string <br> - programIntro : string <br> - programDetail : string | - resultCode : int |
| 정보 수정 | PUT | - Authorization : 발급받은 token <br> - transactionId : string | localhost:10080/ecotourism/tour/modify | - ecotourismCode (`ectr_숫자` 형식 엄수. 지역 코드로 조회 api로 확인 가능) : string <br> - programName (not null) : string <br> - theme (not null) : string <br> - region (not null) : string <br> - programIntro : string <br> - programDetail : string | - resultCode : int |
| 서비스 지역으로 프로그램명, 테마 조회 | GET | - Authorization : 발급받은 token <br> - transactionId : string | localhost:10080/ecotourism/tour/summary | - regionKeyword : string (조회할 지역 키워드. (ex: 평창군, 경기도...)) | - resultCode : int <br> - region : string <br> - programName : string <br> - theme : string |
| 프로그램 소개로 서비스 지역 정보, 갯수 조회 | GET | - Authorization : 발급받은 token <br> - transactionId : string | localhost:10080/ecotourism/tour/frequency | - programIntroKeyword (프로그램 소개 키워드. (ex: 세계문화유산, 국립공원...)) : string | - resultCode : int <br> - region : string <br> - count : int |
| 프로그램 상세 정보로 출현 빈도 수 조회 | GET | - Authorization : 발급받은 token <br> - transactionId : string | localhost:10080/ecotourism/program/frequency | - programDetailKeyword (프로그램 상세 정보 키워드. (ex: 문화, 한산대첩...)) : string | - resultCode : int <br> - programDetailKeyword : string <br> - count : int |
| 프로그램 상세 정보로 출현 빈도 수 조회 | GET | - Authorization : 발급받은 token <br> - transactionId : string | localhost:10080/ecotourism/tour/recommend | - regionKeyword (조회할 지역 키워드. (ex: 평창군, 경기도...)) : string <br> - recommendKeyword : string (검색하고 싶은 키워드 (ex : 국립공원, 체험...)) | - resultCode : int <br> - ecotourismCode : string |

#### ※ 간편한 테스트가 필요 할 시 서버 실행 후 http://localhost:10080/swagger-ui.html 를 이용 할 수 있습니다.