
import requests
from bs4 import BeautifulSoup
import pandas as pd
from datetime import datetime
import csv
import matplotlib.pyplot as plt
# import matplotlib
from wordcloud import WordCloud
import numpy as np
from konlpy.tag import Okt
from collections import Counter
import os





# now = datetime.now()  # 파일이름 현 시간으로 저장하기(임시)
# https://movie.naver.com/movie/bi/mi/point.nhn?code=189537#tab

with open('movie_num.csv', 'w', encoding='utf-8') as csvfile:
    writer = csv.writer(csvfile)
    writer.writerow(["영화 번호", "개봉일", "제목", "관람객 평점", "평론가 평점", "네티즌 평점", "저장날짜"])

# 영화 목록 크롤링
# 개봉일 관련 수정 해야함 / 재개봉 관련것 추가
def get_review_score(m_num):
    detail = []
    url = "https://movie.naver.com/movie/bi/mi/basic.nhn?code=" + m_num

    breq = requests.get(url)
    bsoup = BeautifulSoup(breq.content, 'html.parser')
    # print(bsoup)

    try:
        title = get_review_title(m_num)
        try:
            # 평점 추출 부분 수정하기
            score1 = bsoup.select('.score .star_score em')[0].text + '.' + bsoup.select('.score .star_score em')[2].text + bsoup.select('.score .star_score em')[3].text
            score2 = bsoup.select('.score .star_score em')[4].text + '.' + bsoup.select('.score .star_score em')[6].text + bsoup.select('.score .star_score em')[7].text
            score3 = bsoup.select('.score .star_score em')[8].text + '.' + bsoup.select('.score .star_score em')[10].text + bsoup.select('.score .star_score em')[11].text
            m_c_date = bsoup.select('.info_spec span a')[3].text + bsoup.select('.info_spec span a')[4].text

            if (score1 == score2 and score1 == score3[:4]):
                score1 = "평점 없음"
                score3 = "평점 없음"
        except:
            score1 = "평점 없음"
            score2 = "평점 없음"
            score3 = "평점 없음"
            m_c_date = bsoup.select_one('.mv_info_area .mv_info .h_movie2').text
            # 슈퍼맨3 참조 개봉일, 재개봉일 구분
            m_c_date = m_c_date.split(',')[1].lstrip()

        date = datetime.today().strftime("%Y-%m-%d")    # 2020-08-14
        # ["영화번호", "영화제목", "개봉일", "관람객 평점", "평론가 평점", "네티즌 평점","저장날짜"]
        detail = [m_num , title , m_c_date , score1 , score2 , score3, date]

        with open('movie_num.csv', 'a', encoding='utf-8') as csvfile:
            writer = csv.writer(csvfile)
            writer.writerow(detail)
            print(m_num , "저장성공")
    except:
        print("평점 없음 오류발생")

    return detail


# 총 리뷰건수 따오기
def get_review_allnum(m_num):

    m_num = str(m_num)
    review_allnum_nrl = "https://movie.naver.com/movie/bi/mi/basic.nhn?code=" + m_num

    try:
        breall = requests.get(review_allnum_nrl)
        bsoupall = BeautifulSoup(breall.content, 'html.parser')
        review_allnum = bsoupall.select('.score_total em')[1].text.strip()
        review_allnum = int(review_allnum.replace(',', ''))
    except:
        review_allnum = 0
    return review_allnum



# 영화 제목 따오기
def get_review_title(m_num):
    url = "https://movie.naver.com/movie/bi/mi/basic.nhn?code=" + m_num
    breq = requests.get(url)
    bsoup = BeautifulSoup(breq.content, 'html.parser')

    try:
        title = bsoup.select_one('.mv_info_area .mv_info .h_movie a').text
    except:
        title = "오류"
        print('[',m_num,']로 배정된 영화는 없는 영화')

    return title



