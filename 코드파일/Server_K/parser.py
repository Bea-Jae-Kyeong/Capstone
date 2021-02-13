# parser.py
import requests
from bs4 import BeautifulSoup
import datetime
from datetime import datetime
now = datetime.now()
from multiprocessing import Pool
import time
import os
import sys
import pandas as pd
import subprocess
import konlp
# Python이 실행될 때 DJANGO_SETTINGS_MODULE이라는 환경 변수에 현재 프로젝트의 settings.py파일 경로를 등록합니다.
import rpy2
import rpy2.robjects.packages as rpackages
utils = rpackages.importr('utils')

utils.chooseCRANmirror(ind=1) # select the first mirror in the list
#packnames = ('NLP')
from rpy2.robjects.vectors import StrVector
#utils.install_packages(StrVector(packnames))
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "Server_K.settings")

# 이제 장고를 가져와 장고 프로젝트를 사용할 수 있도록 환경을 만듭니다.
import django
django.setup()
from api.models import Articledata
import pymysql

fileDir = os.path.dirname(os.path.realpath('__file__'))
source_team = ['Arsenal', 'Aston villa', 'Bournemouth', 'Brighton & Hove albion', 'Burnley', 'Chelsea',
                   'Crystal Palace', 'Everton', 'Leicester City','Liverpool', 'Manchester City', 'Manchester United', 'Newcastle United', 'Norwich City',
                   'Sheffield United', 'Southampton', 'Tottenham Hotspur', 'Watford', 'West Ham United','Wolverhampton Wanderers']


def month_conversion(month):
    if month == 'January' or month == "Jan":
        month = '01'
    elif month == 'February' or month == "Feb":
        month = '02'
    elif month == 'March' or month ==  "Mar":
        month = '03'
    elif month == 'April' or month == "Apr":
        month = '04'
    elif month == 'May' or month == "May":
        month = '05'
    elif month == 'June' or month == "Jun":
        month = '06'
    elif month == 'July' or month == "Jul":
        month = '07'
    elif month == 'August' or month == "Aug":
        month = '08'
    elif month == 'September' or month == "Sep":
        month = '09'
    elif month == 'October' or month == "Oct":
        month = '10'
    elif month == 'November' or month == "Nov":
        month = '11'
    elif month == 'December' or month == "Dec":
        month = '12'

    return month

def team_select(team_url):
    team_index = 0
    if "arsenal" in team_url:
        team_index = 0
    elif "aston-villa" in team_url:
        team_index = 1
    elif ("afc-bournemouth" in team_url) or ("bournemouth" in team_url):
        team_index = 2
    elif ("brighton-and-hove-albion" in team_url) or ("brighton-hove-albion" in team_url) or ("brighton" in team_url):
        team_index = 3
    elif "burnley" in team_url:
        team_index = 4
    elif "chelsea" in team_url:
        team_index = 5
    elif ("crystal-palace" in team_url) or ("crystalpalace" in team_url) or ("crystal" in team_url):
        team_index = 6
    elif "everton" in team_url:
        team_index = 7
    elif ("leicester-city" in team_url) or ("leicestercity" in team_url) or ("leicester" in team_url):
        team_index = 8
    elif "liverpool" in team_url:
        team_index = 9
    elif ("manchester-city" in team_url) or ("manchestercity" in team_url):
        team_index = 10
    elif ("manchester-united" in team_url) or ("manchesterunited" in team_url):
        team_index = 11
    elif ("newcastle-united" in team_url) or ("newcastle" in team_url):
        team_index = 12
    elif ("norwich-city" in team_url) or ("norwich" in team_url):
        team_index = 13
    elif ("sheffield-united" in team_url) or ("sheffield" in team_url):
        team_index = 14
    elif "southampton" in team_url:
        team_index = 15
    elif ("tottenham-hotspur" in team_url) or ("tottenham" in team_url):
        team_index = 16
    elif "watford" in team_url:
        team_index = 17
    elif ("west-ham-united" in team_url) or ("westham" in team_url) or ("west-ham" in team_url):
        team_index = 18
    elif ("wolverhampton-wanderers" in team_url) or ("wolverhampton" in team_url) or ("wolves" in team_url):
        team_index = 19
    return team_index




def parse_mirror(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'Mirror/mirror_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("mirror_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = 'Mirror'
    headers = {'User-Agent': 'Chrome/77.0.3865.120'}

    req = requests.get(team_url, headers=headers)  # url에 요청
    html = req.text  # text 뽑아냄
    soup = BeautifulSoup(html, 'html.parser')  # html 형식으로 가져옴
    original_links = soup.select('a.headline')  # 각 news 링크 가져옴
    links = []
    for i, val in enumerate(original_links):
        link_temp = val.get('href')
        links.append(link_temp)

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)

    # 각 링크 돌아다니며 제목, 부제목, 내용, 날짜 수집
    for link in links:
        # 같은팀에서 중복된 url이면 for문 종료
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("skysports index error")

        req = requests.get(link, headers=headers)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        title = ''
        contents = ''
        img_src = ''
        date = ''

        # 제목
        if soup.select('h1.section-theme-background-indicator.publication-font'):
            title = soup.select('h1.section-theme-background-indicator.publication-font')[0].text

        # 기사 본문
        if soup.select('div.article-body > p'):
            content_arr = soup.select('div.article-body > p')

        if content_arr.__sizeof__() is 0:
            continue

        for content in content_arr:
            contents = contents + content.text + '\n\n'

        # 이미지
        if soup.select('div.article-body div.mod-image > img'):
            img_src = soup.select('div.article-body div.mod-image > img')[0].get('data-src')

        # 업데이트 날짜 - 2019-01-01 13:00:00
        if soup.select('body > main > article > div.byline > div.article-information > ul > li > time'):
            if soup.select('body > main > article > div.byline > div.article-information > ul > li > time')[0].get(
                    'datetime'):
                datetemp = soup.select('body > main > article > div.byline > div.article-information > ul > li > time')[
                    0].get('datetime').split('T', maxsplit=1)
            elif soup.select('body > main > article > div.byline > div.article-information > ul > li > time')[0].get(
                    'date-updated'):
                datetemp = soup.select('body > main > article > div.byline > div.article-information > ul > li > time')[
                    0].get('date-updated').split('T', maxsplit=1)
            time = datetemp[1].split('Z', maxsplit=1)
            date = datetemp[0] + ' ' + time[0]

        print(link)
        data = [source, source_team[team_select(team_url)], link, title, contents, img_src, date]
        final_data.append(data)
    f.close()
    return final_data


