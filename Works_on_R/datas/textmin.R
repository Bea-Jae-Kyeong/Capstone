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
Query<-Article %>% filter(Article$news_id==Article$news_id[art])
art=art+1
title<-Query$title
content<-Query$contents
txt<-title
txt <- str_replace_all(txt, "\\W", " ")
txt<-removeWords(txt, stopwords("english"))
nouns <- extractNoun(txt)
wordcount<-table(unlist(str_extract_all(nouns, "[A-Z][a-z]+")))
df_word <- as.data.frame(wordcount, stringsAsFactors = F)
df_word <- rename(df_word, word = Var1, freq = Freq)
df_word <- filter(df_word, !word %in% c("Premier", "League", "English"))
df_word <- filter(df_word, nchar(word) >= 4)
sentence <- strsplit(content, "(?<=[.?!]) ?", perl=TRUE)[[1]][1]
txt<-sentence
txt <- str_replace_all(txt, "\\W", " ")
txt<-removeWords(txt, stopwords("english"))
nouns <- extractNoun(txt)
wordcount<-table(unlist(str_extract_all(nouns, "[A-Z][a-z]+")))
df_word1 <- as.data.frame(wordcount, stringsAsFactors = F)
df_word1 <- rename(df_word1, word = Var1, freq = Freq)
df_word1 <- filter(df_word1, !word %in% c("Premier", "League", "English"))
df_word1 <- filter(df_word1, nchar(word) >= 4)
x<-as.vector(unlist(unique(bind_rows(df_word, df_word1))$word))
txt<-paste(title, content)
txt <- str_replace_all(txt, "\\W", " ")
txt<-removeWords(txt, stopwords("english"))
nouns <- extractNoun(txt)
wordcount<-table(unlist(str_extract_all(nouns, "[A-Z][a-z]+")))
df_word2 <- as.data.frame(wordcount, stringsAsFactors = F)
df_word2 <- rename(df_word2, word = Var1, freq = Freq)
df_word2 <- filter(df_word2, word %in% x)
df_word2 <- filter(df_word2, nchar(word) >= 4)
top_10 <- df_word2 %>%   arrange(desc(freq)) %>%   head(10)
top_10
x<-as.vector(unlist(top_10$word))

n=1
k<-"r"
for(a in 1:10){
  if(length(keyQuery[str_detect(keyQuery$full_name, regex(x[a], ignore_case=T)),]$keyword_no)!=0){
    k[n]<-x[a]
    n=n+1
  }
}
for(i in 1:10){
  if(!is.na(k[i])){
    city=0
    if(k[i]%in%"City"){
      city=1
      for(j in 1:10){
        if(k[j]%in%"Leicester"){
          dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=514 where news_id=",Query$news_id, sep=""))
        }
        else if(k[j]%in%"Manchester"&&city==1){
          dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=516 where news_id=",Query$news_id, sep=""))
          city=0
        }
        else if(k[j]%in%"Norwich"){
          dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=519 where news_id=",Query$news_id, sep=""))
        }
      }
    }
    else if(k[i]%in%"United"){
      for(z in 1:10){
        if(k[z]%in%"Newcastle"){
          dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=518 where news_id=",Query$news_id, sep=""))
        }
        else if(k[z]%in%"Manchester"&&city!=1){
          dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=517 where news_id=",Query$news_id, sep=""))
        }
        else if(k[z]%in%"Sheffield"){
          dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=520 where news_id=",Query$news_id, sep=""))
        }
        else if(k[z]%in%"West"|| k[z]%in%"Ham"){
          dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=524 where news_id=",Query$news_id, sep=""))
        }
      }
    }
    else if(k[i]%in%"Manchester"){
      for(z in 1:10){
        if(k[z]%in%"City"){
          dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=516 where news_id=",Query$news_id, sep=""))
        }
        else if(k[z]%in%"United"){
          dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=517 where news_id=",Query$news_id, sep=""))
        }
      }
    }
    else{
      dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=",keyQuery[str_detect(keyQuery$full_name, regex(k[i], ignore_case=T)),]$keyword_no," where news_id=",Query$news_id, sep=""))
    }
  }
  else{
    dbSendQuery(DB, paste("UPDATE articledata set ",keyword[i],"=NULL where news_id=",Query$news_id, sep=""))
  }
}
}
dbDisconnect(DB)