# 리뷰 따오고 저장
def get_review(m_num):
    detail = []

    # 총 리뷰건수(int)
    review_allnum = get_review_allnum(m_num)
    title = get_review_title(m_num)
    # 리뷰 저장용 csv 타이틀 만들기
    csvname = 'review_csv_' + str(m_num) + '_review.csv'

    try:
        if(title != '오류'): # 영화없는 번호인지 판별
            review_url = "https://movie.naver.com/movie/bi/mi/pointWriteFormList.nhn?code=" + m_num + "&page=1"
            bre = requests.get(review_url)
            bsoup = BeautifulSoup(bre.content, 'html.parser')

            if len(bsoup.select('.score_result ul li')) != 0: # 리뷰없는 영화인지 판별

                with open(csvname, 'w', encoding='utf-8') as csvfile:
                    writer = csv.writer(csvfile)
                    # header= 설정
                    # writer.writerow(["이름", "점수", "리뷰내용", "리뷰날짜"])

                # 마지막까지
                for num in range(1, int(review_allnum/10)+2):
                    i = str(num)
                    # 최신순
                    review_url = "https://movie.naver.com/movie/bi/mi/pointWriteFormList.nhn?code=" + m_num + \
                                 "&type=after&onlyActualPointYn=N&onlySpoilerPointYn=N&order=newest&page=" + i

                    bre = requests.get(review_url)
                    bsoup = BeautifulSoup(bre.content, 'html.parser')


                    # 페이지 내의 10개의 리뷰 따오기
                    if(num == int(review_allnum/10)+1):
                        a = review_allnum%10
                    else:
                        a = 10

                    for i in range(0, a):
                        total_review = bsoup.select('.score_result ul li')[i]
                        review_score = total_review.select('div em')[0].text.strip()
                        review_content = bsoup.find(id='_filtered_ment_' + str(i)).text.strip()
                        review_name = total_review.select('div dl dt em a span')[0].text.strip()
                        review_date = total_review.select('div dl dt em')[1].text.strip()

                        # ["이름", "점수", "리뷰내용", "리뷰날짜"]
                        detail = [review_name, review_score, review_content, review_date]


                        with open(csvname, 'a', encoding='utf-8') as csvfile:
                            writer = csv.writer(csvfile)
                            writer.writerow(detail)
                    percent = num/(int(review_allnum/10)+2) * 100
                    # 진행도가 너무 많이 표시되어 5페이지씩 표시
                    if num == int(review_allnum/10)+2 or num % 5 == 0:
                        print(int(review_allnum / 10) + 2, "중", num, "번째 저장성공 / 진행도 -  %.2f %%" % percent)


            else:
                print('리뷰가 없는 영화')

            # 저장여부 판별 밑 리뷰 저장 이름 설정해야함
        else:
            title = get_review_title(m_num)
            print('[', title ,']은 영화가 없는 번호')
    except:
        print('리뷰가 없는 영화')
    return detail


# 리뷰파일이 최신 파일인지 확인
def review_datecheck(m_num, check):
    # 읽어올 파일 지정
    title = 'review_csv/' + str(m_num) + '_review.csv'

    if os.path.isfile(title) == True:
        # 파일 읽어오기
        csv_text = pd.read_csv(title, encoding='utf-8')
        csv_text.columns = ['name', 'score', 'con', 'date']
        # ["이름", "점수", "리뷰내용", "리뷰날짜"]

        # 최신순
        review_url = "https://movie.naver.com/movie/bi/mi/pointWriteFormList.nhn?code=" + m_num + "&type=after&onlyActualPointYn=N&onlySpoilerPointYn=N&order=newest&page=1"
        bre = requests.get(review_url)
        bsoup = BeautifulSoup(bre.content, 'html.parser')
        total_review = bsoup.select('.score_result ul li')[1]

        # 과거 파일과 현재 파일 중 어떤 거를 할 지 정하기
        # check 가 0 이면 과거 파일
        if csv_text['date'][0] != total_review.select('div dl dt em')[1].text.strip() and check != 0:
            print('최신 리뷰파일이 아닙니다.')
            os.remove(title)
            get_review(m_num)
        else:
            print('리뷰파일이 최신입니다.')

    else:
        print('리뷰파일이 없어서 생성 중')
        get_review(m_num)
        csv_text = pd.read_csv(title, encoding='utf-8')


# 리뷰파일에서 문자 추출
def review_word_extra(m_num):
    # 읽어올 파일 지정
    title = 'review_csv/' + str(m_num) + '_review.csv'

    review_datecheck(m_num, 0)
    csv_text = pd.read_csv(title, encoding='utf-8')

    csv_text.columns = ['name', 'score', 'con', 'date']
    # ["이름", "점수", "리뷰내용", "리뷰날짜"]

    # con = []
    con = ""
    i = 0
    while i < int(len(csv_text['con'])):
        # con.append(str(csv_text['con'][i]))
        con = con + str(csv_text['con'][i])
        i = i + 1

        # 진행도
        # percent = i / int(len(csv_text['con'])) * 100
        # if i == int(len(csv_text['con'])) or i%int(len(csv_text['con'])/10) == 0:
        #     print(int(len(csv_text['con'])), "중", i, "번째 저장성공 / 진행도 -  %.2f %%" % percent)

    if con != "":
        return con
    else:
        con = "리뷰 없음"
        return con


