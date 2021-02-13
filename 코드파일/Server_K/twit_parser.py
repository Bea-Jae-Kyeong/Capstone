
import datetime
import os
import time
import pandas as pd
import pymysql

from datetime import timedelta
from datetime import date
from multiprocessing.pool import Pool


os.environ.setdefault("DJANGO_SETTINGS_MODULE", "Server_K.settings")

# 이제 장고를 가져와 장고 프로젝트를 사용할 수 있도록 환경을 만듭니다.
import django
django.setup()
from api.models import Twitter

import GetOldTweets3
from twitterscraper import query_tweets

source_team = ['Arsenal', 'Aston villa', 'Bournemouth', 'Brighton & Hove albion', 'Burnley', 'Chelsea',
                   'Crystal Palace', 'Everton', 'Leicester City','Liverpool', 'Manchester City', 'Manchester United', 'Newcastle United', 'Norwich City',
                   'Sheffield United', 'Southampton', 'Tottenham Hotspur', 'Watford', 'West Ham United','Wolverhampton Wanderers']

def team_select(team_url):
    team_index = 0
    if "Arsenal" in team_url:
        team_index = 0
    elif "AVFCOfficial" in team_url:
        team_index = 1
    elif "AFC Bournemouth" in team_url:
        team_index = 2
    elif "OfficialBHAFC" in team_url:
        team_index = 3
    elif "BurnleyOfficial" in team_url:
        team_index = 4
    elif "ChelseaFC" in team_url:
        team_index = 5
    elif "CPFC" in team_url:
        team_index = 6
    elif "Everton" in team_url:
        team_index = 7
    elif "LCFC" in team_url:
        team_index = 8
    elif "LFC" in team_url:
        team_index = 9
    elif "ManCity" in team_url:
        team_index = 10
    elif "ManUtd" in team_url:
        team_index = 11
    elif "NUFC" in team_url:
        team_index = 12
    elif "NorwichCityFC" in team_url:
        team_index = 13
    elif "sheffieldUnited" in team_url:
        team_index = 14
    elif "SouthamptonFC" in team_url:
        team_index = 15
    elif "SpursOfficial" in team_url:
        team_index = 16
    elif "WatfordFC" in team_url:
        team_index = 17
    elif "WestHam" in team_url:
        team_index = 18
    elif "Wolves" in team_url:
        team_index = 19
    return team_index


def parse_twit(team_twit):

    final_data = []

    now = datetime.datetime.now()
    td = timedelta(weeks = 3)
    last = now - td

    tc = GetOldTweets3.manager.TweetCriteria().setUsername(team_twit)\
                                                     .setSince(str(last.year) + "-" + str(last.month) + "-" + str(last.day))\
                                                     .setUntil(str(now.year) + "-" + str(now.month) + "-" + str(now.day))

    tweet = GetOldTweets3.manager.TweetManager.getTweets(tc)
    for t in tweet:
        if len(t.text) < 10:
            continue
        print(t)
        print(t.text)
        print(t.date)
        # print(t.timestamp)
        ti = str(t.date).split(' ', maxsplit=1)
        date = ti[0].split('-', maxsplit=2)
        year = date[0]
        month = date[1]
        day = date[2]
        time = ti[1].split(':', maxsplit=3)
        hour = time[0]
        minute = time[1]
        sec = '00'
        date = datetime.datetime(int(year), int(month), int(day), int(hour), int(minute), int(sec))
        text = t.text
        data = [source_team[team_select(team_twit)], text, date]
        final_data.append(data)

    return final_data

def parse_ID(ID_twit):

    final_data = []

    now = datetime.datetime.now()
    td = timedelta(weeks = 3)
    last = now - td

    tc = GetOldTweets3.manager.TweetCriteria().setUsername(ID_twit)\
                                                     .setSince(str(last.year) + "-" + str(last.month) + "-" + str(last.day))\
                                                     .setUntil(str(now.year) + "-" + str(now.month) + "-" + str(now.day))



    tweet = GetOldTweets3.manager.TweetManager.getTweets(tc)

    for t in tweet:
        if len(t.text) < 10:
            continue
        print(t.text)
        print(t.date)
        # print(t.timestamp)
        ti = str(t.date).split(' ', maxsplit=1)
        date = ti[0].split('-', maxsplit=2)
        year = date[0]
        month = date[1]
        day = date[2]
        time = ti[1].split(':', maxsplit=3)
        hour = time[0]
        minute = time[1]
        sec = '00'
        date = datetime.datetime(int(year), int(month), int(day), int(hour), int(minute), int(sec))
        data = ['', t.text, date]
        final_data.append(data)

    return final_data

def parse_Keyword(Keyword_twit):

    final_data = []

    now = datetime.date.today()
    # print(now.isoformat())
    # year = now.year
    # month = now.month
    # day = now.day
    #today = now.strftime("%y-%m-%d")
    td = timedelta(days = 1)
    last = now - td

    tweet = query_tweets(Keyword_twit, begindate=last, enddate=now, lang='en')

    for t in tweet:
        print(t.timestamp)
        time = str(t.timestamp).split(' ', maxsplit=1)
        date = time[0].split('-', maxsplit=2)
        year = date[0]
        month = date[1]
        day = date[2]
        time = time[1].split(':', maxsplit=2)
        hour = time[0]
        minute = time[1]
        sec = time[2]
        date = datetime.datetime(int(year), int(month), int(day), int(hour),int(minute), int(sec))
        print(date)