def parse_express(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'Express/express_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("express_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = 'Express'

    req = requests.get(team_url)
    html = req.text
    soup = BeautifulSoup(html, 'html.parser')
    original_link = soup.select('div.clear.clearfix a')

    links = []
    for val in original_link:
        if "football" in val.get('href'):
            if "/stat" in val.get('href'):
                continue
            links.append("https://www.express.co.uk" + val.get('href'))

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)

    for link in links:
        # 같은팀에서 중복된 url이면 for문 종료
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("skysports index error")

        req = requests.get(link)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')

        title = ''
        contents = ''
        img_src = ''
        date = ''

        # 기사 title
        if soup.select('header.clearfix > h1'):
            if 'news LIVE' in soup.select('header.clearfix > h1')[0].text:
                continue
            title = soup.select('header.clearfix > h1')[0].text.strip()

        # 기사 본문
        content_arr = soup.select('div.clearfix.hR.new-style p')

        if content_arr.__sizeof__() is 0:
            continue

        for content in content_arr:
            contents = contents + content.text + '\n\n'

        # image
        if soup.select("div.ctx_content.p402_premium source"):
            img_src = soup.select("div.ctx_content.p402_premium source")[0].get('srcset')

        # date
        if soup.select('.single-article div.dates > meta'):
            temp = soup.select('.single-article div.dates > meta')[0].get('content').split('T', maxsplit=2)
            datetemp = temp[0]
            time = temp[1].split('Z', maxsplit=2)
            date = datetemp + ' ' + time[0]
            date = datetime.strptime(date, '%Y-%m-%d %H:%M:%S')
        print(link)
        data = [source, source_team[team_select(team_url)], link, title, contents, img_src, date]
        final_data.append(data)
    f.close()
    return final_data


def parse_goal(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'Goal/goal_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("goal_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = 'Goal'

    req = requests.get(team_url)  # url에 요청
    html = req.text  # text 뽑아냄
    soup = BeautifulSoup(html, 'html.parser')  # html 형식으로 가져옴
    original_links = soup.select(
        'div.page-container > div.widget-news-archive.newsarchive-card-group > table.widget-news-card.card-type-article > tr > td > a,'
        'div.page-container > div.widget-news-archive.newsarchive-card-group > table.widget-news-card.card-type-featureArticle > tr > td > a')  # 각 news 링크 가져옴
    links = []
    for i, val in enumerate(original_links):
        # category href 삭제
        is_category = ''
        if val.get('class'):
            is_category = val.get('class')[0]
        if is_category is not '':
            if "widget-news-card__category" == is_category:
                continue
        if "www.goal.com" in val.get('href'):
            links.append(val.get('href'))
            continue
        # href 삽입
        link_temp = "https://www.goal.com" + val.get('href')
        links.append(link_temp)

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)

    # 각 링크 돌아다니며 제목, 부제목, 내용, 날짜 수집
    for link in links:
        # 같은팀에서 중복된 url이면 for문 종료
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("skysports index error")

        req = requests.get(link)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        title = ''
        contents = ''
        img_src = ''
        date = ''

        # 제목
        if soup.select('h1.article-headline'):
            title = soup.select('h1.article-headline')[0].text.strip()
        else:
            continue

        # 기사 본문
        if soup.select('div.teaser , div.body > p'):
            content_arr = soup.select('div.teaser , div.body > p')

        if content_arr.__sizeof__() is 0:
            continue

        for content in content_arr:
            contents = contents + content.text + '\n\n'

        # 이미지
        if soup.select('div.page-container > div.content > div > div.picture > div.image-wrapper'):
            img_src = \
            soup.select('div.page-container > div.content > div > div.picture > div.image-wrapper > img')[0].get(
                'src').split('?', maxsplit=1)[0]
        # print(img_src)

        # 업데이트 날짜 - 2019-01-01 13:00:00
        if soup.select('div.page-container > div.content > div > div.publish-date > span.time'):
            time = soup.select('div.page-container > div.content > div > div.publish-date > span.time')[0].text.split(
                ' ', maxsplit=3)
            datetemp = soup.select('div.page-container > div.content > div > div.publish-date > span.date')[
                0].text.split('/', maxsplit=2)
            year = datetemp[2].strip()
            month = datetemp[1].strip()
            day = datetemp[0].strip()
            second = str(now.second)
            date = year + '-' + month + '-' + day + ' ' + time[1] + ':' + second
            date = datetime.strptime(date, '%Y-%m-%d %H:%M:%S')

        print(link)
        data = [source, source_team[team_select(team_url)], link, title, contents, img_src, date]
        final_data.append(data)
    f.close()
    return final_data