# 리뷰 내용 점수별로 나누기 (1~10)
def review_word_extra(m_num , score):
    # 읽어올 파일 지정
    title = 'review_csv/' + str(m_num) + '_review.csv'

    review_datecheck(m_num, 0)
    csv_text = pd.read_csv(title, encoding='utf-8')

    csv_text.columns = ['name', 'score', 'con', 'date']
    # ["이름", "점수", "리뷰내용", "리뷰날짜"]

    # con = []
    con = ""
    i = 0

    while i < int(len(csv_text['con'])):
        if score == csv_text['score'][i]:
            con = con + str(csv_text['con'][i])
        i = i + 1

        # 진행도
        # percent = i / int(len(csv_text['con'])) * 100
        # if i == int(len(csv_text['con'])) or i%int(len(csv_text['con'])/10) == 0:
        #     print(int(len(csv_text['con'])), "중", i, "번째 저장성공 / 진행도 -  %.2f %%" % percent)

    if con != "":
        return con
    else:
        con = "리뷰 없음"
        return con


# 형태소 분석 밑 단어 사용 빈도 추출
def review_wordcount(m_num):

    con = review_word_extra(m_num)
    ntags = 100

    okt = Okt() # konlpy의 Okt객체
    count = Counter(okt.nouns(con))
    # con에서 명사만 분리/추출 후 count에 숫자와 함께 저장

    return_list = []  # 명사 빈도수 저장할 변수

    for n, c in count.most_common(ntags):
         if n != '영화':
            temp = (n, c)
            return_list.append(temp)
    # print("return_list " , return_list )


    # 조사등을 제거하는 것 stop_words
    # stop_words = ['.', '(', ')', ',', "'", '%', '-', 'X', ').', '×','의','자','에','안','번',
    #  '호','을','이','다','만','로','가','를','액','세','제','위','월','수','중','것','표','명']
    # ko = [each_word for each_word in ko if each_word not in stop_words]

    return return_list


# 형태소 분석 밑 단어 사용 빈도 추출(점수별)
def review_wordcount(m_num, score):

    con = review_word_extra(m_num, score)
    ntags = 100

    okt = Okt() # konlpy의 Okt객체
    count = Counter(okt.nouns(con))
    # con에서 명사만 분리/추출 후 count에 숫자와 함께 저장

    return_list = []  # 명사 빈도수 저장할 변수

    for n, c in count.most_common(ntags):
         if n != '영화':
            temp = (n, c)
            return_list.append(temp)
    # print("return_list " , return_list )


    # 조사등을 제거하는 것 stop_words
    # stop_words = ['.', '(', ')', ',', "'", '%', '-', 'X', ').', '×','의','자','에','안','번',
    #  '호','을','이','다','만','로','가','를','액','세','제','위','월','수','중','것','표','명']
    # ko = [each_word for each_word in ko if each_word not in stop_words]

    if con != "리뷰 없음":
        return return_list
    else:
        return_list = {("이 점수의 리뷰 없음" , 1)}
        return return_list


# 워드클라우드
def review_wordcloud(m_num):
    return_list = review_wordcount(m_num)

    wordcloud = WordCloud(font_path='c:/Windows/Fonts/malgun.ttf',
                          relative_scaling=0.2,
                          background_color='white', ).generate_from_frequencies(dict(return_list))

# 워드클라우드(점수별)
def review_wordcloud(m_num, score):
    # 시작 점수와 끝점수 넣기 추가??
    try:
        if int(score) <= 10 and int(score) >=1 and score%1 == 0:
            return_list = review_wordcount(m_num, score)
        elif int(score) == 0:
            review_wordcloud_all(m_num)
        else:
            print('점수 스코어 불량')
            return None

        wordcloud = WordCloud(font_path='c:/Windows/Fonts/malgun.ttf',
                              relative_scaling=0.2,
                              background_color='white', ).generate_from_frequencies(dict(return_list))
        return wordcloud
    except:
        print('입력값 불량')


# 워드클라우드(점수별) 보여주기
def review_wordcloud_show(m_num, score):
    wordcloud = review_wordcloud(m_num, score)
    plt.figure(figsize=(32, 24))
    plt.imshow(wordcloud)
    plt.axis('off')
    plt.show()


