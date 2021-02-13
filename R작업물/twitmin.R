library(DBI)
library(RMySQL)
library(dbConnect)
library(KoNLP)
library(dplyr)
library(stringr)
library(tm)
DB<-dbConnect(dbDriver("MySQL"), dbname="api", user="DB_K", password="1q2w3e4r!", host="localhost")
Query<-dbGetQuery(DB, "SELECT * FROM twitter")
team<-dbGetQuery(DB, "SELECT team FROM keyword")
Query$contents<-paste(team, Query$contents)
docs.df<-data.frame(doc_id=Query$twit_id, text=Query$contents)
docs.ds<-DataframeSource(docs.df)
docs.cp<-Corpus(docs.ds)
docs.cp<-tm_map(docs.cp,stemDocument)
docs.cp<-tm_map(docs.cp,stripWhitespace)
docs.cp<-tm_map(docs.cp,removePunctuation)
docs.cp<-tm_map(docs.cp,removeNumbers)
docs.cp<-tm_map(docs.cp, tolower) 
docs.cp<-tm_map(docs.cp,removeWords,stopwords("english"))
docs.dtm<-DocumentTermMatrix(docs.cp)
docs.dtm = removeSparseTerms(docs.dtm, as.numeric(0.99))
x<-as.vector(unlist(unique(team)))
x<-str_replace_all(x, "\\W", " ")
x<-str_to_lower(x)
x[2]<-"aston"
x[4]<-"brighton"
x[7]<-"crystal"
x[9]<-"leicester"
x[11]<-"manchester"
x[13]<-"newcastle"
x[14]<-"norwich"
x[15]<-"sheffield"
x[16]<-"southampton"
x[17]<-"tottenham"
x[19]<-"west"
x[20]<-"wolverhampton"
t<-1
for(i in 1:20){
  t[i]<-x[i]
}
library(NLP)
library(openNLP)
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

str <-  tagPOS(docs.dtm)
prp <- str_extract_all(str$POStagged,"\\w+/NNP\\$?")
x<-str_replace(unlist(prp), "/NNP\\$?", "")
findAssocs(x, t,  0.01)
dbDisconnect(DB)