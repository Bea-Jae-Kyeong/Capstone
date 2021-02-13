import pymysql
import datetime
import time
import pandas as pd
import subprocess

def press_to_n(press):
    rt = 0
    if press == 'BBC':
        rt = 547
    if press == 'DailyMail':
        rt = 548
    if press == 'ESPN':
        rt =  549
    if press == 'Express':
        rt =  550
    if press == 'Goal':
        rt =  551
    if press == 'Mirror':
        rt =  552
    if press == 'SkySports':
        rt =  553
    if press == 'Telegraph':
        rt =  554
    if press == 'The Guardian':
        rt =  555
    if press == 'The Sun':
        rt =  556

    return rt

def article_rec_score():

    # mysql Connection
    conn = pymysql.connect(host='localhost', user='DB_K', password='1q2w3e4r!', db='api',
                           charset='utf8')

    #Connection cursor
    curs = conn.cursor()

    #SQL statament execute
    sql = "select * from articledata"
    curs.execute(sql)


    #data fetch
    rows = curs.fetchall()

    article = [] # news_id, click_no, date, image, rec_percent, rec_count, rec_point
    __keyword = [] #  Keyword 1~10


    for ar in rows:
        _keyword = []               #_10 keyword
        _article = []               # article_data
        _article.append(ar[0])      # news_id               0
        _article.append(ar[8])      # click_no              1
        _article.append(ar[7])      # date                  2
        _article.append(ar[6])      # image                 3
        _article.append(ar[20])     # rec_percent           4
        _article.append(ar[21])     # rec_count             5
        _keyword.append(ar[9])      # keyword 0
        _keyword.append(ar[10])
        _keyword.append(ar[11])
        _keyword.append(ar[12])
        _keyword.append(ar[13])
        _keyword.append(ar[14])
        _keyword.append(ar[15])
        _keyword.append(ar[16])
        _keyword.append(ar[17])
        _keyword.append(ar[18])     # keyword end 9
        _article.append(0)        # Recomendation_Point     6
        _article.append(ar[1])      # press                 7
        article.append(_article)
        __keyword.append(_keyword)


    sql = "select * from keyword"

    curs.execute(sql)

    #data fetch
    rows = curs.fetchall()

    keyword = [] #  Keyword 1~5 no overlap



    # keyword select 5
    for count in __keyword:
        _keyword = []
        _keyword.clear()

        _keyword.append(count[0])
        for i in range(1, 10):

            if count[i] is None:
                break
            if len(_keyword) is 5:
                break

        _keyword.append(count[i])

        keyword.append(_keyword)

    keyword_weight = []

    for ar in rows:
        keyword_weight.append(ar[6])    # keyword_weight


    for i, point_data in enumerate(article):

        point = 30
        temp = point_data[1]


        #click
        _temp = temp // 3
        point = point + (_temp * 2)


        #date
        temp = point_data[2]

        now = datetime.datetime.now()

        temp = now - temp
        timedelt = datetime.timedelta(hours = 6)
        temp = temp / timedelt

        point = point - (temp)

        #press
        point = point + keyword_weight[press_to_n(point_data[7])]

        #keyword

        keyword_point = 0

        for p in range(len(keyword[i])):

            if keyword[i][p] is None:
                break

            keyword_point += keyword_weight[keyword[i][p]]

        point += keyword_point

        # image
        
        if point_data[3] is not '':
            point += 20

        #recomadation_percent 4 // rec_count 5

        if point_data[5] >= 5:
            if point_data[4] < 30:
                point -= 3
            elif point_data[4] < 50:
                point -= 1
            elif point_data[4] < 70:
                point += 1
            elif point_data[4] <= 100:
                point += 3


        point_data[6] = point

        # update rec_point news_id  0 Recomendation_Point  6
        sql = "UPDATE articledata SET rec_score = %s WHERE news_id = %s"
        curs.execute(sql, (point_data[6], point_data[0]))

        conn.commit()





    conn.close()