def parse_telegraph(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'Telegraph/telegraph_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("telegraph_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = 'Telegraph'

    req = requests.get(team_url)
    html = req.text
    soup = BeautifulSoup(html, 'html.parser')
    original_link = soup.select('a.list-headline__link.u-clickable-area__link')

    links = []
    for val in original_link:
        if "football" in val.get('href'):
            links.append('https://www.telegraph.co.uk' + val.get('href'))

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)

    for link in links:
        # 같은팀에서 중복된 url이면 for문 종료
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("skysports index error")
        req = requests.get(link)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')

        title = ''
        contents = ''
        img_src = ''
        date = ''

        # 기사 title
        if soup.select('.headline__heading'):
            title = soup.select('.headline__heading')[0].text.strip()

        # 기사 본문
        content_arr = soup.select(
            'div.articleBodyText.section > div.article-body-text.component > div.component-content > p')

        if content_arr.__sizeof__() is 0:
            continue

        for content in content_arr:
            contents = contents + content.text + '\n\n'

        # image
        if soup.select("div.hero-area-wrapper meta:nth-child(3)"):
            img_src = soup.select("div.hero-area-wrapper meta:nth-child(3)")[0].get('content')

        # date
        if soup.select('div.article-author__meta > span > span > time'):
            temp = soup.select('div.article-author__meta > span > span > time')[0].text
            date_temp = temp.split(' ', maxsplit=4)
            year = date_temp[2]
            month = month_conversion(date_temp[1])
            day = date_temp[0]
            if len(date_temp[0]) == 1:
                day = '0' + day

            time_temp = date_temp[4].split(':', maxsplit=1)
            hour = time_temp[0]
            _time = time_temp[1]
            minute = _time[0] + _time[1]
            m = _time[2] + _time[3]
            if (m == 'pm'):
                hour = 12 + int(hour)
                if (hour == 24):
                    hour = 00
            hour = str(hour)
            second = str(now.second)
            date = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second

            date = datetime.strptime(date, '%Y-%m-%d %H:%M:%S')
        print(link)
        data = [source, '', link, title, contents, img_src, date]
        final_data.append(data)
    f.close()
    return final_data


def parse_guardian(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'Guardian/guardian_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("guardian_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = 'The Guardian'

    req = requests.get(team_url)  # url에 요청
    html = req.text  # text 뽑아냄
    soup = BeautifulSoup(html, 'html.parser')  # html 형식으로 가져옴
    original_links = soup.select('.u-faux-block-link__overlay.js-headline-text')  # 각 news 링크 가져옴
    links = []
    for i, val in enumerate(original_links):
        link_temp = val.get('href')
        if ('/football/' not in link_temp) or ('/live/' in link_temp) or ('/gallery/' in link_temp):
            continue
        links.append(link_temp)

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)

    # 각 링크 돌아다니며 제목, 부제목, 내용, 날짜 수집
    for link in links:
        # 같은팀에서 중복된 url이면 for문 종료
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("skysports index error")

        req = requests.get(link)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        title = ''
        contents = ''
        img_src = ''
        date = ''

        # 제목
        if soup.select('#article > div > div > div > header > div > div > div > h1'):
            title = soup.select('#article > div > div > div > header > div > div > div > h1')[0].text.strip()
        else:
            continue

        # 기사 본문
        if soup.select('.content__article-body.from-content-api.js-article__body > h2,'
                       ' .content__article-body.from-content-api.js-article__body > p'):
            content_arr = soup.select('.content__article-body.from-content-api.js-article__body > h2, '
                                      '.content__article-body.from-content-api.js-article__body > p')

        if content_arr.__sizeof__() is 0:
            continue

        for content in content_arr:
            contents = contents + content.text + '\n\n'

        # 이미지
        if soup.select('#img-1 > a > div > picture > img'):
            img_src = soup.select('#img-1 > a > div > picture > img')[0].get('src')

        # 업데이트 날짜
        if soup.select('#article > div > div > div > header > div > div > p > time'):
            date = soup.select('#article > div > div > div > header > div > div > p > time')[0].get('datetime')
            datetemp1 = date.split('T', maxsplit=1)
            datetemp2 = datetemp1[1].split('+', maxsplit=1)
            date = datetemp1[0] + ' ' + datetemp2[0]
            date = datetime.strptime(date, '%Y-%m-%d %H:%M:%S')

        print(link)
        data = [source, source_team[team_select(team_url)], link, title, contents, img_src, date]
        final_data.append(data)
    f.close()
    return final_data


def parse_dailymail(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'DailyMail/dailymail_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("dailymail_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = 'DailyMail'
    req = requests.get(team_url)  # url에 요청
    html = req.text  # text 뽑아냄
    soup = BeautifulSoup(html, 'html.parser')  # html 형식으로 가져옴
    original_links = soup.select('.football-team-news > div > a')  # 각 news 링크 가져옴
    links = []
    for i, val in enumerate(original_links):
        links.append("https://www.dailymail.co.uk" + val.get('href'))

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)

    # 각 링크 돌아다니며 제목, 부제목, 내용, 날짜 수집
    for link in links:
        # 같은팀에서 중복된 url이면 for문 종료
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("skysports index error")

        req = requests.get(link)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        title = ''
        contents = ''
        img_src = ''
        date = ''

        # 제목
        if soup.select('#js-article-text > h2'):
            title = soup.select('#js-article-text > h2')[0].text.strip()

        # 기사 본문
        if soup.select('.mol-para-with-font'):
            content_arr = soup.select('.mol-para-with-font')

        if content_arr.__sizeof__() is 0:
            continue

        for content in content_arr:
            contents = contents + content.text + '\n\n'

        # 이미지
        if soup.select('.artSplitter.mol-img-group > div > div > img'):
            img_src = soup.select('.artSplitter.mol-img-group > div > div > img')[0].get('data-src')

        # 업데이트 날짜
        if soup.select('.article-timestamp.article-timestamp-updated > time'):
            datetemp = soup.select('.article-timestamp.article-timestamp-updated > time')[0].get('datetime').split('T', maxsplit=1)
            time = datetemp[1].split('+', maxsplit=1)
            date = datetemp[0] + ' ' + time[0]
            date = datetime.strptime(date, '%Y-%m-%d %H:%M:%S')
        else:
            date = now.strftime('%Y-%m-%d %H:%M:%S')

        print(link)
        data = [source, source_team[team_select(team_url)], link, title, contents, img_src, date]
        final_data.append(data)
    f.close()
    return final_data


