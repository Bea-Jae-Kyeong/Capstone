#! /usr/bin/Rscript
# 트위터 클러스터링 R 파일
library(DBI)
library(RMySQL)
library(dbConnect)
library(dplyr)
library(stringr)
library(NLP)
library(tm)
library(openNLP)
library(reticulate)

tagPOS <-  function(x, ...) {       #명사 추출하는 함수
  s <- as.String(x)
  word_token_annotator <- Maxent_Word_Token_Annotator()     #단어 토큰화
  a2 <- Annotation(1L, "sentence", 1L, nchar(s))
  a2 <- annotate(s, word_token_annotator, a2)
  a3 <- annotate(s, Maxent_POS_Tag_Annotator(), a2)
  a3w <- a3[a3$type == "word"]                              #단어일경우
  POStags <- unlist(lapply(a3w$features, `[[`, "POS"))      #태그 종류 가져옴
  POStagged <- paste(sprintf("%s/%s", s[a3w], POStags), collapse = " ")   #해당 단어에 태그 붙임
  list(POStagged = POStagged, POStags = POStags)
}
keywords<-1       #last_name 키워드 저장 벡터 변수
keytmp<-1
keywordsf<-1       #first_name 키워드 저장 벡터 변수
keywordst<-1       #team 키워드 저장 벡터 변수
DB<-dbConnect(dbDriver("MySQL"), dbname="api", user="DB_K", password="1q2w3e4r!", host="localhost")   #MySQL DB와 연결
Query<-dbGetQuery(DB, "SELECT * FROM twitter")                #트위터 테이블 가져옴
Article<-dbGetQuery(DB, "SELECT * FROM articledata")          #기사 테이블 가져옴
keyword<-dbGetQuery(DB, "SELECT * FROM keyword")              #키워드 테이블 가져옴
userdata<-dbGetQuery(DB, "SELECT * FROM userdata")            #유저 테이블 가져옴
dbSendQuery(DB, "DELETE FROM sharedata")                      #ShareData 테이블 초기화
art<-1
pythe<-1
pid<-0
idnum<-0
while(!is.na(userdata$user_id[art])){
  if(!is.na(userdata$rec_keyword2[art])){
    userd<-userdata %>% filter(userdata$user_id==userdata$user_id[art])   #유저 하나씩 불러옴
    if(nchar(userd$user_id)>3){
    pythe[1]<-userd$rec_keyword1                              #유저 키워드 id 최대 6개 저장
    pythe[2]<-userd$rec_keyword2
    pythe[3]<-userd$rec_keyword3
    pythe[4]<-userd$rec_keyword4
    pythe[5]<-userd$rec_keyword5
    pythe[6]<-userd$rec_keyword6
    c<-1
    pyth<-1
    print("user id")
    print(userd$user_id)
    for(i in 1:6){
    if(!is.na(pythe[i])){
      pyth[c]<-pythe[i]
      c=c+1
    }
    }
    c=c-1
    p=0
    for(i in 1:c){
      keytmp[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$last_name        #유저 키워드 id로 키워드 last_name 가져옴
      if(str_detect(keytmp[i], "\xe9")){
        keytmp[i]<-str_replace(keytmp[i], "\xe9", "e")
        p=1
      }
      else if(str_detect(keytmp[i], "\xd6")){
        keytmp[i]<-str_replace(keytmp[i], "\xd6", "o")
        p=1
      }
      if(nchar(keytmp[i])!=0){
        if(p==0){
      keywords[i]<-keytmp[i]
        }
      keywordsf[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$first_name        # 키워드 first_name 가져옴
      keywordst[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$team              # 키워드 team 가져옴
      }
      else{
        keywords[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$first_name
        keywordsf[i]<-NA
        keywordst[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$team
      }
    }
    x<-as.vector(unlist(keywords))                    #last_name의 특수문자 제거, 소문자화
    x<-str_replace_all(x, "\\W", " ")
    x<-str_to_lower(x)

    xf<-as.vector(unlist(unique(keywordsf)))          #first_name 의특수문자 제거, 소문자화
    xf<-str_replace_all(xf, "\\W", " ")
    xf<-str_to_lower(xf)
    xt<-as.vector(unlist(unique(keywordst)))          #team의 특수문자 제거, 소문자화
    xt<-str_replace_all(xt, "\\W", " ")
    xt<-str_to_lower(xt)
    xt<-word(xt,1)
    for(i in 1:length(pyth)){                         #예외 처리
      if(x[i] %in% "emery"){
        x[i]<-"emeri"
      }
    }
    Query$contents<-paste(Query$team, Query$contents)       #트윗 제목과 내용 합쳐서 저장
    docs.df<-data.frame(doc_id=Query$twit_id, text=Query$contents)    #데이터 프레임 생성
    docs.ds<-DataframeSource(docs.df)
    docs.cp<-Corpus(docs.ds)                                          #코퍼스 생성
    docs.cp<-tm_map(docs.cp,stemDocument)                             #어근 제거
    docs.cp<-tm_map(docs.cp,stripWhitespace)                          #공백 제거
    docs.cp<-tm_map(docs.cp,removePunctuation)                        #따옴표 제거
    docs.cp<-tm_map(docs.cp,removeNumbers)                            #숫자 제거
    docs.cp<-tm_map(docs.cp,removeWords,stopwords("english"))         #불용어 제거
    docs.dtm<-DocumentTermMatrix(docs.cp)                             #매트릭스화
    docs.dtm = removeSparseTerms(docs.dtm, as.numeric(0.99))          #1% 불필요 단어들 제거
    count<-1
    t<-1
    p<-10
    for(i in 1:length(pyth)){
      if(nchar(x[i])!=0){
        t[i]<-x[i]
      }
    }
    asc<-findAssocs(docs.dtm, t,  0.01)                             #유저 키워드(last_name)연관도 분석
    for(i in 1:length(pyth)){
      if(length(asc[[i]])==0){
        count=1
        while(length(findAssocs(docs.dtm, t[i],  0.01)[[1]])==0){   #연관도 안나올시 문자를 줄여가며 추출
          if(nchar(t[i])-count>5){
            p=nchar(t[i])-count
            t[i]<-strtrim(t[i], p)
          }
          else break
        }
      }
    }
    
    asct<-findAssocs(docs.dtm, xt,  0.01)                         # 팀 키워드 연관도 분석
    for(i in 1:length(xt)){
      if(length(asct[[i]])==0){
        count=1
        while(length(findAssocs(docs.dtm, xt[i],  0.01)[[1]])==0&&!is.na(xt[i])){     #연관 키워드에서 키워드 소속 팀 이름 제거
          if(nchar(xt[i])-count>5){
            p=nchar(xt[i])-count
            xt[i]<-strtrim(xt[i], p)
          }
          else break
        }
      }
    }
    asc<-findAssocs(docs.dtm, t,  0.01)                           #유저 키워드의 연관도와 연관 키워드 저장
    mylist<-lapply(asc, function(x) data.frame(terms = names(x), cor = x, stringsAsFactors = FALSE))    #리스트화
    if(length(mylist[[1]]$terms)!=0){
      twit <-  tagPOS(mylist[[1]]$terms)                          #명사 추출 함수 호출
      prp <- str_extract_all(twit$POStagged,"\\w+/NN\\$?")        #명사 토큰 추출
      twit<-str_replace(unlist(prp), "/NN\\$?", "")
      twit <- as.data.frame(twit, stringsAsFactors = F)
      twit<-rename(twit, words=twit)
      twit <- filter(twit, twit$words%in%twit[(nchar(twit$words) >= 5),])     #5글자 이상 10글자 이하 연관 키워드만 추출
      twit <- filter(twit, twit$words%in%twit[(nchar(twit$words) <= 10),])
      twit <- filter(twit, !words %in% xf)                        #연관 키워드 속 팀 이름과 first_name 제거(관련성이 높으므로)
      twit <- filter(twit, !words %in% xt)
      twit<-twit %>% head(5)                                      #연관도순으로 5위까지 추출
      twit$id<-names(mylist[1])
      twitt<-twit
    }else {
      twitt<-NA
    }
    for(i in 2:length(pyth)){
      if(length(mylist[[i]]$terms)!=0){
        twit <-  tagPOS(mylist[[i]]$terms)
        prp <- str_extract_all(twit$POStagged,"\\w+/NN\\$?")
        twit<-str_replace(unlist(prp), "/NN\\$?", "")
        twit <- as.data.frame(twit, stringsAsFactors = F)
        twit<-rename(twit, words=twit)
        twit <- filter(twit, twit$words%in%twit[(nchar(twit$words) >= 5),])
        twit<-filter(twit, twit$words%in%twit[(nchar(twit$words) <= 10),])
        twit <- filter(twit, !words %in% xf)
        twit <- filter(twit, !words %in% xt)
        twit<-twit %>% head(5)
        twit$id<-names(mylist[i])
        twitt<-rbind(twitt, twit)
      }
    }
    if(length(twitt)==2){
      twitnam<-unique(twitt$id)
    cnt<-1
    twitna<-1
    pyt<-1
    artid<-0
    for(i in 1:length(twitnam)){
      if(!is.na(twitnam[i])){
        twitna[cnt]<-twitnam[i]
        pyt[cnt]<-pyth[i]
        cnt<-cnt+1
      }
    }
    if(cnt!=1){
      cnt<-cnt-1
    }
    for(a in 1:cnt){
      fnd<-filter(twitt, twitt$id == twitna[a])$words
      for(i in 1:length(fnd)){                              #예외 처리
        if(fnd[i] %in% "emeri"){
          fnd[i]<-"emery"
        }
        else if(fnd[i] %in% "citi"){
          fnd[i]<-"city"
        }
      }
      num<-1
      i<-1
      Article$contents<-paste(Article$title, Article$contents)        #기사 제목과 내용 합쳐서 저장
      #기사에서 해당 연관 키워드 존재하는지 탐색
      while(!is.na(fnd[i])&&i<6){
        if(length(Article[str_detect(Article$contents, regex(fnd[i], ignore_case=T)),]$news_id)!=0
           &&length(row.names(as.data.frame(as.character(Article[str_detect(Article$contents, regex(fnd[i], ignore_case=T)),]$news_id))))>1){
          artid[num]<-as.data.frame(as.character(Article[str_detect(Article$contents, regex(fnd[i], ignore_case=T)),]$news_id))
          artid[[num]]<-rename(as.data.frame(table(unlist(artid[[num]]))), news_id=Var1,score=Freq)
          
          for(j in 1:length(artid[[num]]$news_id)){ #기사 속에 연관 키워드 존재 시
            artid[[num]]$score[j]<-6-i              #연관 키워드의 순위에 따라 5점부터 최소 1점까지 부여
          }
          num=num+1
        }
        i=i+1
      }
      final<-bind_rows(artid)
      final<-final[unique(final$news_id),]          #기사 id 추출
      final<-final %>% filter(!is.na(final$news_id))
      print("inserting...")
      print(pyt[a])
      shareid<-1
      while(!is.na(final$news_id[shareid])){        #ShareData 테이블에 share_id, 키워드 id, 기사 id, 클러스터링 점수 추가하는 쿼리문 DB에 보냄
        dbSendQuery(DB, paste("INSERT INTO sharedata(share_id, keyword_id, news_id, rec_point) VALUES(",pid+shareid,",",pyt[a],",",final$news_id[shareid],",",final$score[shareid], ")", sep=""))
        shareid=shareid+1
      }
      
      pid<-pid+shareid-1
      print(pid)
    }
    }
    }
  art=art+1
  }
}
dbDisconnect(DB)                #DB 연결 해제
print("twit clustering success")