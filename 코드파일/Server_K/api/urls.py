from django.conf.urls import url, include
from rest_framework import routers
from django.urls import path
from api import views
from rest_framework.urlpatterns import format_suffix_patterns

# router = routers.DefaultRouter()
# router.register(r'test1', views.skysports_list)
#
# app_name = 'api'
urlpatterns = [
    # url(r'^', include(router.urls)),
    url(r'api/article_get_with_keyword/$', views.article_fetch.as_view(), name='article_get_with_keyword_latest'),
    url(r'api/article_get_with_keyword_relevance/$', views.article_fetch3.as_view(), name='article_get_with_keyword_relevance'),
    url(r'api/article_get_with_id/$', views.article_fetch2.as_view(), name='article_get_detail_with_news_id'),
    url(r'api/save_userdata/$', views.save_userdata.as_view(), name='save_userdata'),
    url(r'api/article_get_latest',views.article_fetch4.as_view(), name='get_latest'),
    url(r'api/keyword_get/$',views.keyword_fetch.as_view(), name="get_keywords"),
    url(r'api/article_get_with_keyword_no_date/$',views.article_fetch5.as_view(),name='get_with_keyword_no_Date'),
    url(r'api/article_recommend_get/$',views.recommend_fetch.as_view(),name='get_article_recommend')
    # url(r'api/article_set_click_with_id$', views.article_click, name='article_click'),
    #
    # url(r'api/skysports/(?P<pk>\d+)/$', views.article_detail.as_view(), name='skysports_detail'),
    # url(r'api/skysports/(?P<pk>\d+)/update$', views.article_update.as_view(), name='skysports_update'),
    # url(r'api/skysports/(?P<pk>\d+)/delete$', views.article_delete.as_view(), name='skysports_delete'),
    # url(r'api/skysports_get_with_title/$', views.article_get_with_title.as_view(), name='get_with_title'),
    # url(r'^api-v1', include('rest_framework.urls', namespace='rest_framework_category'))
]

urlpatterns = format_suffix_patterns(urlpatterns)