def parse_sun(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'Sun/sun_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("sun_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = 'The Sun'

    req = requests.get(team_url)
    html = req.text
    soup = BeautifulSoup(html, 'html.parser')
    original_link = soup.select('a.teaser-anchor')

    links = []
    for val in original_link:
        if "live-stream" in val.get('href'):
            continue
        if "sport/football" in val.get('href'):
            links.append(val.get('href'))

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)

    for link in links:
        # 같은팀에서 중복된 url이면 for문 종료
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("skysports index error")

        req = requests.get(link)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')

        title = ''
        contents = ''
        img_src = ''
        date = ''

        # 기사 title
        if soup.select('.article__headline'):
            if 'news LIVE' in soup.select('.article__headline')[0].text:
                continue
            title = soup.select('.article__headline')[0].text.strip()

        # 기사 본문
        content_arr = soup.select('div.article__content > p')

        if content_arr.__sizeof__() is 0:
            continue

        for content in content_arr:
            contents = contents + content.text + '\n\n'

        # image
        if soup.select(
                '#main-content > section > div > div > article > div > div.article__content > figure.article__media > div > a > img'):
            img_src = soup.select(
                '#main-content > section > div > div > article > div > div.article__content > figure.article__media > div > a > img')[
                0].get('data-src')

        # date
        if soup.select('div.article__name-date > ul.article__time > li.article__published > span'):
            temp = soup.select(
                'div.article__name-date > ul.article__time > li.article__published > span.article__datestamp')[0].text
            # 23 Oct 2019,
            date_temp = temp.split(' ', maxsplit=2)
            _year = date_temp[2].split(',', maxsplit=1)
            year = _year[0]
            month = month_conversion(date_temp[1])
            day = date_temp[0]
            if len(date_temp[0]) == 1:
                day = '0' + day

            time = soup.select(
                'div.article__name-date > ul.article__time > li.article__published > span.article__timestamp')[0].text.strip('')
            time_temp = time.split(':', maxsplit=1)
            _hour = time_temp[0]
            hour = _hour.replace(" ", "")
            if len(hour) is 1:
                hour = '0' + hour
            minute = time_temp[1]
            second = str(now.second)
            date = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second

            date = datetime.strptime(date, '%Y-%m-%d %H:%M:%S')

        data = [source, source_team[team_select(team_url)], link, title, contents, img_src, date]
        if data[6] is "":
            continue
        final_data.append(data)
    f.close()
    return final_data


def parse_bbc(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'BBC/bbc_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("bbc_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = 'BBC'
    req = requests.get(team_url)
    html = req.text
    soup = BeautifulSoup(html, 'html.parser')
    original_links = soup.select('.layout__primary-col.layout__primary-col--1280 a.faux-block-link__overlay')
    links = []
    for val in original_links:
        link = val.get('href')
        if ("av/football" in link) or ("av" in link) or ("football" not in link):
            continue
        links.append('https://www.bbc.com' + link)

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)

    for link in links:

        # 같은팀에서 중복된 url이면 for문 종료
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("skysports index error")

        req = requests.get(link)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')

        title = ''
        contents = ''
        img_src = ''
        date = ''
        if soup.select('#responsive-story-page > article > h1'):
            title = soup.select('#responsive-story-page > article > h1')[0].text.strip()
        if title == '':
            continue

        # 기사 본문
        content_arr = soup.select('#story-body > p')
        for content in content_arr:
            contents = contents + content.text + '\n\n'

        if content_arr.__sizeof__() is 0:
            continue

        if contents == '':
            continue

        if soup.select('#story-body > figure > div > div > img'):
            if soup.select('#story-body > figure > div > div > img')[0].get('data-src'):
                img_src = soup.select('#story-body > figure > div > div > img')[0].get('data-src')
                img_src = img_src.replace("{width}{hidpi}", "976")
            else:
                img_src = soup.select('#story-body > figure > div > div > img')[0].get('src')

        if soup.select('#responsive-story-page > article > div.story__info.story-info.clearfix > div.story-info__list > ul > li.story-info__item.story-info__item--time > span > time > abbr'):
            date = soup.select('#responsive-story-page > article > div.story__info.story-info.clearfix > div.story-info__list > ul > li.story-info__item.story-info__item--time > span > time > abbr')[0].get('title')
            date_temp = date.split(' ', maxsplit=2)
            year = date_temp[2]
            month = month_conversion(date_temp[1])
            day = date_temp[0]
            if len(date_temp[0]) == 1:
                day = '0' + day
            hour = str(now.hour)
            minute = str(now.minute)
            second = str(now.second)
            date = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second
            date = datetime.strptime(date, '%Y-%m-%d %H:%M:%S')
        print(link)
        data = [source, source_team[team_select(team_url)], link, title, contents, img_src, date]
        final_data.append(data)
    f.close()
    return final_data


