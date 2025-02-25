제 3회 고용노동 공공데이터 활용 공모전 앱개발 부문 참여 

### RDMD(ready medi) : 최적의 병원 정보를 찾을 수 있는 앱 
기획의도 : 실생활에서도 사용 할 수 있는 유용한 앱 , 3가지 토픽 ( 사용자의 편의성, 시간 소모 최소화, 최적의 병원) 

### 3가지 주요 기능 : 
#### 첫번째 ) 내 주변의 병원 지도에 표시 (구글 지도 활용) 
#### 두번째 ) 사용자 맞춤형 검색 - 근로자 - 산재재활병원, 소아 - 야간진료 가능 , 노약자 - 요양병원 , 응급환자 - 응급실 . 
각각에 맞는 공공데이터 찾아서 open API를 활용함. 
#### 세번째 ) 우수병원 평가 정보를 활용해 질병 별 3번 연속 우수등급 받은 병원 조회 

다양한 검색 기능을 사용함으로써 병원에 가기 전 수많은 조건들을 고려해야 하는 번거로운 과정을 최소화 . 

좀 더 상세히)
#### 첫번째 지도 기능 )  현 위치를 gps 로 mylocation을 저장 -> Google Maps API 요청 + 해당 위치를 포함해서 URL 구성 -> 설정한 거리내(1km) 에 있음 마커 표시 


#### 두번째 검색 기능 ) 검색하고 싶은 주소(위치 , ex 부산, 울산, 서울)을 입력하면 해당 되는 주소를 포함하고 있는 맞춤형 병원이 뜨게 합니다.
editText로 location 입력값 받아오기 -> uri만들기 (ex- 해당 공공데이터의 제공되는 base api code + service key + &numOfRows=100 + &pageNo =1 ) 
-> 가져온 xml 파일에서 파일의 tag가 <addr>…</addr> 일 경우 안에 addr 테그 안에 적혀 있는 내용에서 location을 포함하고 있으면 버퍼에 추가
-> 그리고 END_document 에서 buffer에 저장된 파싱 결과를 list에 추가 후 나타낸다. 

(사용한 공공데이터의 종류) 
(근로복지공단_ 산재재활기관관리정보 api 사용) 
( 건강보험심사평가원_특수진료병원 api 사용) 
( 국립중앙의료원_ 전국 응급의료기관 정보 조회 서비스 api 사용) 

#### 세번째 기능 ) 총 3가지 카테고리  ( 암 질환, 만성질환, 급성질환 별 라디오 버튼 존재) 
검색 하고자 하는 질병명과 xml내의 질병명 이 일치시 나오게 한다. 
몇회 선정인지를 담고 있는 tag 을 찾아 3이라면 나오게 한다. 

logo --> ![image](https://github.com/user-attachments/assets/db623dbb-def2-4610-9566-98705cf8cad0)

![image](https://github.com/user-attachments/assets/45102ba9-1498-4970-8cbf-1c8d6745bcea)
![image](https://github.com/user-attachments/assets/c0e421c2-6368-4a36-bda6-6bdc9839f132)
![image](https://github.com/user-attachments/assets/893e31a9-26dd-4e6d-898e-75f46d5ffa75)
![image](https://github.com/user-attachments/assets/6e5f67b1-09a7-40cf-b3a8-46d0627c85b4)
![image](https://github.com/user-attachments/assets/25a307ed-0a80-45bf-93b5-9c351fc7a319)

QR --> ![image](https://github.com/user-attachments/assets/c4ea374e-0c7c-4c67-8f93-eeff15d4a483)

팀프로젝트 : https://github.com/syoung01/MobileProgramming_TeamProject
