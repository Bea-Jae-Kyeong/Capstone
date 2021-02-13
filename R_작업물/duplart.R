#! /usr/bin/Rscript
library(DBI)
library(RMySQL)
library(dbConnect)
library(dplyr)
library(stringr)
DB<-dbConnect(dbDriver("MySQL"), dbname="api", user="DB_K", password="1q2w3e4r!", host="localhost")
Articled<-dbGetQuery(DB, "SELECT * FROM articledata")
dateQuery<-strtrim(Articled$date,10)
dateQuery<-unique(dateQuery)
datQuery<-1
drt=1
c=1
delnum=0
Query<-1
while(!is.na(dateQuery[drt])){
  if(dateQuery[drt]>'2019-12-13'){
    datQuery[c]<-dateQuery[drt]
    c=c+1
  }
  drt=drt+1
}
c<-c-1

for(a in 1:c){
  Article<-Articled %>% filter(strtrim(Articled$date,10) %in% datQuery[a])
  art=1
  donti=0
  dontj=0
  while(!is.na(Article$news_id[art])){
    art=art+1
  }
  art<-art-1
for(i in 1:art){
  if(donti!=i&&dontj!=i){
    Query<-Article %>% filter(Article$news_id==Article$news_id[i])
    for(j in 1:art){
      if(j!=i&&donti!=j&&dontj!=j){
        sQuery<-Article %>% filter(Article$news_id==Article$news_id[j])
        sQuery$title <- str_replace_all(sQuery$title, "\\W", " ")
        Query$title <- str_replace_all(Query$title, "\\W", " ")
        acount=0
        if(length(strsplit(Query$title, " ")[[1]]) <=length(strsplit(sQuery$title, " ")[[1]]) ){
          for(k in 1:length(strsplit(Query$title, " ")[[1]]) ){
            if((strsplit(Query$title, " ")[[1]] %in% strsplit(sQuery$title, " ")[[1]])[k]==TRUE){
              acount=acount+1
            }
          }
          per<-acount/length(strsplit(Query$title, " ")[[1]])
        }
        else{
          per<-acount/length(strsplit(sQuery$title, " ")[[1]])
        }
        
        if(per>=0.8){
          Q<-Query
          sQ<-sQuery
          spQ<-strsplit(Q$title, "(?<=[.?!]) ?", perl=TRUE)[[1]][1]
          spsQ<-strsplit(sQ$title, "(?<=[.?!]) ?", perl=TRUE)[[1]][1]
          count<-0
          
            for(k in 1:length(strsplit(spQ, " ")[[1]]) ){
              if((strsplit(spQ, " ")[[1]] %in% strsplit(spsQ, " ")[[1]])[k]==TRUE){
                count=count+1
              }
            }
            donti=i
            dontj=j
            if(length(strsplit(spQ, " ")[[1]]) <=length(strsplit(spsQ, " ")[[1]]) ){
            sper<-count/length(strsplit(spQ, " ")[[1]])
            }
            else{
              sper<-count/length(strsplit(spsQ, " ")[[1]])
            }
            if(sper>=0.8){
              delnum=delnum+1
              if(Q$date<=sQ$date){
                del<-Q$news_id
                print(del)
                dbSendQuery(DB, paste("DELETE FROM articledata where news_id=",del, sep=""))
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