def keyword_weight_setting():

    # mysql Connection
    conn = pymysql.connect(host='localhost', user='DB_K', password='1q2w3e4r!', db='api',
                           charset='utf8')

    # Connection cursor
    curs = conn.cursor()

    # SQL statament execute
    sql = "select * from keyword"
    curs.execute(sql)

    # data fetch
    rows = curs.fetchall()

    keyword = []


    # keyword
    for ar in rows:
        _keyword = []

        _keyword.append(ar[0])  # keyword_no
        _keyword.append(ar[5])  # view_no 1
        _keyword.append(ar[6])  # weight 2

        keyword.append(_keyword)


    # player setting

    click_total = 0

    for i in range(0, 506):
        click_total += keyword[i][1]


    for i in range(0, 506):
        total = 2530

        keyword[i][2] = (keyword[i][1] / click_total) * total


    #team setting

    click_total = 0

    for i in range(506, 526):
        click_total += keyword[i][1]

    for i in range(506, 526):
        total = 120

        keyword[i][2] = (keyword[i][1] / click_total) * total

    # manager setting

    click_total = 0

    for i in range(526, 546):
        click_total += keyword[i][1]

    for i in range(526, 546):
        total = 140

        keyword[i][2] = (keyword[i][1] / click_total) * total

    # press setting

    click_total = 0

    for i in range(547, 557):
        click_total += keyword[i][1]

    for i in range(547, 557):
        total = 30

        keyword[i][2] = (keyword[i][1] / click_total) * total



    # update sql
    for key in keyword:
        sql = "UPDATE keyword SET weight = %s WHERE keyword_no = %s"
        curs.execute(sql,(key[2], key[0]))

    conn.commit()

    conn.close()


def click_update():


    # mysql Connection
    conn = pymysql.connect(host='localhost', user='DB_K', password='1q2w3e4r!', db='api',
                           charset='utf8')

    #Connection cursor
    curs = conn.cursor()

    #SQL statament execute
    sql = "select * from articledata"
    curs.execute(sql)


    #data fetch
    rows = curs.fetchall()

    article = []  # news_id, click_no, press
    keyword = []  # Keyword 1~10

    for ar in rows:
        _keyword = []  # _10 keyword
        _article = []  # article_data
        _article.append(ar[0])  # news_id       0
        _article.append(ar[8])  # click_no      1
        _article.append(ar[1])  # press         2
        _keyword.append(ar[9])  # keyword 0
        _keyword.append(ar[10])
        _keyword.append(ar[11])
        _keyword.append(ar[12])
        _keyword.append(ar[13])
        _keyword.append(ar[14])
        _keyword.append(ar[15])
        _keyword.append(ar[16])
        _keyword.append(ar[17])
        _keyword.append(ar[18])  # keyword end 9
        article.append(_article)
        keyword.append(_keyword)

    # SQL statament execute
    sql = "select * from keyword"
    curs.execute(sql)

    # data fetch
    rows = curs.fetchall()

    keyword_click = []

    for ar in rows:
        _keyword = []
        _keyword.append(ar[0])  # keyword_no 0
        _keyword.append(0)  # keyword_click 1
        keyword_click.append(_keyword)

    for i, ar in enumerate(article):
        click = ar[1]

        for j in range(0, 10):
            if keyword[i][j] is None:
                break
            keyword_click[keyword[i][j]][1] += int(click)

    # press 547 ~ 556
    # press_to_n(press)
    for i, ar in enumerate(article):
        n = press_to_n(ar[2])
        keyword_click[press_to_n(article[i][2])][1] += 1

    for i in range(0, 556):
        if keyword_click[i][1] is 0:
            keyword_click[i][1] = 1

    # update sql
        for key in keyword_click:
            sql = "UPDATE keyword SET views_no = %s WHERE keyword_no = %s"
            curs.execute(sql, (key[1], key[0]))

        conn.commit()

    conn.close()



