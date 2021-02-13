R 폴더

	TWITEX.R :  트위터 클러스터링
	DUPLART.R : 중복제거
	TXTMIN.R : 데이터마이닝

Server_K 폴더

	Manage.py : django manage파일
	Parser.py : 기사 크롤링 파일
	Twit_parser.py : 트위터 크롤링 파일
	Recommendation_system.py : 추천시스템 파일
	각 언론사 파일 : 마지막으로 크롤링했던 언론사 팀 기사 URL
	Server_K 폴더 : django setting 파일들
	api폴더내부 :  
		init, admin, apps, test : django 기본파일
		models , serializers: django 데이터베이스 객체 선언 및 연동파일
		views, urls : django 서버 네트워크 url 선언 및 실행 클래스 파일 

Sql 폴더

	각 테이블 sql 파일




Android 폴더

	src/main/java/com/example/testapplication 폴더 (자바파일)
1.	APIHelper.java : retrofit을 이용한 서버연결에 대한 클래스
2.	ArticleAdapter: 추천뉴스 화면 recycler view의 adapter 파일
3.	ArticleAdapterNoHeader: 관련뉴스 화면 recycler view의 adapter파일
4.	ArticleAdapterTotal: 최신뉴스 화면 recycler view의 adapter 파일
5.	ArticleApiService: retrofit연결을 위한 interface 파일
6.	ArticleDetail: 기사세부내용 화면을 구성하는 파일
7.	ArticleFragment: 관련뉴스 화면 fragment 구성하는 파일
8.	ArticleModel: 추천뉴스화면, 최신뉴스화면, 관련뉴스화면의 recyclerview의 item 객체
9.	ArticleModel2: 기사세부내용 화면의 기사정보를 가지는 객체
10.	ArticleWebview: 기사원문화면을 구성하는 파일
11.	BottomViewPagerAdpater: BottomNavigationView의 adapter파일
12.	CallbackWithRetry: 서버연결 재요청을 하게끔 해주는 추상클래스 파일
13.	FetchArticleData: 서버와 통신해 기사정보를 가져오는 파일
14.	FirstAuthActivity: 앱 실행시 팀선택화면으로 갈건지 홈화면으로 갈건지 결정해주는 파일
15.	HomeFragment: 홈화면(추천뉴스화면)을 구성하는 파일
16.	KeywordModel: 사용자의 키워드 정보를 가지는 객체
17.	MainActivty: 메인화면 - 홈화면, 최신뉴스화면, 관련뉴스화면, 트위터화면, 설정화면에 대한 Fragment 를 실행할 수 있게 해주는 파일
18.	ResultsListActivity: 검색결과화면을 구성하는 파일
19.	SaveSharedPreference: SharedPreference로 사용자 고유id, 키워드 3개에 대한 정보를 앱에 저장
20.	SearchAdpater: 검색된 결과에 대한 목록을 제공하기 위한 adapter파일
21.	SearchResultsActivity: 검색화면을 구성하는 파일
22.	SettingFragment: 설정화면을 구성하는 파일
23.	Tab1_1Fragment: 관련뉴스화면에서 첫번째 키워드에 대한 화면을 구성하는 파일
24.	Tab1_2Fragment: 관련뉴스화면에서 두번째 키워드에 대한 화면을 구성하는 파일
25.	Tab1_3Fragment: 관련뉴스화면에서 세번째 키워드에 대한 화면을 구성하는 파일
26.	TabViewPagerAdpater: 관련뉴스화면의 Tab을 구성하기 위한 adpater파일
27.	TeamSelect: 팀선택 화면을 구성하는 파일
28.	TotalFragment: 최신뉴스화면을 구성하는 파일
29.	TwitterFragment: 트위터 화면을 구성하는 파일
30.	TwitterSubFragment: 트위터화면에서 팀별 Tab화면을 구성하기 위한 파일
31.	UserMode: 사용자정보를 가지는 객체
src/main/res/drawable 폴더
-	화면구성에 필요한 이미지들을 가지고 있는 폴더
src/main/res/font 폴더
-	앱 폰트 구성에 필요한 폴더
src/main/res/layout 폴더
-	앱 화면(레이아웃) 폴더
src/main/res/menu 폴더
-	앱 화면의 메뉴(툴바) 구성에 필요한 폴더
src/main/res/values 폴더
-	화면구성에 필요한 문자열이나 컬러, 스타일값들을 가지고 있는 폴더
