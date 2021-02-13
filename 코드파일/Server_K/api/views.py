# from rest_framework import viewsets
#
# from api.models import SkySportsData
# from api.serializers import TestSerializer1
#
#
# class TestViewSet1(viewsets.ModelViewSet):
#     # queryset = SkySportsData.objects.all()
#     queryset = SkySportsData.objects.raw('SELECT id,title,subtitle FROM api_skysportsdata')
#     serializer_class = TestSerializer1

from rest_framework import status, generics
from rest_framework import renderers
from rest_framework.decorators import api_view
from rest_framework.response import Response
from api.models import Articledata
from api.models import Userdata,Keyword,NewsReg
from django.db import connection
from api.serializers import ArticleSerializer,ArticleSerializer2,ArticleSerializer3,UserSerializer,KeywordSerializer

import datetime
# keyword 테이블의 full_name으로 keyword맞는거 찾은후, article 테이블에서 그 keyword를 포함하는 기사 모두 가져오기
class article_fetch(generics.ListAPIView):
    model = Articledata
    serializer_class = ArticleSerializer

    def get_queryset(self):
        query_params = self.request.query_params
        keyword = query_params.get('kwd',None)
        date = query_params.get('date',None)

        queryset = Articledata.objects.raw("select * from api.articledata,(select keyword_no from api.keyword where full_name = %s) s where DATE(articledata.date) = %s and " +
                                           "(keyword1 = s.keyword_no or keyword2 = s.keyword_no or keyword3 = s.keyword_no or keyword4 = s.keyword_no or keyword5 = s.keyword_no or "
                                           "keyword6 = s.keyword_no or keyword7 = s.keyword_no or keyword8 = s.keyword_no or keyword9 = s.keyword_no or keyword10 = s.keyword_no) "
                                           "ORDER BY articledata.date DESC",
            [keyword,date])


        return queryset

class article_fetch2(generics.ListAPIView):
    model = Articledata
    serializer_class = ArticleSerializer2

    def get_queryset(self):
        query_params = self.request.query_params
        news_id = query_params.get('news_id',None)
        user_id = query_params.get('user_id',None)

        queryset = Articledata.objects.raw("select * from api.articledata where news_id = %s;", [news_id])

        q = Articledata.objects.filter(news_id = news_id)        #queryset
        q = q.get(news_id = news_id)
        click = q.click_no
        click += 1

        Articledata.objects.filter(news_id = news_id).update(click_no = click)

        now = datetime.datetime.now()
        nowDatetime = now.strftime('%Y-%m-%d %H:%M:%S')

        queryset1 = Userdata.objects.filter(user_id = user_id)
        u0 = queryset1.get(user_id = user_id)

        queryset2 = Articledata.objects.filter(news_id = news_id)
        n0 = queryset2.get(news_id = news_id)

        NewsReg.objects.create(user_id = user_id, news_id = news_id, date = nowDatetime).save()

        return queryset

class save_userdata(generics.ListAPIView):
    model = Userdata
    serializer_class = UserSerializer

    def get_queryset(self):
        query_params = self.request.query_params
        id = query_params.get('user_id',None)
        keyword1 = query_params.get('keyword1',None)
        keyword2 = query_params.get('keyword2', None)
        keyword3 = query_params.get('keyword3', None)

        queryset1 = Keyword.objects.filter(full_name = keyword1)
        k1 = queryset1.get(full_name = keyword1)

        queryset2 = Keyword.objects.filter(full_name = keyword2)
        k2 = queryset2.get(full_name = keyword2)

        queryset3 = Keyword.objects.filter(full_name = keyword3)
        k3 = queryset3.get(full_name = keyword3)

        Userdata(user_id=id, user_keyword1 = k1 , user_keyword2 = k2, user_keyword3 = k3).save()


class article_fetch3(generics.ListAPIView):
    model = Articledata
    serializer_class = ArticleSerializer

    def get_queryset(self):
        query_params = self.request.query_params
        keyword = query_params.get('kwd', None)
        date = query_params.get('date', None)

        queryset = Articledata.objects.raw(
            "select * from api.articledata,(select keyword_no from api.keyword where full_name = %s) s where DATE(articledata.date) = %s and " +
            "(keyword1 = s.keyword_no or keyword2 = s.keyword_no or keyword3 = s.keyword_no or keyword4 = s.keyword_no or keyword5 = s.keyword_no or "
            "keyword6 = s.keyword_no or keyword7 = s.keyword_no or keyword8 = s.keyword_no or keyword9 = s.keyword_no or keyword10 = s.keyword_no) "
            "ORDER BY field(keyword1,s.keyword_no) DESC, field(keyword2,s.keyword_no) DESC, field(keyword3, s.keyword_no) DESC, field(keyword4, s.keyword_no) DESC, field(keyword5, s.keyword_no) DESC",
            [keyword, date])

        return queryset


class article_fetch4(generics.ListAPIView):
    model = Articledata
    serializer_class = ArticleSerializer

    def get_queryset(self):
        queryset = Articledata.objects.raw("SELECT * FROM api.articledata ORDER BY articledata.date DESC LIMIT 30")

        return queryset

