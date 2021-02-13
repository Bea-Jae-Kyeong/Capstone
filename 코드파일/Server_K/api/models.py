from django.db import models
from datetime import datetime

import django.forms
from django.forms import DateTimeField

now = datetime.now()

# DATE_INPUT_FORMATS = ['%Y-%m-%d %H:%M:%S']
# class Articledata(models.Model):
#     source = models.CharField(max_length=1000, default='source')
#     source_team = models.CharField(max_length=30,default='source_team')
#     source_url = models.CharField(max_length=200,default='source_url')
#     title = models.CharField(max_length=400,default='title')
#     content = models.TextField(default='content1')
#     img_src = models.CharField(max_length=400, default='img_src1')
#     date = models.DateTimeField(default='2019-01-01 00:00:00')

class Articledata(models.Model):
    news_id = models.AutoField(primary_key=True)
    source = models.CharField(max_length=30, blank=True, null=True)
    source_team = models.CharField(max_length=30, blank=True, null=True)
    url = models.CharField(max_length=200, blank=True, null=True)
    title = models.CharField(max_length=400, blank=True, null=True)
    contents = models.TextField(blank=True, null=True)
    img_url = models.CharField(max_length=400, blank=True, null=True)
    date = models.DateTimeField(blank=True, null=True)
    click_no = models.IntegerField(blank=True, null=True)       # click number
    keyword1 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword1', blank=True, null=True, related_name='kwd1')
    keyword2 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword2', blank=True, null=True, related_name='kwd2')
    keyword3 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword3', blank=True, null=True, related_name='kwd3')
    keyword4 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword4', blank=True, null=True, related_name='kwd4')
    keyword5 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword5', blank=True, null=True, related_name='kwd5')
    keyword6 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword6', blank=True, null=True, related_name='kwd6')
    keyword7 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword7', blank=True, null=True, related_name='kwd7')
    keyword8 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword8', blank=True, null=True, related_name='kwd8')
    keyword9 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword9', blank=True, null=True, related_name='kwd9')
    keyword10 = models.ForeignKey('Keyword', models.DO_NOTHING, db_column='keyword10', blank=True, null=True, related_name='kwd10')
    rec_score = models.FloatField(blank=True, null=True)        # recommendation score
    rec_percent = models.CharField(max_length=45, blank=True, null=True)        #recommendation percentage
    rec_count = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'articledata'



class Keyword(models.Model):
    keyword_no = models.IntegerField(primary_key=True)
    team = models.CharField(max_length=45, blank=True, null=True)
    first_name = models.CharField(max_length=45, blank=True, null=True)
    last_name = models.CharField(max_length=45, blank=True, null=True)
    player_no = models.IntegerField(blank=True, null=True)
    views_no = models.IntegerField(blank=True, null=True)   # == click_no
    weight = models.FloatField(blank=True, null=True)
    full_name = models.CharField(max_length=45, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'keyword'



class NewsReg(models.Model):
    reg_id = models.AutoField(primary_key=True)
    user = models.ForeignKey('Userdata', models.DO_NOTHING, blank=True, null=True)
    news = models.ForeignKey(Articledata, models.DO_NOTHING, blank=True, null=True)
    date = models.DateTimeField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'news_reg'





class RecNews(models.Model):
    reco_id = models.AutoField(primary_key=True)
    user = models.ForeignKey('Userdata', models.DO_NOTHING, blank=True, null=True)
    news = models.ForeignKey(Articledata, models.DO_NOTHING, blank=True, null=True)
    rec_point = models.IntegerField(blank=True, null=True)
    rec_reg = models.IntegerField(blank=True, null=True)
    date = models.DateTimeField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'rec_news'



class Twitter(models.Model):
    twit_id = models.AutoField(primary_key=True)
    team = models.CharField(max_length=45, blank=True, null=True)
    contents = models.TextField(blank=True, null=True)
    date = models.DateTimeField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'twitter'


class Userdata(models.Model):
    user_id = models.CharField(primary_key=True,max_length=100)
    user_keyword1 = models.ForeignKey(Keyword, models.DO_NOTHING, db_column='user_keyword1', blank=True, null=True, related_name='usr_kwd1')
    user_keyword2 = models.ForeignKey(Keyword, models.DO_NOTHING, db_column='user_keyword2', blank=True, null=True, related_name='usr_kwd2')
    user_keyword3 = models.ForeignKey(Keyword, models.DO_NOTHING, db_column='user_keyword3', blank=True, null=True, related_name='usr_kwd3')
    rec_keyword1 = models.ForeignKey(Keyword, models.DO_NOTHING, db_column='rec_keyword1', blank=True, null=True, related_name='rec_kwd1')
    rec_keyword2 = models.ForeignKey(Keyword, models.DO_NOTHING, db_column='rec_keyword2', blank=True, null=True, related_name='rec_kwd2')
    rec_keyword3 = models.ForeignKey(Keyword, models.DO_NOTHING, db_column='rec_keyword3', blank=True, null=True, related_name='rec_kwd3')
    rec_keyword4 = models.ForeignKey(Keyword, models.DO_NOTHING, db_column='rec_keyword4', blank=True, null=True, related_name='rec_kwd4')
    rec_keyword5 = models.ForeignKey(Keyword, models.DO_NOTHING, db_column='rec_keyword5', blank=True, null=True, related_name='rec_kwd5')
    rec_keyword6 = models.ForeignKey(Keyword, models.DO_NOTHING, db_column='rec_keyword6', blank=True, null=True, related_name='rec_kwd6')

    class Meta:
        managed = False
        db_table = 'userdata'




