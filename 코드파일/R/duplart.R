#! /usr/bin/Rscript
# DB의 중복 기사 제거하는 R 파일
library(DBI)
library(RMySQL)
library(dbConnect)
library(dplyr)
library(stringr)
DB<-dbConnect(dbDriver("MySQL"), dbname="api", user="DB_K", password="1q2w3e4r!", host="localhost")   #DB와 연결
Articled<-dbGetQuery(DB, "SELECT * FROM articledata")   #기사 테이블 불러옴
dateQuery<-strtrim(Articled$date,10)                    #기사 날짜 수집
dateQuery<-unique(dateQuery)
datQuery<-1
drt=1
c=1
delnum=0
Query<-1
while(!is.na(dateQuery[drt])){
  if(dateQuery[drt]>'2019-12-13'){                      #해당 날짜 이후 날짜만 저장
    datQuery[c]<-dateQuery[drt]
    c=c+1
  }
  drt=drt+1
}
c<-c-1

for(a in 1:c){
  Article<-Articled %>% filter(strtrim(Articled$date,10) %in% datQuery[a])    #날짜별로 기사 추출
  art=1
  donti=0
  dontj=0
  while(!is.na(Article$news_id[art])){
    art=art+1
  }
  art<-art-1
for(i in 1:art){
  if(donti!=i&&dontj!=i){                                                   #중복 탐색 처리
    Query<-Article %>% filter(Article$news_id==Article$news_id[i])          #각 기사 추출
    for(j in 1:art){                                                        #이중 루프로 두 기사씩 비교
      if(j!=i&&donti!=j&&dontj!=j){
        sQuery<-Article %>% filter(Article$news_id==Article$news_id[j])
        sQuery$title <- str_replace_all(sQuery$title, "\\W", " ")           #기사 제목의 특수문자 제거
        Query$title <- str_replace_all(Query$title, "\\W", " ")
        acount=0
        if(length(strsplit(Query$title, " ")[[1]]) <=length(strsplit(sQuery$title, " ")[[1]]) ){ 
          for(k in 1:length(strsplit(Query$title, " ")[[1]]) ){
            if((strsplit(Query$title, " ")[[1]] %in% strsplit(sQuery$title, " ")[[1]])[k]==TRUE){     #기사 제목 속 단어끼리 일치 비교
              acount=acount+1
            }
          }
          per<-acount/length(strsplit(Query$title, " ")[[1]])           #일치율 저장
        }
        else{
          per<-acount/length(strsplit(sQuery$title, " ")[[1]])
        }
        
        if(per>=0.8){                                                   #80% 이상의 일치율을 보일 때
          Q<-Query
          sQ<-sQuery
          spQ<-strsplit(Q$contents, "(?<=[.?!]) ?", perl=TRUE)[[1]][1]     #기사 내용의 첫 문장 추출
          spsQ<-strsplit(sQ$contents, "(?<=[.?!]) ?", perl=TRUE)[[1]][1]
          count<-0
          
            for(k in 1:length(strsplit(spQ, " ")[[1]]) ){
              if((strsplit(spQ, " ")[[1]] %in% strsplit(spsQ, " ")[[1]])[k]==TRUE){       #기사 첫 문장 속 단어끼리 일치 비교
                count=count+1
              }
            }
            donti=i
            dontj=j
            if(length(strsplit(spQ, " ")[[1]]) <=length(strsplit(spsQ, " ")[[1]]) ){
            sper<-count/length(strsplit(spQ, " ")[[1]])                   #일치율 저장
            }
            else{
              sper<-count/length(strsplit(spsQ, " ")[[1]])
            }
            if(sper>=0.8){                                                #80% 이상의 일치율을 보일 때
              delnum=delnum+1
              if(Q$date<=sQ$date){                                        #최근이 아닌 기사 선택
                del<-Q$news_id
                print(del)
                dbSendQuery(DB, paste("DELETE FROM articledata where news_id=",del, sep=""))    #DB에 해당 기사 제거하는 쿼리문 보냄
              }
              else{
                del<-sQ$news_id
                print(del)
                dbSendQuery(DB, paste("DELETE FROM articledata where news_id=",del, sep=""))
              }
            }
        }
        }
      }
    }
}
}
print(delnum)
dbDisconnect(DB)
print("overlap success")