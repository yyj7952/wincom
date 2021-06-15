#JVM 환경변수 설정 확인
 1) -Dconfig.file.path=[workspace]/src/main/profiles/local/external-files/nosql.conf  -> NoSQL연동파일
 2) -Dtempdb.config.path=[workspace]/src/main/profiles/local/external-files/tempdb.conf  -> TempDB연동파일
 3) -Dtlolog.index.name=[서버명]_[서버번호]  -> TLO로그 파일명으로 지정하기 위한 정보
    
 #무중단 설정 API 호출방법
   1) 무중단모드 실행
     -> http://[IP]/mjdstart
		해더셋팅 : 'accept-mode:Non-Stop'
   2) 무중단모드 해제
     -> http://[IP]/mjdstop
		해더셋팅 : 'accept-mode:Normal'
		
 # NoSQL로 연동되는 DB쿼리나 함수가 수정되어 조회되는 데이터에 변화가 생길 경우에 대한 조치
   - NoSQL에 캐싱되어 있는 데이터를 업데이트 해야 하므로 NoSQL연동되는 부분의 [SQL_ID]의 순번을 Up시킨 후 재배포가 필요 함.