def parse_espn(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'ESPN/espn_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("espn_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = "ESPN"
    req = requests.get(team_url)
    html = req.text
    soup = BeautifulSoup(html, 'html.parser')
    temp_links = soup.select('a.external.realStory')
    original_links = soup.select('a.realStory')
    for i, _i in enumerate(temp_links):
        for j, _j in enumerate(original_links):
            if(_i.get('href') == _j.get('href')):
                original_links.pop(j)
    links = []
    for val in original_links:

        if "preview" in val.get('href'):
            continue
        links.append('https://www.espn.com' + val.get('href'))

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)

    for link in links:
        # 같은팀에서 중복된 url이면 for문 종료

        print(link)
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("espn index error")

        req = requests.get(link)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')

        title = ''
        contents = ''
        img_src = ''
        date = ''

        if soup.select('.article-header > h1'):
            title = soup.select('.article-header > h1')[0].text.strip()
        if title == '':
            continue

        content_arr = soup.select('.article-body p')

        if content_arr.__sizeof__() is 0:
            continue
        # 기사 본문
        for content in content_arr:
            contents = contents + content.text + '\n\n'

        if soup.select('.article-body > aside > figure > picture > source'):
            img_src = soup.select('.article-body > aside > figure > picture > source')[0].get('data-srcset').split(',',maxsplit=1)[0]

        if soup.select('div.article-body > div.article-meta > span'):
            datetemp = soup.select('div.article-body > div.article-meta > span')[0].get('data-date').split('T',maxsplit=1)
            time = datetemp[1].split('Z', maxsplit=1)
            date = datetemp[0] + ' ' + time[0]
            date = datetime.strptime(date, '%Y-%m-%d %H:%M:%S')

        data = [source, source_team[team_select(team_url)], link, title, contents, img_src, date]
        final_data.append(data)
    f.close()
    return final_data


def parse_skysports(team_url):
    # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 빼기
    filename = os.path.join(fileDir, 'SkySports/skysports_' + source_team[team_select(team_url)] + '.txt')
    line = ''
    try:
        # 읽기모드로 파일열기
        f2 = open(filename, 'r')
        line = f2.readline().rstrip('\n')
        # print(line)
        f2.close()
    except FileNotFoundError:
        print("skysports_file not found")
    # 쓰기모드로 열기
    f = open(filename, 'w')

    final_data = []
    source = 'SkySports'

    req = requests.get(team_url)  # url에 요청
    html = req.text  # text 뽑아냄
    soup = BeautifulSoup(html, 'html.parser')  # html 형식으로 가져옴
    original_links = soup.select('.news-list__headline-link')  # 각 news 링크 가져옴
    original_label = soup.select('.label__tag')  # label 가져옴 - Live Football 제외하려고
    links = []

    # Live Football 인 기사 제외하는 작업
    for i, val in enumerate(original_links):
        if original_label[i].text != "Live Football":
            links.append(val.get('href'))

    # 각 팀의 맨첫번째 url 쓰기
    try:
        f.write(links[0] + '\n')
    except Exception as ex:
        print('file write error', ex)


    # 각 링크 돌아다니며 제목, 부제목, 내용, 날짜 수집
    for link in links:
        # 같은 팀의 기사들 긁어올때 전에 크롤링했던거 있으면 포문 종료
        try:
            if line == link:
                print("overlap1")
                break
        except IndexError:
            print("skysports index error")

        req = requests.get(link)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        title = ''
        contents = ''
        img_src = ''
        date = ''

        # 제목
        if soup.select('.article__long-title'):
            title = soup.select('.article__long-title')[0].text.strip()
        if title == '':
            continue

        # 기사 본문
        content_arr = soup.select(
            '.article__body p'
        )
        for content in content_arr:
            contents = contents + content.text + '\n\n'

        if content_arr.__sizeof__() is 0:
            continue
        if contents == '':
            continue

        # 이미지 source 추출
        if soup.select('.auto-size__target.postpone-load--fade-in.widge-figure__image'):
            img_src = soup.select('.auto-size__target.postpone-load--fade-in.widge-figure__image')[0].get(
                    'data-src')

        # 업데이트 날짜
        if soup.select('.article__header-date-time'):
            temp = soup.select('.article__header-date-time')[0].text
            day = temp[14:16]
            month = temp[17:19]
            year = '20' + temp[20:22]
            time = temp[23:30]
            time_temp = time.split(':', maxsplit=1)
            hour = time_temp[0]
            _time = time_temp[1]
            minute = _time[0] + _time[1]
            m = _time[2] + _time[3]
            if (m == 'pm'):
                hour = 12 + int(hour)
                if (hour == 24):
                    hour = 00

            if(len(str(hour)) == 1):
                hour = '0' + str(hour)
            hour = str(hour)
            second = '00'

            _date = year + '-' + month + '-' + day + ' ' + hour + ":" + minute + ":" + second
            date = datetime.strptime(_date, '%Y-%m-%d %H:%M:%S')
            # date = datetime.now

        print(link)
        data = [source, source_team[team_select(team_url)], link, title, contents, img_src, date]
        final_data.append(data)
    f.close()
    return final_data



