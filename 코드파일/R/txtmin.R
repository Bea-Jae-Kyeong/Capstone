#! /usr/bin/Rscript
# 데이터 마이닝 R 파일
library(DBI)
library(RMySQL)
library(dbConnect)
library(KoNLP)
library(dplyr)
library(stringr)
library(tm)
DB<-dbConnect(dbDriver("MySQL"), dbname="api", user="DB_K", password="1q2w3e4r!", host="localhost")   #DB 연결
Article<-dbGetQuery(DB, "SELECT * FROM articledata")                #기사 테이블 불러옴
keyQuery<-dbGetQuery(DB, "SELECT * FROM keyword")                   #키워드 테이블 불러옴
keyword<-"a"
keyword[1]<-"keyword1"
keyword[2]<-"keyword2"
keyword[3]<-"keyword3"
keyword[4]<-"keyword4"
keyword[5]<-"keyword5"
keyword[6]<-"keyword6"
keyword[7]<-"keyword7"
keyword[8]<-"keyword8"
keyword[9]<-"keyword9"
keyword[10]<-"keyword10"
art=1
while(!is.na(Article$news_id[art])){
  if(is.na(Article$keyword1[art])){
  Query<-Article %>% filter(Article$news_id==Article$news_id[art])          #기사별로 선택
  title<-Query$title
  content<-paste(Query$contents, Query$source_team)                         #기사 내용과 출처 팀 합쳐서 저장
  sentence <- strsplit(content, "(?<=[.?!]) ?", perl=TRUE)[[1]][1]          #기사 본문의 첫 문장 추출

  #기사 제목과 첫 문장에 대해 필터링하여 키워드 추출
  txt<-paste(title, sentence)                                               #기사 제목과 첫 문장 합쳐서 저장
  txt <- str_replace_all(txt, "\\W", " ")                                   #특수문자 제거
  txt<-removeWords(txt, stopwords("english"))                               #불용어 제거
  nouns <- extractNoun(txt)                                                 #명사 추출
  wordcount<-table(unlist(str_extract_all(nouns, "[A-Z][a-z]+")))           #대명사 추출
  df_word <- as.data.frame(wordcount, stringsAsFactors = F)                 #데이터프레임화, 기사 속 빈도수 추출
  df_word <- filter(df_word, !Var1 %in% c("Premier", "League", "English"))  #해당 키워드 제외
  df_word <- filter(df_word, nchar(Var1) >= 4)                              #4글자 이상 단어 추출
  x<-as.vector(unlist(df_word$Var1))
  #기사 내용에 대해 필터링하여 키워드 추출
  txt<-content                                  
  txt <- str_replace_all(txt, "\\W", " ")                                   #특수문자 제거
  txt<-removeWords(txt, stopwords("english"))                               #불용어 제거
  nouns <- extractNoun(txt)                                                 #명사 추출
  wordcount<-table(unlist(str_extract_all(nouns, "[A-Z][a-z]+")))           #대명사 추출
  df_word2 <- as.data.frame(wordcount, stringsAsFactors = F)                #데이터프레임화, 기사 속 빈도수 추출
  df_word2 <- filter(df_word2, nchar(Var1) >= 4)                            #4글자 이상 단어 추출
  df_word3 <- filter(df_word2, Var1 %in% x)                                 #기사 제목과 첫 문장에서 추출된 키워드 제외
  top_ct <- df_word2 %>%   arrange(desc(Freq))                              #빈도순으로 정렬
  top_ts <- df_word3 %>%   arrange(desc(Freq))
  top<-unique(bind_rows(top_ts, top_ct))                                    #기사 제목과 첫 문장 키워드가 먼저, 기사 내용이 그 아래로 가도록 리스트 병합 
  
  x<-as.vector(unlist(top$Var1))
  
  n=1
  k<-"r"
  a=1
  keywords<-1
  while(!is.na(x[a])){
    if(length(keyQuery[str_detect(keyQuery$full_name, regex(x[a], ignore_case=T)),]$keyword_no)!=0){
      k[n]<-x[a]
      n=n+1
    }
    a=a+1
  }
  for(i in 1:a){
    if(!is.na(k[i])){
      city=0
      if(k[i]%in%"City"){                         #중복되는 팀 이름 처리
        city=1
        for(j in 1:a){
          if(k[j]%in%"Leicester"){                #키워드 리스트에 해당 이름이 들어있는지 확인 후 팀 키워드 id 저장
            keywords[i]<-514
          }
          else if(k[j]%in%"Manchester"&&city==1){
            keywords[i]<-516
            city=0
          }
          else if(k[j]%in%"Norwich"){
            keywords[i]<-514
          }
        }
      }
      else if(k[i]%in%"United"){                  #중복되는 팀 이름 처리
        for(z in 1:a){
          if(k[z]%in%"Newcastle"){                #키워드 리스트에 해당 이름이 들어있는지 확인 후 팀 키워드 id 저장
            keywords[i]<-518
          }
          else if(k[z]%in%"Manchester"&&city!=1){
            keywords[i]<-517
          }
          else if(k[z]%in%"Sheffield"){
            keywords[i]<-520
          }
          else if(k[z]%in%"West"|| k[z]%in%"Ham"){
            keywords[i]<-524
          }
        }
      }
      else if(k[i]%in%"Manchester"){
        for(z in 1:a){
          if(k[z]%in%"City"){
            keywords[i]<-516
          }
          else if(k[z]%in%"United"){
            keywords[i]<-517
          }
        }
      }
      else{
        if(length(keyQuery[str_detect(keyQuery$full_name, regex(k[i], ignore_case=T)),]$keyword_no)>1){
          #키워드 full_name에 해당 키워드 있는지 확인 후 키워드 id 저장
          for(z in 1:a){
            if(length(grep(k[z],keyQuery[str_detect(keyQuery$full_name, regex(k[i], ignore_case=T)),]$team, ignore.case =T))==1){
              keywords[i]<-keyQuery[str_detect(keyQuery$full_name, regex(k[i], ignore_case=T)),]$keyword_no[z]
            }
          }
        }
        else{
          keywords[i]<-keyQuery[str_detect(keyQuery$full_name, regex(k[i], ignore_case=T)),]$keyword_no
        }
      }
    }
    else{
      keywords[i]<-NA
    }
  }
  keywords<-unique(keywords)
  finkeyw<-1
  b=1
  for(j in 1:a){
    if(!is.na(keywords[j])){
    finkeyw[b]<-keywords[j]
    b=b+1
    }
  }
  for(j in 1:10){
    if(!is.na(finkeyw[j])){           # ArticleData 테이블에 keyword1 부터 최대 keyword10까지 순서대로 저장하는 쿼리문 DB에 보냄
     dbSendQuery(DB, paste("UPDATE articledata set ",keyword[j],"=",finkeyw[j]," where news_id=",Query$news_id, sep=""))
    }
    else{
      dbSendQuery(DB, paste("UPDATE articledata set ",keyword[j],"=NULL where news_id=",Query$news_id, sep=""))
    }
  }
  }
  art=art+1
}
dbDisconnect(DB)
print("keyword success")