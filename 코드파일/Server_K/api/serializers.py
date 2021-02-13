from rest_framework import serializers
from api.models import Articledata,Userdata,Keyword,RecNews


class ArticleSerializer(serializers.ModelSerializer):
    class Meta:
        model = Articledata
        fields = ('news_id','source','title','img_url','date')

class ArticleSerializer2(serializers.ModelSerializer):
    class Meta:
        model = Articledata
        fields = ('news_id','source','title','img_url','date','contents','url','click_no')

class ArticleSerializer3(serializers.ModelSerializer):
    class Meta:
        model = Articledata
        fields = {'news_id','click_no'}

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = Userdata
        fields = {'user_id','user_keyword1','user_keyword2','user_keyword3'}

class KeywordSerializer(serializers.ModelSerializer):
    class Meta:
        model = Keyword
        fields = '__all__'
