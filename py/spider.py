# -*- coding:utf-8 -*-
import pymysql
import urllib.request
from bs4 import BeautifulSoup as Bs
import time
import os

base_base_url = r'http://m.biquge.com.tw'
base_url = base_base_url+'/wapbook/{}.html'
agent = r'Mozilla/5.0 (Windows NT 10.0; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0'
accept = r'text/html'
language = r'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2'
cookie = r'''__cfduid=d9d3836ec64483d6cb057a4692af5f4421516533097; 
        UM_distinctid=161186abd4a2f7-085c625e783fbf8-12676c4a-100200-161186abd4b23b; 
        PHPSESSID=g1msj424iorepjcm1129v461j5; CNZZDATA1271224740=1245634237-1516836108-%7C1516836108; 
        yjs_id=63f163bfb605bf919d16f0ac1a0e417a; ctrl_time=1'''
connection = r'keep-alive'
requests = r'1'
header = {'Host': 'm.biquge.com.tw',
          'User-Agent': agent,
          'Accept': accept,
          'Accept-Language': language,
          'Referer': base_base_url,
          'Cookie': cookie,
          'Connection': connection,
          'Upgrade-Insecure-Requests': requests}
clazz = {'类型：玄幻小说': 1, '类型：修真小说': 5, '类型：都市小说': 6, '类型：历史小说': 4, '类型：网游小说': 3, '类型：科幻小说': 2, '类型：恐怖小说': 7, '类型：其他小说': 8}


def connect_sql(sql, si='insert'):
    """
    写入数据库
    :param sql: sql 语句
    :return:
    """
    db = pymysql.connect('地址',
                         '用户名',
                         '密码',
                         'books',
                         use_unicode=True,
                         charset="utf8")

    cursor = db.cursor()
    if si == 'insert'or si == 'update':
        try:
            cursor.execute(sql)
            db.commit()
        except Exception as e0:
            err(e0)
            db.rollback()
    elif si == 'select':
        try:
            cursor.execute(sql)
            return cursor.fetchall()
        except Exception as e0:
            err(e0)
    db.close()


def get_html(url):
    """
    根据url获取html
    :param url: 目标地址
    :return html: html代码
    """
    req = urllib.request.Request(url=url, headers=header)
    response = urllib.request.urlopen(req)
    html = response.read().decode('gb18030', 'ignore')
    return html


def get_book(html):
    """
    获取book 信息
    :param html:book目录地址
    :return book字典: book{}
    """
    book = {'name': '书名',
            'description': '描述',
            'author': '作者',
            'clazz': '分类',
            'preview_url': '最新章节',
            'image': '封面'}
    soup = Bs(html, 'html.parser')
    book['name'] = soup.select_one('h3').get_text()
    book['description'] = soup.select_one('div[class="intro"]').select_one('p').get_text()
    info = soup.select_one('div[class="infotype"]').select('p')
    book['author'] = info[0].get_text()[3:]
    book['clazz'] = clazz[info[1].get_text()]
    book['preview_url'] = base_base_url+info[3].select_one('a')['href']
    book['image'] = soup.select_one('img')['src']
    return book


def get_content(chapter_url):
    """
    获取章节内容
    :param chapter_url: 章节地址
    :return: 章节字典chapter{}
    """
    chapter = {'title': '章节标题',
               'preview_url': '上一章url',
               'content': '内容'}
    html = get_html(chapter_url)
    soup = Bs(html, 'html.parser')
    chapter['title'] = soup.select_one('h1').get_text()
    chapter['preview_url'] = base_base_url+soup.select_one('p[class="p1"]').select_one('a')['href']
    chapter['content'] = soup.select_one('div[id="novelcontent"]').select_one('p').get_text()
    return chapter


def write_book(book_id, chapter_id, chapter):
    """
    存储章节
    :param book_id:
    :param chapter_id:
    :param chapter:
    :return:
    """
    with open(r'{}/{}.txt'.format(book_id, chapter_id), 'w', encoding='utf8') as t:
        t.write('{}\n {}'.format(chapter['title'], chapter['content']))


def get_str_time():
    """
    获得当前时间字符串
    :return:
    """
    return str(time.asctime(time.localtime(time.time())))


def err(ep, p=0):
    """
    记录异常
    :param ep:
    :param p:
    :return:
    """
    with open('log_1.txt', 'a', encoding='utf8') as error:
        msg = 'Error: {} \ntime: {}\n id={} \n\n'
        error.write(msg.format(str(ep), get_str_time(), p))


def init(x):
    """
    入口
    :param x:
    :return:
    """
    html = get_html(base_url.format(x))
    item = get_book(html)
    if not os.path.exists(str(x)):
        os.mkdir(str(x))
    book_insert_sql = 'INSERT INTO book(book_id,author,clazz,book_name,description,image,last_url,update_time) VALUES("{}","{}","{}","{}","{}","{}","{}","{}")'.format(
                x, item['author'], item['clazz'], item['name'], item['description'], item['image'],
                item['preview_url'], get_str_time())
    connect_sql(book_insert_sql)
    chapter_id = 1
    while len(item['preview_url']) > (37 + len(str(x))):
        try:
            item = get_content(item['preview_url'])
            write_book(x, chapter_id, item)
            chapter_id += 1
        except Exception as e:
            err(e, x)
            time.sleep(10)
            break


if __name__ == "__main__":
    os.chdir('/usr/txt')
    x, count, z = 0, 25, 0
    with open('flag.txt') as f:
        flag = f.readline()
        x = int(flag)
    with open('flag.txt', 'w') as ff:
        ff.write(str(x+25))
    while z < 25:
        z += 1
        try:
            init(x+z)
        except Exception as e:
            err(e, x+z)
            time.sleep(10)
            continue



