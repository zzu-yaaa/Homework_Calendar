  # GCU Homework Calendar
  
  > 사이버 캠퍼스에 등록된 과제를 편리하게 확인해보세요! </br>
  색상을 통해 과제 제출 여부를 파악할 수 있고, 원하는 과제의 사이버 캠퍼스 링크로 바로 접속할 수 있습니다.
  </br>
  
  
  <img src="https://github.com/zzu-yaaa/MP_TermProject/assets/110540359/2b80fb98-e966-4ccf-aba6-7d1393e1fef5" width="216" height="444"/>  
  <img src="https://github.com/zzu-yaaa/MP_TermProject/assets/110540359/2849a8b2-9119-409d-a783-9fc7a3b5b670" width="216" height="444"/>  
  <img src="https://github.com/zzu-yaaa/MP_TermProject/assets/110540359/31dd4e0c-fd6a-4be4-b150-d27ba2404e4f" width="216" height="444"/>  
  <img src="https://github.com/zzu-yaaa/MP_TermProject/assets/110540359/5119e1a4-cfd9-4b8c-89cd-79005cb73396" width="216" height="444"/>
  </br></br>
  
  ## 기능
  
1. 사이버캠퍼스 아이디/비밀번호를 통한 로그인
2. 과제 마감기한 확인
3. 원하는 색상 선택 가능
4. 원하는 과목 선택 가능
5. 음성인식을 통한 과제 검색 가능
  </br>
  
  ## 프로젝트 설치 및 실행
  
  ```
  git clone https://github.com/zzu-yaaa/MP_TermProject.git
  ```
  
  - 실행 오류 시 참조
      
      [라이브러리 세팅 방법](https://github.com/zzu-yaaa/MP_TermProject/blob/main/setting%26error.txt)
      
      <AndroidManifest.xml>
      
      ```java
      //Web Crawling
      <uses-permission android:name="android.permission.INTERNET" />
      <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
      <uses-permission android:name="android.permission.READ_CONTACTS" />
      <uses-permission android:name="android.permission.WRITE_CONTACTS" />
  
      //SQLite
      <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
      <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  
      //STT
      <uses-permission android:name="android.permission.RECORD_AUDIO"/>
      ```

      <build.gradle(app)>
    
      ```java
     dependencies {
          //Web Crawling
          implementation 'org.jsoup:jsoup:1.13.1'

          //calendar
          implementation 'com.jakewharton.threetenabp:threetenabp:1.3.1'
          implementation project(path: ':library')
      }
     ```
  </br>
 
  
  ## 사용한 기술 소개 & 구현
  
  - 달력 커스텀 위해 material calendar view 사용
  - 데이터 저장을 위한 SQLite 사용
  - 음성인식을 위한 STT(SPEACH TO TEST) 사용
  - 자세한 사항은 [발표자료](https://github.com/zzu-yaaa/MP_TermProject/blob/main/발표자료.pdf) 참조
  </br>
  
  ## 외부 리소스 및 저작권
  
  - [material calendar view](https://github.com/prolificinteractive/material-calendarview)
  - 가천대학교 마스코트 무당이&무한이