def pool_and_save(result):
    # 삼중포문 -> 이중포문으로 변환
    result2 = []
    for data in result:
        for p in data:
            result2.append(p)
    # 다른 팀에서 중복된 url이면 for문 건너뜀
    for link1 in result2:
        cnt = 0
        for i, link2 in enumerate(result2):
            if link1[2] == link2[2]:
                cnt = cnt + 1
                if cnt >= 2:
                    print("overlap2")
                    print(result2[i][2])
                    result2.pop(i)

    for data in result2:
        Articledata(source=data[0], source_team=data[1], url=data[2], title=data[3], contents=data[4], img_url=data[5], date=data[6], click_no = 1, rec_percent = 0, rec_score = 0, rec_count = 0).save()

if __name__ == '__main__':
    # skysports_data_dict = parse_skysports()
    # for t, s, c, d in skysports_data_dict:
    #     SkySportsData(title=t, subtitle=s, content=c, date=d).save()

    pool = Pool(processes=4)
    start_time = time.time()
    skysports_urls = ['https://www.skysports.com/arsenal-news', 'https://www.skysports.com/aston-villa-news', 'https://www.skysports.com/bournemouth-news',
            'https://www.skysports.com/brighton-and-hove-albion-news','https://www.skysports.com/burnley-news','https://www.skysports.com/chelsea-news',
            'https://www.skysports.com/crystal-palace-news','https://www.skysports.com/everton-news','https://www.skysports.com/leicester-city-news',
            'https://www.skysports.com/liverpool-news','https://www.skysports.com/manchester-city-news', 'https://www.skysports.com/manchester-united-news',
            'https://www.skysports.com/newcastle-united-news','https://www.skysports.com/norwich-city-news','https://www.skysports.com/sheffield-united-news',
            'https://www.skysports.com/southampton-news','https://www.skysports.com/tottenham-hotspur-news','https://www.skysports.com/watford-news',
            'https://www.skysports.com/west-ham-united-news','https://www.skysports.com/wolverhampton-wanderers-news']
    skysports_result = pool.map(parse_skysports, skysports_urls)
    pool_and_save(skysports_result)
    print("SkySports Done --- %s seconds ---" % (time.time() - start_time))

    espn_urls = ['https://www.espn.com/soccer/club/_/id/359/arsenal','https://www.espn.com/soccer/team/_/id/362/aston-villa','https://www.espn.com/soccer/team/_/id/349/afc-bournemouth',
                 'https://www.espn.com/soccer/team/_/id/331/brighton-hove-albion','https://www.espn.com/soccer/team/_/id/379/burnley','https://www.espn.com/soccer/team/_/id/363/chelsea',
                 'https://www.espn.com/soccer/team/_/id/384/crystal-palace','https://www.espn.com/soccer/team/_/id/368/everton','https://www.espn.com/soccer/team/_/id/375/leicester-city',
                 'https://www.espn.com/soccer/team/_/id/364/liverpool','https://www.espn.com/soccer/team/_/id/382/manchester-city','https://www.espn.com/soccer/team/_/id/360/manchester-united',
                 'https://www.espn.com/soccer/team/_/id/361/newcastle-united','https://www.espn.com/soccer/team/_/id/381/norwich-city','https://www.espn.com/soccer/team/_/id/398/sheffield-united',
                 'https://www.espn.com/soccer/team/_/id/376/southampton','https://www.espn.com/soccer/team/_/id/367/tottenham-hotspur','https://www.espn.com/soccer/team/_/id/395/watford',
                 'https://www.espn.com/soccer/team/_/id/371/west-ham-united','https://www.espn.com/soccer/team/_/id/380/wolverhampton-wanderers']
    espn_result = pool.map(parse_espn, espn_urls)
    pool_and_save(espn_result)
    print("ESPN Done --- %s seconds ---" % (time.time() - start_time))

    bbc_urls = ['https://www.bbc.com/sport/football/teams/arsenal','https://www.bbc.com/sport/football/teams/aston-villa','https://www.bbc.com/sport/football/teams/afc-bournemouth',
    'https://www.bbc.com/sport/football/teams/brighton-and-hove-albion','https://www.bbc.com/sport/football/teams/burnley','https://www.bbc.com/sport/football/teams/chelsea',
    'https://www.bbc.com/sport/football/teams/crystal-palace','https://www.bbc.com/sport/football/teams/everton','https://www.bbc.com/sport/football/teams/leicester-city',
    'https://www.bbc.com/sport/football/teams/liverpool','https://www.bbc.com/sport/football/teams/manchester-city','https://www.bbc.com/sport/football/teams/manchester-united',
    'https://www.bbc.com/sport/football/teams/newcastle-united','https://www.bbc.com/sport/football/teams/norwich-city','https://www.bbc.com/sport/football/teams/sheffield-united',
    'https://www.bbc.com/sport/football/teams/southampton','https://www.bbc.com/sport/football/teams/tottenham-hotspur','https://www.bbc.com/sport/football/teams/watford',
    'https://www.bbc.com/sport/football/teams/west-ham-united','https://www.bbc.com/sport/football/teams/wolverhampton-wanderers']
    bbc_result = pool.map(parse_bbc, bbc_urls)
    pool_and_save(bbc_result)
    print("BBC Done --- %s seconds ---" % (time.time() - start_time))

    sun_urls = ['https://www.thesun.co.uk/sport/football/team/1196653/arsenal/', 'https://www.thesun.co.uk/sport/football/team/1196670/aston-villa/', 'https://www.thesun.co.uk/sport/football/team/1196663/bournemouth/', 'https://www.thesun.co.uk/sport/football/team/3571589/brighton-hove-albion/',
                'https://www.thesun.co.uk/sport/football/team/1225466/burnley/', 'https://www.thesun.co.uk/sport/football/team/1196660/chelsea/', 'https://www.thesun.co.uk/sport/football/team/1196666/crystal-palace/', 'https://www.thesun.co.uk/sport/football/team/1196662/everton/', 'https://www.thesun.co.uk/sport/football/team/1196651/leicester-city/',
                'https://www.thesun.co.uk/sport/football/team/1196659/liverpool/', 'https://www.thesun.co.uk/sport/football/team/1196654/manchester-city/', 'https://www.thesun.co.uk/sport/football/team/1196656/manchester-united/', 'https://www.thesun.co.uk/sport/football/team/1196669/newcastle-united/',
                'https://www.thesun.co.uk/sport/football/team/1196667/norwich-city/', 'https://www.thesun.co.uk/sport/football/team/3735972/sheffield-united/', 'https://www.thesun.co.uk/sport/football/team/1196657/southampton/' ,'https://www.thesun.co.uk/sport/football/team/1196652/tottenham-hotspur/',
                'https://www.thesun.co.uk/sport/football/team/1196664/watford/' ,'https://www.thesun.co.uk/sport/football/team/1196655/west-ham/', 'https://www.thesun.co.uk/sport/football/team/1226009/wolverhampton-wanderers/']
    sun_result = pool.map(parse_sun, sun_urls)
    pool_and_save(sun_result)
    print("Sun Done --- %s seconds ---" % (time.time() - start_time))

    dailymail_urls = ['https://www.dailymail.co.uk/sport/teampages/arsenal.html', 'https://www.dailymail.co.uk/sport/teampages/aston-villa.html', 'https://www.dailymail.co.uk/sport/teampages/bournemouth.html',
    'https://www.dailymail.co.uk/sport/teampages/brighton-and-hove-albion.html','https://www.dailymail.co.uk/sport/teampages/burnley.html','https://www.dailymail.co.uk/sport/teampages/chelsea.html',
    'https://www.dailymail.co.uk/sport/teampages/crystal-palace.html','https://www.dailymail.co.uk/sport/teampages/everton.html','https://www.dailymail.co.uk/sport/teampages/leicester.html',
    'https://www.dailymail.co.uk/sport/teampages/liverpool.html','https://www.dailymail.co.uk/sport/teampages/manchester-city.html','https://www.dailymail.co.uk/sport/teampages/manchester-united.html',
    'https://www.dailymail.co.uk/sport/teampages/newcastle-united.html','https://www.dailymail.co.uk/sport/teampages/norwich-city.html','https://www.dailymail.co.uk/sport/teampages/sheffield-united.html',
    'https://www.dailymail.co.uk/sport/teampages/southampton.html','https://www.dailymail.co.uk/sport/teampages/tottenham-hotspur.html','https://www.dailymail.co.uk/sport/teampages/watford.html',
    'https://www.dailymail.co.uk/sport/teampages/west-ham-united.html','https://www.dailymail.co.uk/sport/teampages/wolverhampton-wanderers.html']
    dailymail_result = pool.map(parse_dailymail, dailymail_urls)
    pool_and_save(dailymail_result)
    print("DailyMail Done --- %s seconds ---" % (time.time() - start_time))

    guardian_urls = ['https://www.theguardian.com/football/arsenal','https://www.theguardian.com/football/aston-villa','https://www.theguardian.com/football/bournemouth','https://www.theguardian.com/football/brightonfootball',
    'https://www.theguardian.com/football/burnley','https://www.theguardian.com/football/chelsea','https://www.theguardian.com/football/crystalpalace','https://www.theguardian.com/football/everton',
    'https://www.theguardian.com/football/leicestercity','https://www.theguardian.com/football/liverpool','https://www.theguardian.com/football/manchestercity','https://www.theguardian.com/football/manchester-united',
    'https://www.theguardian.com/football/newcastleunited','https://www.theguardian.com/football/norwichcity','https://www.theguardian.com/football/sheffieldunited','https://www.theguardian.com/football/southampton',
    'https://www.theguardian.com/football/tottenham-hotspur','https://www.theguardian.com/football/watford','https://www.theguardian.com/football/westhamunited','https://www.theguardian.com/football/wolves']
    guardian_result = pool.map(parse_guardian, guardian_urls)
    pool_and_save(guardian_result)
    print("Guardian Done --- %s seconds ---" % (time.time() - start_time))

    telegraph_urls = ['https://www.telegraph.co.uk/premier-league/']
    telegraph_result = pool.map(parse_telegraph, telegraph_urls)
    pool_and_save(telegraph_result)
    print("Telegraph Done --- %s seconds ---" % (time.time() - start_time))

    goal_urls = ['https://www.goal.com/en/team/arsenal/1/4dsgumo7d4zupm2ugsvm4zm4d','https://www.goal.com/en/team/aston-villa/1/b496gs285it6bheuikox6z9mj','https://www.goal.com/en/team/afc-bournemouth/1/1pse9ta7a45pi2w2grjim70ge',
            'https://www.goal.com/en/team/brighton-hove-albion/1/e5p0ehyguld7egzhiedpdnc3w','https://www.goal.com/en/team/burnley/1/64bxxwu2mv2qqlv0monbkj1om',
            'https://www.goal.com/en/team/chelsea/1/9q0arba2kbnywth8bkxlhgmdr','https://www.goal.com/en/team/crystal-palace/1/1c8m2ko0wxq1asfkuykurdr0y','https://www.goal.com/en/team/everton/1/ehd2iemqmschhj2ec0vayztzz',
            'https://www.goal.com/en/team/leicester-city/1/avxknfz4f6ob0rv9dbnxdzde0','https://www.goal.com/en/team/liverpool/1/c8h9bw1l82s06h77xxrelzhur','https://www.goal.com/en/team/manchester-city/1/a3nyxabgsqlnqfkeg41m6tnpp',
            'https://www.goal.com/en/team/manchester-united/1/6eqit8ye8aomdsrrq0hk3v7gh','https://www.goal.com/en/team/newcastle-united/1/7vn2i2kd35zuetw6b38gw9jsz','https://www.goal.com/en/team/norwich-city/1/suz80crpy3anixyzccmu6jzp',
            'https://www.goal.com/en/team/sheffield-united/1/bws31egwjda253q9lvykgnivo','https://www.goal.com/en/team/southampton/1/d5ydtvt96bv7fq04yqm2w2632','https://www.goal.com/en/team/tottenham-hotspur/1/22doj4sgsocqpxw45h607udje',
            'https://www.goal.com/en/team/watford/1/4t83rqbdbekinxl5fz2ygsyta','https://www.goal.com/en/team/west-ham-united/1/4txjdaqveermfryvbfrr4taf7','https://www.goal.com/en/team/wolverhampton-wanderers/1/b9si1jn1lfxfund69e9ogcu2n']
    goal_result = pool.map(parse_goal, goal_urls)
    pool_and_save(goal_result)
    print("Goal Done --- %s seconds ---" % (time.time() - start_time))

    express_urls = ['https://www.express.co.uk/football/teams/3/arsenal','https://www.express.co.uk/football/teams/7/aston-villa','https://www.express.co.uk/football/teams/75/bournemouth',
                    'https://www.express.co.uk/football/teams/62/brighton-hove-albion','https://www.express.co.uk/football/teams/23/burnley','https://www.express.co.uk/football/teams/17/chelsea',
                    'https://www.express.co.uk/football/teams/44/crystal-palace','https://www.express.co.uk/football/teams/5/everton','https://www.express.co.uk/football/teams/56/leicester-city',
                    'https://www.express.co.uk/football/teams/56/leicester-city','https://www.express.co.uk/football/teams/15/liverpool','https://www.express.co.uk/football/teams/20/manchester-city',
                    'https://www.express.co.uk/football/teams/8/manchester-united','https://www.express.co.uk/football/teams/18/newcastle-united','https://www.express.co.uk/football/teams/40/norwich-city',
                    'https://www.express.co.uk/football/teams/38/sheffield-united','https://www.express.co.uk/football/teams/35/southampton','https://www.express.co.uk/football/teams/12/tottenham-hotspur',
                    'https://www.express.co.uk/football/teams/30/watford','https://www.express.co.uk/football/teams/6/west-ham','https://www.express.co.uk/football/teams/28/wolves']
    express_result = pool.map(parse_express, express_urls)
    pool_and_save(express_result)
    print("Express Done --- %s seconds ---" % (time.time() - start_time))

    mirror_urls = ['https://www.mirror.co.uk/all-about/arsenal-fc','https://www.mirror.co.uk/all-about/aston-villa-fc','https://www.mirror.co.uk/all-about/afc-bournemouth',
    'https://www.mirror.co.uk/all-about/brighton-and-hove-albion-fc','https://www.mirror.co.uk/all-about/burnley-fc','https://www.mirror.co.uk/all-about/chelsea-fc',
    'https://www.mirror.co.uk/all-about/crystal-palace-fc','https://www.mirror.co.uk/all-about/everton-fc','https://www.mirror.co.uk/all-about/leicester-city-fc',
    'https://www.mirror.co.uk/all-about/liverpool-fc','https://www.mirror.co.uk/all-about/manchester-city-fc','https://www.mirror.co.uk/all-about/manchester-united-fc',
    'https://www.mirror.co.uk/all-about/newcastle-united-fc','https://www.mirror.co.uk/all-about/norwich-city-fc','https://www.mirror.co.uk/all-about/sheffield-united-fc',
    'https://www.mirror.co.uk/all-about/southampton-fc','https://www.mirror.co.uk/all-about/tottenham-hotspur-fc','https://www.mirror.co.uk/all-about/watford-fc',
    'https://www.mirror.co.uk/all-about/west-ham-united-fc','https://www.mirror.co.uk/all-about/wolverhampton-wanderers-fc']
    mirror_result = pool.map(parse_mirror, mirror_urls)
    pool_and_save(mirror_result)
    print("Mirror Done --- %s seconds ---" % (time.time() - start_time))

    subprocess.call("/home/kim/txtmin.R", shell=True)

    print("textmining Done --- %s seconds ---" % (time.time() - start_time))

    subprocess.call("/home/kim/duplart.R", shell=True)
    print("overlap Done --- %s seconds ---" % (time.time() - start_time))







        # skysports_data = pd.DataFrame(parse_skysports())
        # skysports_data.columns = ['source', 'source_team', 'link', 'title', 'subtitle', 'content', 'date', 'img']
        # skysports_data.to_csv('SkySportsData.csv')



    # 중복제거하는거 (다른팀에서도 나오는거)
    # result = parse_skysports()
    # for link1 in result:
    #     cnt = 0
    #     for i, link2 in enumerate(result):
    #         if (link1[2] == link2[2]):
    #             cnt = cnt + 1
    #             if cnt >= 2:
    #                 print("overlap2")
    #                 print(result[i][2])
    #                 result.pop(i)
    # for i in result:
    #     print(i[2])

# # 이 명령어는 이 파일이 import가 아닌 python에서 직접 실행할 경우에만 아래 코드가 동작하도록 합니다.
# if __name__=='__main__':
#     blog_data_dict = parse_blog()
#     for t, l in blog_data_dict.items():
#         BlogData(title=t, link=l).save()