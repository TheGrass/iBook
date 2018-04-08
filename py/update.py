# -*- coding:utf-8 -*-
import spider
from bs4 import BeautifulSoup
import os


def err(ep, p=0, q=0):
    """
    记录异常
    :param q:
    :param ep:
    :param p:
    :return:
    """
    with open('update_log.txt', 'a', encoding='utf8') as error:
        msg = 'Error: {} \ntime: {}\n id={}->{} \n\n'
        error.write(msg.format(str(ep), spider.get_str_time(), p, q))


def get_update_tuple():
    """
    获取更新信息
    :return:
    """
    sql = 'SELECT book_id,last_url,last_id FROM book_copy'
    return spider.connect_sql(sql, si='select')


def get_soup(url):
    html = spider.get_html(url)
    soup = BeautifulSoup(html, 'html.parser')
    url = spider.base_base_url + soup.select_one('p[class="p1 p3"]').select_one('a')['href']
    return soup, url


def update(update_info):
    """
    更新
    :param update_info:
    :return:
    """
    for info in update_info:
        try:
            last_url = ''
            book_id, url, last_id, flag = info[0], info[1], info[2], 1
            while len(url) > (37 + len(str(info[0]))):
                try:
                    if flag == 1:
                        flag = 2
                        last_url = url
                        url = get_soup(url)[1]
                        print(last_url, url)
                    else:
                        last_url = url
                        title = get_soup(url)[0].select_one('h1').get_text()
                        content = get_soup(url)[0].select_one('div[id="novelcontent"]').select_one('p').get_text()
                        url = get_soup(url)[1]
                        last_id -= 1
                        print(last_url, url)
                        if not os.path.exists(str(book_id)):
                            os.mkdir(str(book_id))
                        with open(r'{}/{}.txt'.format(book_id, last_id), 'w', encoding='utf8') as t:
                            t.write('{}\n {}'.format(title, content))
                            print('ok')
                except Exception as e:
                    err(e, book_id, last_id)
                    break
            update_sql = 'UPDATE book_copy SET last_url="{}", update_time="{}", last_id="{}" WHERE (book_id="{}")'
            spider.connect_sql(update_sql.format(last_url, spider.get_str_time(), last_id, book_id), si='update')
        except Exception as e:
            err(e, info[0])
            continue


if __name__ == "__main__":
    os.chdir('/usr/txt')
    update(get_update_tuple())