def group_rec():    # user-group keyword

    # mysql Connection
    conn = pymysql.connect(host='localhost', user='DB_K', password='1q2w3e4r!', db='api',
                           charset='utf8')

    # Connection cursor
    curs = conn.cursor()

    # SQL statament execute
    sql = "select * from userdata"
    curs.execute(sql)

    # data fetch
    rows = curs.fetchall()

    userdata = []


    for ar in rows:
        _user = []
        _user.append(ar[0])         #user_id 0
        _user.append(ar[1])         #user_kwd 1
        _user.append(ar[2])         #user_kwd 2
        _user.append(ar[3])         #user_kwd 3
        _user.append('')            #user_Rec 1
        _user.append('')            #user_Rec 2
        _user.append('')            #user_Rec 3
        userdata.append(_user)

    for usr in userdata:
        id = usr[0]
        k1 = usr[1]     #user_keyword 1
        k2 = usr[2]     #user_keyword 2
        k3 = usr[3]     #user_keyword 3
        k_rec = []

        for rec_usr in userdata:
            if rec_usr[0] is id:
                continue

            for i in range(1,4):
                if rec_usr[i] == k1:
                    for j in range(1, 4):
                        if rec_usr[j] == k1 or rec_usr[j] == k2 or rec_usr[j] == k3:
                            continue

                        kwd_append = [rec_usr[j], 1]
                        check = False

                        for k in k_rec:
                            if rec_usr[j] == k[0]:
                                k[1] += 1
                                check = True

                        if check is False:
                            k_rec.append(kwd_append)

            for i in range(1,4):
                if rec_usr[i] == k2:
                    for j in range(1, 4):
                        if rec_usr[j] == k1 or rec_usr[j] == k2 or rec_usr[j] == k3:
                            continue

                        kwd_append = [rec_usr[j], 1]
                        check = False

                        for k in k_rec:
                            if rec_usr[j] == k[0]:
                                k[1] += 1
                                check = True

                        if check is False:
                            k_rec.append(kwd_append)

            for i in range(1,4):
                if rec_usr[i] == k3:
                    for j in range(1, 4):
                        if rec_usr[j] == k1 or rec_usr[j] == k2 or rec_usr[j] == k3:
                            continue

                        kwd_append = [rec_usr[j], 1]
                        check = False

                        for k in k_rec:
                            if rec_usr[j] == k[0]:
                                k[1] += 1
                                check = True

                        if check is False:
                            k_rec.append(kwd_append)

        k_rec.sort(reverse = True, key=lambda k_rec: k_rec[1])

        if len(k_rec) < 3:
            if len(k_rec) < 1:
                usr[4] = None
                usr[5] = None
                usr[6] = None
            elif len(k_rec) < 2:
                usr[4] = k_rec[0][0]
                usr[5] = None
                usr[6] = None
            else:
                usr[4] = k_rec[0][0]
                usr[5] = k_rec[1][0]
                usr[6] = None
        else:
            usr[4] = k_rec[0][0]
            usr[5] = k_rec[1][0]
            usr[6] = k_rec[2][0]


        sql = "UPDATE userdata SET rec_keyword1 = %s, rec_keyword2 = %s, rec_keyword3 = %s WHERE user_id = %s"
        curs.execute(sql, (usr[4], usr[5], usr[6], usr[0]))

    conn.commit()
    conn.close()


