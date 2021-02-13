#! /usr/bin/Rscript
library(DBI)
library(RMySQL)
library(dbConnect)
library(dplyr)
library(stringr)
library(NLP)
library(tm)
library(openNLP)
library(reticulate)

tagPOS <-  function(x, ...) {
  s <- as.String(x)
  word_token_annotator <- Maxent_Word_Token_Annotator()
  a2 <- Annotation(1L, "sentence", 1L, nchar(s))
  a2 <- annotate(s, word_token_annotator, a2)
  a3 <- annotate(s, Maxent_POS_Tag_Annotator(), a2)
  a3w <- a3[a3$type == "word"]
  POStags <- unlist(lapply(a3w$features, `[[`, "POS"))
  POStagged <- paste(sprintf("%s/%s", s[a3w], POStags), collapse = " ")
  list(POStagged = POStagged, POStags = POStags)
}
keywords<-1
keytmp<-1
keywordsf<-1
keywordst<-1
DB<-dbConnect(dbDriver("MySQL"), dbname="api", user="DB_K", password="1q2w3e4r!", host="localhost")
Query<-dbGetQuery(DB, "SELECT * FROM twitter")
Article<-dbGetQuery(DB, "SELECT * FROM articledata")
keyword<-dbGetQuery(DB, "SELECT * FROM keyword")
userdata<-dbGetQuery(DB, "SELECT * FROM userdata")
dbSendQuery(DB, "DELETE FROM sharedata")
art<-1
pythe<-1
pid<-0
idnum<-0
while(!is.na(userdata$user_id[art])){
  if(!is.na(userdata$rec_keyword2[art])){
    userd<-userdata %>% filter(userdata$user_id==userdata$user_id[art])
    if(nchar(userd$user_id)>3){
    pythe[1]<-userd$rec_keyword1
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
      keytmp[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$last_name
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
      keywordsf[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$first_name
      keywordst[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$team
      }
      else{
        keywords[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$first_name
        keywordsf[i]<-NA
        keywordst[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$team
      }
    }
    x<-as.vector(unlist(keywords))          #apply here
    x<-str_replace_all(x, "\\W", " ")
    x<-str_to_lower(x)
    xf<-as.vector(unlist(unique(keywordsf)))          #apply here
    xf<-str_replace_all(xf, "\\W", " ")
    xf<-str_to_lower(xf)
    xt<-as.vector(unlist(unique(keywordst)))          #apply here
    xt<-str_replace_all(xt, "\\W", " ")
    xt<-str_to_lower(xt)
    xt<-word(xt,1)
    for(i in 1:length(pyth)){
      if(x[i] %in% "emery"){
        x[i]<-"emeri"
      }
    }
    Query$contents<-paste(Query$team, Query$contents)
    docs.df<-data.frame(doc_id=Query$twit_id, text=Query$contents)
    docs.ds<-DataframeSource(docs.df)
    docs.cp<-Corpus(docs.ds)
    docs.cp<-tm_map(docs.cp,stemDocument)
    docs.cp<-tm_map(docs.cp,stripWhitespace)
    docs.cp<-tm_map(docs.cp,removePunctuation)
    docs.cp<-tm_map(docs.cp,removeNumbers)
    docs.cp<-tm_map(docs.cp,removeWords,stopwords("english"))
    docs.dtm<-DocumentTermMatrix(docs.cp)
    docs.dtm = removeSparseTerms(docs.dtm, as.numeric(0.99))
    count<-1
    t<-1
    p<-10
    for(i in 1:length(pyth)){
      if(nchar(x[i])!=0){
        t[i]<-x[i]
      }
    }
    asc<-findAssocs(docs.dtm, t,  0.01)
    for(i in 1:length(pyth)){
      if(length(asc[[i]])==0){
        count=1
        while(length(findAssocs(docs.dtm, t[i],  0.01)[[1]])==0){
          if(nchar(t[i])-count>5){
            p=nchar(t[i])-count
            t[i]<-strtrim(t[i], p)
          }
          else break
        }
      }
    }
    
    asct<-findAssocs(docs.dtm, xt,  0.01)
    for(i in 1:length(xt)){
      if(length(asct[[i]])==0){
        count=1
        while(length(findAssocs(docs.dtm, xt[i],  0.01)[[1]])==0&&!is.na(xt[i])){
          if(nchar(xt[i])-count>5){
            p=nchar(xt[i])-count
            xt[i]<-strtrim(xt[i], p)
          }
          else break
        }
      }
    }
    asc<-findAssocs(docs.dtm, t,  0.01)
    mylist<-lapply(asc, function(x) data.frame(terms = names(x), cor = x, stringsAsFactors = FALSE))
    if(length(mylist[[1]]$terms)!=0){
      twit <-  tagPOS(mylist[[1]]$terms)
      prp <- str_extract_all(twit$POStagged,"\\w+/NN\\$?")
      twit<-str_replace(unlist(prp), "/NN\\$?", "")
      twit <- as.data.frame(twit, stringsAsFactors = F)
      twit<-rename(twit, words=twit)
      twit <- filter(twit, twit$words%in%twit[(nchar(twit$words) >= 5),])
      twit <- filter(twit, twit$words%in%twit[(nchar(twit$words) <= 10),])
      twit <- filter(twit, !words %in% xf)
      twit <- filter(twit, !words %in% xt)
      twit<-twit %>% head(5)
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
      for(i in 1:length(fnd)){
        if(fnd[i] %in% "emeri"){
          fnd[i]<-"emery"
        }
        else if(fnd[i] %in% "citi"){
          fnd[i]<-"city"
        }
      }
      num<-1
      i<-1
      Article$contents<-paste(Article$title, Article$contents)
      while(!is.na(fnd[i])&&i<6){
        if(length(Article[str_detect(Article$contents, regex(fnd[i], ignore_case=T)),]$news_id)!=0
           &&length(row.names(as.data.frame(as.character(Article[str_detect(Article$contents, regex(fnd[i], ignore_case=T)),]$news_id))))>1){
          artid[num]<-as.data.frame(as.character(Article[str_detect(Article$contents, regex(fnd[i], ignore_case=T)),]$news_id))
          artid[[num]]<-rename(as.data.frame(table(unlist(artid[[num]]))), news_id=Var1,score=Freq)
          
          for(j in 1:length(artid[[num]]$news_id)){
            artid[[num]]$score[j]<-6-i
          }
          num=num+1
        }
        i=i+1
      }
      final<-bind_rows(artid)
      final<-final[unique(final$news_id),]
      final<-final %>% filter(!is.na(final$news_id))
      print("inserting...")
      print(pyt[a])
      shareid<-1
      while(!is.na(final$news_id[shareid])){
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
dbDisconnect(DB)
print("twit clustering success")