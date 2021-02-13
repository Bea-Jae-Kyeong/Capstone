library(DBI)
library(RMySQL)
library(dbConnect)
library(dplyr)
library(stringr)
library(openNLP)
library(NLP)
library(tm)
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
keywordsf<-1
keywordst<-1
DB<-dbConnect(dbDriver("MySQL"), dbname="api", user="DB_K", password="1q2w3e4r!", host="localhost")
Query<-dbGetQuery(DB, "SELECT * FROM twitter")
Article<-dbGetQuery(DB, "SELECT * FROM articledata")
keyword<-dbGetQuery(DB, "SELECT * FROM keyword")
pyth<-read.csv("kwd_art.csv")
pyth<-unlist(pyth$x)
for(i in 1:length(pyth)){
  keywords[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$last_name
  keywordsf[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$first_name
  keywordst[i]<-(keyword %>% filter(keyword$keyword_no==pyth[i]))$team
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
corr1<-findAssocs(docs.dtm, t[2],  0.01)[[1]]

corr1 <- cbind(read.table(text = names(corr1), stringsAsFactors = FALSE), corr1)
corr2<-findAssocs(docs.dtm, t[3],  0.01)[[1]]

corr2 <- cbind(read.table(text = names(corr2), stringsAsFactors = FALSE), corr2)

# join them together
two_terms_corrs <- full_join(corr1, corr2)

# gather for plotting
library(tidyr)
two_terms_corrs_gathered <- gather(two_terms_corrs, term, correlation, corr1:corr2)

# insert the actual terms of interest so they show up on the legend
two_terms_corrs_gathered$term <- ifelse(two_terms_corrs_gathered$term  == "corr1", t[2], t[3])


# Draw the plot...

require(ggplot2)
ggplot(two_terms_corrs_gathered, aes(x = "Twitter association by keyword", y = correlation, colour =  term ) ) +
  geom_point(size = 3) +
  ylab(paste0("Association each with twitter matrix and the terms ", "\"", t[2],  "\"", " or ",  "\"", t[3], "\"")) +
  theme_bw() +
  theme(axis.text.x = element_text(angle = 90, hjust = 1, vjust = 0.5))