def user_rec():

    # mysql Connection
    conn = pymysql.connect(host='localhost', user='DB_K', password='1q2w3e4r!', db='api',
                           charset='utf8')

    # Connection cursor
    curs = conn.cursor()

    # SQL statament execute
    sql = "select * from userdata"
    curs.execute(sql)

    # data fetch
    rows = curs.fetchall()

    userdata = []

    for ar in rows:
        _user = []
        _user.append(ar[0])  # user_id 0
        _user.append(ar[1])  # user_kwd 1
        _user.append(ar[2])  # user_kwd 2
        _user.append(ar[3])  # user_kwd 3
        _user.append('')  # user_Rec 4
        _user.append('')  # user_Rec 5
        _user.append('')  # user_Rec 6
        _user.append(ar[4])
        _user.append(ar[5])
        _user.append(ar[6])
        userdata.append(_user)


    for usr in userdata:
        id = usr[0]
        k1 = usr[1]     #user_keyword 1
        k2 = usr[2]     #user_keyword 1
        k3 = usr[3]     #user_keyword 1
        r_k1 = usr[7]
        r_k2 = usr[8]
        r_k3 = usr[9]
        reg = []
        kwd = []
        # SQL statament execute
        sql = "select * from news_reg WHERE user_id = %s"
        curs.execute(sql, (id))


        # data fetch
        rows = curs.fetchall()

        for rg in rows:
            reg.append(rg[2])

        for rg in reg:
            sql = "select * from articledata WHERE news_id = %s"
            curs.execute(sql, (rg))

            row = curs.fetchall()
            news_kwd = []
            for ar in row:
                news_kwd.append(ar[9])
                news_kwd.append(ar[10])
                news_kwd.append(ar[11])
                news_kwd.append(ar[12])
                news_kwd.append(ar[13])
                news_kwd.append(ar[14])
                news_kwd.append(ar[15])
                news_kwd.append(ar[16])
                news_kwd.append(ar[17])
                news_kwd.append(ar[18])


            for k in news_kwd:
                if k == k1 or k == k2 or k == k3  or k == r_k1 or k == r_k2 or k == r_k3:
                    continue
                if k == None:
                    break
                kwd_append = [k, 1]
                check = False

                for ch in kwd:
                    if k == ch[0]:
                        ch[1] += 1
                        check = True

                if check == False:
                    kwd.append(kwd_append)

        kwd.sort(reverse=True, key=lambda kwd: kwd[1])

        if len(kwd) < 3:
            if len(kwd) < 1:
                usr[4] = None
                usr[5] = None
                usr[6] = None
            elif len(kwd) < 2:
                usr[4] = kwd[0][0]
                usr[5] = None
                usr[6] = None
            else:
                usr[4] = kwd[0][0]
                usr[5] = kwd[1][0]
                usr[6] = None
        else:
            usr[4] = kwd[0][0]
            usr[5] = kwd[1][0]
            usr[6] = kwd[2][0]



        sql = "UPDATE userdata SET rec_keyword4 = %s, rec_keyword5 = %s, rec_keyword6 = %s WHERE user_id = %s"
        curs.execute(sql, (usr[4], usr[5], usr[6], usr[0]))

    conn.commit()
    conn.close()