class keyword_fetch(generics.ListAPIView):
    model = Keyword
    serializer_class = KeywordSerializer

    def get_queryset(self):
        query_params = self.request.query_params
        name = query_params.get('full_name', None)
        name = '%' + name + '%'
        queryset = Keyword.objects.raw("SELECT * FROM (select * from api.keyword LIMIT 546) k where k.full_name like %s", [name])

        return queryset

class article_fetch5(generics.ListAPIView):
    model = Articledata
    serializer_class = ArticleSerializer

    def get_queryset(self):
        query_params = self.request.query_params
        kwd = query_params.get('kwd',None)
        queryset = Articledata.objects.raw( "select * from api.articledata,(select keyword_no from api.keyword where full_name = %s) s where " +
            "keyword1 = s.keyword_no or keyword2 = s.keyword_no or keyword3 = s.keyword_no or keyword4 = s.keyword_no or keyword5 = s.keyword_no or "
            "keyword6 = s.keyword_no or keyword7 = s.keyword_no or keyword8 = s.keyword_no or keyword9 = s.keyword_no or keyword10 = s.keyword_no "
            "ORDER BY articledata.date DESC, field(keyword1,s.keyword_no) DESC, field(keyword2,s.keyword_no) DESC, field(keyword3, s.keyword_no) DESC, "
            "field(keyword4, s.keyword_no) DESC, field(keyword5, s.keyword_no) DESC LIMIT 30", [kwd])

        return queryset


class recommend_fetch(generics.ListAPIView):
    model = Articledata
    serializer_class = ArticleSerializer

    def get_queryset(self):
        query_params = self.request.query_params
        user_id = query_params.get('user_id',None)
        queryset = Articledata.objects.raw("select * from api.articledata,(select news_id from api.rec_news where rec_news.user_id = %s) r "
                                           "where articledata.news_id = r.news_id", [user_id])

        return queryset

#
# def article_click(request,self):
#
#     if request.method == 'POST':
#         news_id=request.POST.get('news_id')
#         click = request.POST.get('click_no')
#         cursor = connection.cursor()
#         cursor.execute("UPDATE api.articledata SET click_no= %s WHERE news_id = %s", [click,news_id])
#         row = cursor.fetchone()
#
#
# class article_click2(generics.UpdateAPIView):
#     model = Articledata;
#     serializer_class = ArticleSerializer3
#
#     def update(self, request, *args, **kwargs):
#         query_params = self.request.query_params
#         id = query_params.get('news_id', None)
#         click = query_params.get('click_no', None)
#         Articledata.objects.filter(news_id=id).update(click_no=click)
#



# class article_main(generics.ListAPIView, generics.ListCreateAPIView):
#     queryset = Articledata.objects.all()
#     serializer_class = ArticleSerializer
#
#
# class article_detail(generics.RetrieveAPIView):
#     queryset = Articledata.objects.all()
#     serializer_class = ArticleSerializer
#
#
# class article_update(generics.UpdateAPIView):
#     queryset = Articledata.objects.all()
#     serializer_class = ArticleSerializer
#
#
# class article_delete(generics.DestroyAPIView):
#     queryset = Articledata.objects.all()
#     serializer_class = ArticleSerializer
#
#
# class article_get_with_title(generics.ListCreateAPIView):
#     model = Articledata
#     serializer_class = ArticleSerializer
#
#     # Show all of the PASSENGERS in particular WORKSPACE
#     # or all of the PASSENGERS in particular AIRLINE
#     def get_queryset(self):
#         queryset = Articledata.objects.all()
#         title2 = self.request.query_params.get('title')
#
#         if title2:
#             queryset = queryset.filter(title=title2)
#
#         return queryset

# @api_view(['GET', 'POST'])
# def skysports_list(request, format=None):
#     """
#     List all code snippets, or create a new snippet.
#     """
#     if request.method == 'GET':
#         skysports = SkySportsData.objects.all()
#         serializer = SkySportsSerializer(skysports, many=True)
#         return Response(serializer.data)
#     elif request.method == 'POST':
#         serializer = SkySportsSerializer(data=request.data)
#         if serializer.is_valid():
#             serializer.save()
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
#
#
# @api_view(['GET', 'PUT', 'DELETE'])
# def skysports_detail(request, pk, format=None):
#     """
#     Retrieve, update or delete a code snippet.
#     """
#     try:
#         skysports = SkySportsData.objects.get(pk=pk)
#     except SkySportsData.DoesNotExist:
#         return Response(status=status.HTTP_404_NOT_FOUND)
#
#     if request.method == 'GET':
#         serializer = SkySportsSerializer(skysports)
#         return Response(serializer.data)
#
#     elif request.method == 'PUT':
#         serializer = SkySportsSerializer(skysports, data=request.data)
#         if serializer.is_valid():
#             serializer.save()
#             return Response(serializer.data)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
#
#     elif request.method == 'DELETE':
#         skysports.delete()
#         return Response(status=status.HTTP_204_NO_CONTENT)