# 워드클라우드(점수별/전부)
# 수정중 (워드 클라우드 크기조절 - 크기가 클 경우 잘 안된다.)
def review_wordcloud_all(m_num):

    pltshow = plt.figure(figsize=(32, 24))
    axes = []

    for a in range(2 * 5):
        print(a+1,'점 생성 중')
        return_list = review_wordcount(m_num, a+1)
        wordcloud = WordCloud(font_path='c:/Windows/Fonts/malgun.ttf',
                              relative_scaling=0.2,
                              background_color='white', ).generate_from_frequencies(dict(return_list))
        axes.append(pltshow.add_subplot(2, 5, a + 1))
        subplot_title = (str(a+1))    # 타이틀 설정
        axes[-1].set_title(subplot_title)   # 타이틀 표시
        plt.axis('off')
        plt.imshow(wordcloud)
    plt.gcf().set_size_inches(20, 20)   # 완성 후 크기 지정
    plt.show()





    # 리뷰 저장용 csv 타이틀 만들기
    # title = get_review_title(m_num)
    # 이름 저장용 공백 제거 및 특수문자 제거
    # title = title.replace(' ', '_')
    # title = title.replace('\\', '_')
    # title = title.replace('/', '_')
    # title = title.replace(':', '')
    # title = title.replace('*', '_')
    # title = title.replace('?', '_')
    # title = title.replace('"', '_')
    # title = title.replace('<', '_')
    # title = title.replace('>', '_')
    # title = title.replace('|', '_')


def sample(m_num):
    url = "https://movie.naver.com/movie/bi/mi/basic.nhn?code=" + m_num
    breq = requests.get(url)
    bsoup = BeautifulSoup(breq.content, 'html.parser')

    story0 = bsoup.select('.story_area')
    story1 = bsoup.select('.story_area .h_tx_story')[0].text
    story = bsoup.select('.story_area .con_tx')[0].text
    print(story)
    return 1














# 함수
# 영화 목록 크롤링 / def get_review_score(m_num):
# 총 리뷰건수 따오기 / def get_review_allnum(m_num):
# 영화 제목 따오기 / def get_review_title(m_num):
# 리뷰 따오고 저장 / def get_review(m_num):
# 리뷰파일이 최신 파일인지 확인 / review_datecheck(m_num, check):

# 리뷰파일에서 문자 추출 / def review_word_extra(m_num):
# 리뷰 내용 점수별로 나누기 (1~10) / def review_word_extra_score(m_num , score):
# 형태소 분석 밑 단어 사용 빈도 추출 / def review_wordcount(m_num):
# 형태소 분석 밑 단어 사용 빈도 추출(점수별) / def review_wordcount(m_num, score):
# 워드클라우드 / def review_wordcloud(m_num):
# 워드클라우드(점수별) / def review_wordcloud(m_num, score):
# 워드클라우드(점수별) 보여주기 / def review_wordcloud_show(m_num, score):
# 워드클라우드(점수별/전부) / def review_wordcloud_all(m_num):







# 실행부

# movie_num = str(185917)       # 반도
movie_num = str(188909)       # 강철비2
# movie_num = str(155665)       # 강철비
# movie_num = str(194799)       # 빅샤크3: 젤리몬스터 대소동
# movie_num = str(182234)       # 오케이 마담
# movie_num = str(136315)       # 어벤져스: 인피니티 워
# movie_num = str(136900)       # 어벤져스: 엔드게임
# movie_num = str(98438)       # 어벤져스: 에이지 오브 울트론
# movie_num = str(10010)       # 슈퍼맨 3



# 과거 파일을 쓰는 중
# review_datecheck(m_num, 0)


# review_wordcloud_all(movie_num)
sample(movie_num)





# 추가할 것

# ppt 파일 수정
# 웹에 연결

# 줄거리의 문장을 형태소 분석 후 참고 키워드 출력
# 점수별 보여주기 일때 옆에 순위 표시
# 초기화 함수 만들기
# 영화목록 크롤링 수정

# 날짜별 비교 후 그 날짜 이후만 추가하기
# 날짜로 추출하기 / 날짜별 리뷰수 변화 만들기
# 날짜별로 추출하기 / 날짜별 형태소 순위 5개

# 파일로 만들거나 웹에 연결
# 파일또는 웹에 진행도 출력


# 영화 목록 크롤링 중
# for movie_num in range(10000, 200000):
#     m_num = str(movie_num)
#     get_review_score(m_num)
#     if movie_num%20 == 0:
#         percent = movie_num / 200000 * 100
#         print("200000 중", movie_num, "번째 저장성공 / 진행도 -  %.2f %%" % percent)