def kwdtoart():

    # mysql Connection
    conn = pymysql.connect(host='localhost', user='DB_K', password='1q2w3e4r!', db='api',
                           charset='utf8')

    # Connection cursor
    curs = conn.cursor()

    # SQL statament execute
    sql = "delete from rec_news"
    curs.execute(sql)

    sql = "select * from userdata"
    curs.execute(sql)

    # data fetch
    rows = curs.fetchall()

    userdata = []
    now = datetime.datetime.now()
    nowDatetime = now.strftime('%Y-%m-%d %H:%M:%S')

    for ar in rows:
        _user = []
        _user.append(ar[0])         #user_id 0
        _user.append(ar[1])         #user_kwd 1
        _user.append(ar[2])         #user_kwd 2
        _user.append(ar[3])         #user_kwd 3
        _user.append(ar[4])         #rec_kwd 1
        _user.append(ar[5])         #rec_kwd 2
        _user.append(ar[6])         #rec_kwd 3
        _user.append(ar[7])         #rec_kwd 4
        _user.append(ar[8])         #rec_kwd 5
        _user.append(ar[9])         #rec_kwd 6
        userdata.append(_user)

    for usr in userdata:

        id = usr[0]

        rec_all_num = 3
        rec_art = []

        for i in range(4, 10):          # if rec_kwd is none add all_rec
            if usr[i] == None:
                rec_all_num += 2



        # kwd to art
        for j in range(4 ,10):
            if usr[j] == None:
                continue

            sql = "select * from articledata WHERE (keyword1 = %s or keyword2 = %s or keyword3 = %s or keyword4 = %s or keyword5 = %s or keyword6 = %s or keyword7 = %s or keyword8 = %s or keyword9 = %s or keyword10 = %s)"
            curs.execute(sql, (usr[j], usr[j], usr[j], usr[j], usr[j], usr[j], usr[j], usr[j], usr[j], usr[j]))

            kwd_article = []
            rows = curs.fetchall()
            for ar in rows:
                _art = []
                _art.append(ar[0])      #news_id
                _art.append(ar[19])     #rec_point
                _art.append(ar[21])      # rec_count
                kwd_article.append(_art)
            kwd_article.sort(key=lambda kwd_article: kwd_article[1])

            article_len = len(kwd_article)

            sql = "select * from sharedata WHERE keyword_id = %s"
            curs.execute(sql, (usr[j]))

            kwd_sharearticle = []
            rows = curs.fetchall()
            for ar in rows:
                _art = []
                _art.append(ar[1])      # keyword_id
                _art.append(ar[2])      # news_id
                _art.append(ar[3])      # rec_point
                kwd_sharearticle.append(_art)
            kwd_sharearticle.sort(key=lambda kwd_sharearticle: kwd_sharearticle[2])

            article = []
            num = 0

            for art in kwd_sharearticle:
                news_id = art[1]

                for _art in range(num, article_len):
                    if kwd_article[0] == news_id:
                        kwd_article[1] += (art[2] * 3)
                        break

                    num += 1

            kwd_article.sort(reverse=True, key=lambda kwd_article: kwd_article[1])

            limit = len(rec_art)

            if len(kwd_article) < 2:
                rec_all_num += 2
            else:
                for __art in kwd_article:          #if the number of article of some keyword is 0 to 1, that will make error
                    _limit = len(rec_art)
                    if (_limit - limit) == 2:
                        break
                    _art = []
                    check = False
                    for n in rec_art:
                        if __art[0] == n[1]:
                            check = True
                            break
                    if check == True:
                        continue
                    else:
                        _art.append(id)
                        _art.append(__art[0])
                        _art.append(__art[1])
                        _art.append(__art[2])
                        rec_art.append(_art)

        sql = "select * from articledata"
        curs.execute(sql)

        rows = curs.fetchall()

        all_rec_article = []

        for ar in rows:
            _art = []
            _art.append(ar[0])
            _art.append(ar[19])
            _art.append(ar[21] + 1)      # rec_count
            all_rec_article.append(_art)

        all_rec_article.sort(reverse=True, key=lambda all_rec_article: all_rec_article[1])

        limit = len(rec_art)

        for i in all_rec_article:  # user_id, news_id, rec_point
            if len(rec_art) == 15:
                break
            _art = []
            check = False
            for n in rec_art:
                if i[0] == n[1]:
                    rec_all_num += 1
                    check = True
                    break
            if check == True:
                continue
            else:
                _art.append(id)
                _art.append(i[0])
                _art.append(i[1])
                _art.append(i[2] + 1)
                rec_art.append(_art)
        # all_rec end



        for rec in rec_art:
            sql = "INSERT INTO rec_news(user_id, news_id, rec_point, rec_reg, date) Values(%s, %s, %s, %s, %s)"
            curs.execute(sql,(rec[0], rec[1], rec[2], 0, nowDatetime))
            sql = "Update articledata set rec_count = %s where news_id = %s"
            curs.execute(sql,(rec[3], rec[1]))

    conn.commit()
    conn.close()


if __name__ == '__main__':


    start_time = time.time()


    # article click update to
    click_update()

    # update keyword_weight
    keyword_weight_setting()

    # update rec_score
    article_rec_score()

    # update group_rec_kwd
    group_rec()

    # update user_rec_kwd
    user_rec()



    # subprocess.call("/home/kim/twitex.R", shell=True)

    # rec_kwd to rec_art
    kwdtoart()


    print("Reccomendation Done --- %s seconds ---" % (time.time() - start_time))
