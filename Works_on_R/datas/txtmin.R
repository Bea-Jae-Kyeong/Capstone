#! /usr/bin/Rscript
library(DBI)
library(RMySQL)
library(dbConnect)
library(KoNLP)
library(dplyr)
library(stringr)
library(tm)
DB<-dbConnect(dbDriver("MySQL"), dbname="api", user="DB_K", password="1q2w3e4r!", host="localhost")
Article<-dbGetQuery(DB, "SELECT * FROM articledata")
keyQuery<-dbGetQuery(DB, "SELECT * FROM keyword")
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
  Query<-Article %>% filter(Article$news_id==Article$news_id[art])
  title<-Query$title
  content<-paste(Query$contents, Query$source_team)
  sentence <- strsplit(content, "(?<=[.?!]) ?", perl=TRUE)[[1]][1]
  txt<-paste(title, sentence)
  txt <- str_replace_all(txt, "\\W", " ")
  txt<-removeWords(txt, stopwords("english"))
  nouns <- extractNoun(txt)
  wordcount<-table(unlist(str_extract_all(nouns, "[A-Z][a-z]+")))
  df_word <- as.data.frame(wordcount, stringsAsFactors = F)
  df_word <- filter(df_word, !Var1 %in% c("Premier", "League", "English"))
  df_word <- filter(df_word, nchar(Var1) >= 4)
  x<-as.vector(unlist(df_word$Var1))
  
  txt<-content
  txt <- str_replace_all(txt, "\\W", " ")
  txt<-removeWords(txt, stopwords("english"))
  nouns <- extractNoun(txt)
  wordcount<-table(unlist(str_extract_all(nouns, "[A-Z][a-z]+")))
  df_word2 <- as.data.frame(wordcount, stringsAsFactors = F)
  df_word2 <- filter(df_word2, nchar(Var1) >= 4)
  df_word3 <- filter(df_word2, Var1 %in% x)
  top_ct <- df_word2 %>%   arrange(desc(Freq))
  top_ts <- df_word3 %>%   arrange(desc(Freq))
  top<-unique(bind_rows(top_ts, top_ct))
  
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
      if(k[i]%in%"City"){
        city=1
        for(j in 1:a){
          if(k[j]%in%"Leicester"){
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
      else if(k[i]%in%"United"){
        for(z in 1:a){
          if(k[z]%in%"Newcastle"){
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
    if(!is.na(finkeyw[j])){
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