#        date = datetime.strftime(t.timestamp, '%Y-%m-%d %H:%M:%S')
        data = ['', t.text, date]
        final_data.append(data)

    return final_data

if __name__ == '__main__':

    # mysql Connection
    conn = pymysql.connect(host='localhost', user='DB_K', password='1q2w3e4r!', db='api',
                           charset='utf8')

    # Connection cursor
    curs = conn.cursor()

    # SQL statament execute
    sql = "delete from twitter"
    curs.execute(sql)

    conn.commit()
    conn.close()


    team_twit = ['Arsenal', 'AVFCOfficial', 'AFC Bournemouth', 'OfficialBHAFC', 'BurnleyOfficial', 'ChelseaFC',
                 'CPFC', 'Everton', 'LCFC', 'LFC', 'ManUtd', 'ManCity', 'NUFC', 'NorwichCityFC',
                 'SheffieldUnited', 'SouthamptonFC', 'SpursOfficial', 'WatfordFC', 'WestHam', 'Wolves']

    ID_twit = {'gunnerforever78', 'ThrowbackAFC', 'afcstuff', 'GurjitAFC', 'AFTVMedia', 'VipArsenal', 'GunnersNews2019', 'avfcnews2019', 'AVFC_News',
               'astonforza', 'AVFCchats', 'VillaTil1Die', 'AFCB_Fanly', 'FWPBournemouth', 'FWPBrighton', 'AlbionAnalytics', 'BHAseagulls_com',
               'Burnleysbest', 'BurnleyFC_Com', 'TheClaretsChat', 'BXSport', 'BestOfChelsea1', 'CFCNewsReport', 'BoysCfc', 'ChelseaFarleh', 'TheBlues___',
               'PalaceEaglesc0m', 'AdvertiserCPFC' , 'CPFCBC', 'ReadEverton', 'vitaleverton' 'EvertonNewsFeed', 'lcfcawaydays_', 'FIRST4LCFC', 'FoxesofLCFC',
               'Liverpool_zy', 'Liverpoolcom_', 'AnfieldWatch', 'iLiverpoolApp', 'TheAnfieldWrap', 'City_Xtra', 'ManCityMEN', 'ManCityzenscom',
               'MUFCScoop', 'UnitedStandMUFC', 'FullTimeDEVILS', 'FWPNewcastle', 'NUFCCoIIective', 'nufcnews2019', 'TalkNorwichCity', 'NCFC__News',
               'The_Bladesman', 'SheffieldUNews', 'sufcdevelopment', 'iSouthamptonApp', 'ReadSouthampton', 'thespursweb', 'News2019Thfc', 'iSpursApp', 'Spurs_Centre',
               'watfordfclivehq', 'FWPWatford', 'WatfordFNH', 'watfordsz', 'Sam_InkersoleTM', 'WestHam_fl', 'westham_gossip', 'Wolves_TT1', 'FWPWolves', 'LiveWolvesNews', 'WolvesTalk',
               'premierleague', 'OfficialFPL', 'Premiereleague7'}


    Keyword_twit = {'"arsenal"', '"aston villa"', '"Bournemouth"', '"afcbournemouth"', '"Brighton hove albion"', '"Brighton & hove albion"',
                     '"burnley"', '"Chelsea"', '"Crystal Palace"', '"Everton"', '"Leicester City"', '"LCFC"', '"liverpool"', '"Manchester City"',
                     '"Manchester United"', '"Newcastle United"', '"Norwich City"', '"Sheffield United"', '"Southampton"', '"Tottenham Hotspur"',
                     '"Watford"', '"West Ham United"', '"Wolverhampton Wanderers"'}


    pool = Pool(processes=4)

    # DB

    start_time = time.time()

    final_result = []

    twits_result = pool.map(parse_twit, team_twit)
    for i in twits_result:
        for data in i:
            Twitter(team=data[0], contents=data[1], date=data[2]).save()

    twits_result = pool.map(parse_ID, ID_twit)
    for i in twits_result:
        for data in i:
            Twitter(team=data[0], contents=data[1], date=data[2]).save()


    # csvfile

    # final_result = []
    #
    #
    # start_time = time.time()
    # twits_result = pool.map(parse_twit, team_twit)
    # for i in twits_result:
    #     for data in i:
    #         final_result.append(data)
    #
    #
    # twits_result = pool.map(parse_ID, ID_twit)
    # for i in twits_result:
    #     for data in i:
    #         final_result.append(data)

    # twitter_csv = pd.DataFrame(final_result)
    # twitter_csv.columns = ['team', 'contents', 'date']
    # twitter_csv.to_csv('twitter.csv')

    # for i in twits_result:
    #     print(i)
    #     for data in i:
    #         Twitter(team=data[0], contents=data[1], date=data[2]).save()



    # twitter_csv2 = pd.DataFrame(twits_result2)
    # twitter_csv2.columns = ['team', 'contents', 'date']
    # twitter_csv2.to_csv('twitter2.csv')


    # skysports_data = pd.DataFrame(parse_skysports())
    # skysports_data.columns = ['source', 'source_team', 'link', 'title', 'subtitle', 'content', 'date', 'img']
    # skysports_data.to_csv('SkySportsData.csv')

    print("Twitter Done --- %s seconds ---" % (time.time() - start_time))