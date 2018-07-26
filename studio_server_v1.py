import sqlite3
import json
import pandas as pd
from pandas.io import sql
from flask import Flask,request,render_template
app = Flask(__name__)


# --------------ADDING--------------
def add(ids,name,link,logo,classs,subject,star):
    conn = sqlite3.connect('database.db')
    try:
        conn.execute('''CREATE TABLE RECORDS
            (id int primary key not null,
            name text not null,
            link text not null,
            logo text,
            class int,
            subject text,
            star int);''')
    except:
        pass
    stri = f"insert into records (id,name,link,logo,class,subject,star) values({ids},{name},{link},{logo},{classs},{subject},{star})"
    app.logger.info(stri)
    conn.execute(stri)
    conn.commit()
    c = pd.read_sql('select * from records',conn)
    d = c.to_json()
    conn.close()
    return d

def topicadd(classs,subject,response):
    conn = sqlite3.connect('database.db')
    try:
        conn.execute('''CREATE TABLE topics
            (class int,
            subject text,
            response text not null);''')
    except:
        pass
    conn.row_factory = sqlite3.Row
    stri = f"insert into topics (class,subject,response) values({classs},{subject},{response})"
    app.logger.info(stri)
    conn.execute(stri)
    conn.commit()
    c = pd.read_sql('select * from topics',conn)
    d = c.to_json()
    conn.close()
    return d

def addresource(ids,category,name,link,logo,cls,sub,star):
    conn = sqlite3.connect('database.db')
    try:
        conn.execute('''CREATE TABLE resources
            (id int primary key not null,
            name text not null,
            category text,
            link text not null,
            logo text,
            class int,
            subject text,
            star int);''')
    except:
        pass
    stri = f"insert into resources (id,name,category,link,logo,class,subject,star) values({ids},{name},{category},{link},{logo},{classs},{subject},{star})"
    app.logger.info(stri)
    conn.execute(stri)
    conn.commit()
    c = pd.read_sql('select * from resources',conn)
    d = c.to_json()
    conn.close()
    return d


# --------------DELETING-------------- 

def topicdel(classs,subject):
    conn = sqlite3.connect('database.db')
    conn.row_factory = sqlite3.Row
    stri = f"delete from topics where class={classs} and subject={subject}"
    app.logger.info(stri)
    conn.execute(stri)
    conn.commit()
    return "done"

def delete(ids):
    conn = sqlite3.connect('database.db')
    conn.row_factory = sqlite3.Row
    cursor = conn.cursor()
    s = f"delete from records where id={ids}"
    rows = cursor.execute(s)
    conn.commit()
    conn.close()
    return "done"
def resourcedel(resdel):
    conn = sqlite3.connect('database.db')
    conn.row_factory = sqlite3.Row
    cursor = conn.cursor()
    s = f"delete from resources where id={redel}"
    rows = cursor.execute(s)
    conn.commit()
    conn.close()
    return "done"

# -------------- VIEWING --------------
def views( json_str = False ):
    conn = sqlite3.connect('database.db')
    conn.row_factory = sqlite3.Row
    cursor = conn.cursor()
    rows = cursor.execute('''SELECT * from records''').fetchall()
    conn.commit()
    conn.close()
    if json_str:
        return json.dumps( [dict(ix) for ix in rows] ) #CREATE JSON

    return rows

def queryy(cls,sub):
    conn = sqlite3.connect('database.db')
    conn.row_factory = sqlite3.Row
    cursor = conn.cursor()
    s = f"select * from records where class={cls} and subject={sub}"
    rows = cursor.execute(s).fetchall()
    conn.commit()
    conn.close()
    return '{"notes":' + json.dumps( [dict(ix) for ix in rows] ) + '}'

def topicview(cls,sub):
    conn = sqlite3.connect('database.db')
    conn.row_factory = sqlite3.Row
    cursor = conn.cursor()
    s = f"select * from topics where class={cls} and subject={sub}"
    rows = cursor.execute(s).fetchall()
    conn.commit()
    conn.close()
    return json.dumps([dict(rows[0])["response"]] ).replace('["{',"").replace('}"]',"")

def resourceview(resID):
    conn = sqlite3.connect('database.db')
    conn.row_factory = sqlite3.Row
    cursor = conn.cursor()
    s = f"select * from resources where category={resID}"
    rows = cursor.execute(s).fetchall()
    conn.commit()
    conn.close()
    return '{"notes":' + json.dumps( [dict(ix) for ix in rows] ) + '}'



#                           --------------- ROUTES ----------------


# ---------COMPLETE COURSE    
@app.route('/input')
def inputscr():
    return render_template('Input.html')

@app.route('/add')
def adding():
    cls = '"'+request.args.get('class')+'"'
    ids = request.args.get('id')
    name = '"'+request.args.get('name')+'"'
    link = '"'+request.args.get('link')+'"'
    logo = '"'+request.args.get('logo')+'"'
    star = request.args.get('star')
    sub = '"'+request.args.get('subject')+'"'
    return (add(ids,name,link,logo,cls,sub,star))

@app.route('/view')
def view():
    return views(True)

@app.route('/inspect')
def inspect():
    cls = request.args.get('class')
    sub = request.args.get('subject')
    sub = '"'+sub+'"'
    return queryy(cls,sub)

@app.route('/delete')
def dele():
    ids = request.args.get('id')
    return delete(ids)

# -----------TOPICS
@app.route('/topics')
def topics():
    ids = request.args.get('id')
    return topic(ids)
    
@app.route('/addtopicx')
def addtopics():
    cls = request.args.get('class')
    sub = '"'+request.args.get('subject')+'"'
    res = '"'+request.args.get('response').replace("\r\n","").replace('"',"'")+'"'
    return topicadd(cls,sub,res)

@app.route('/addtopics')
def addtopic():
    return render_template('Input_topics.html')

@app.route('/gettopics')
def gettopics():
    cls = request.args.get('class')
    sub = '"'+request.args.get('subject')+'"'
    try:
        return topicview(cls,sub)
    except:
        return"not found"
@app.route('/deletetopics')
def deltopic():
    cls = request.args.get('class')
    sub = '"'+request.args.get('subject')+'"'
    return topicdel(cls,sub)

# ----------RESOURCES
@app.route('/addresource')
def addres():
    return render_template('Input_resources.html')

@app.route('/addresourcex')
def addresx():
    ids = request.args.get('id')
    category = '"'+request.args.get('category')+'"'
    name = '"'+request.args.get('name')+'"'
    link = '"'+request.args.get('link')+'"'
    logo = '"'+request.args.get('logo')+'"'
    cls = '"'+request.args.get('class')+'"'
    sub = '"'+request.args.get('subject')+'"'
    star = request.args.get('star')
    return addresource(ids,category,name,link,logo,cls,sub,star)

@app.route('/getresource')
def getres():
    resID = '"'+request.args.get('resID')+'"'
    return resourceview(resID)







if __name__ == '__main__':
   app.run(debug = True,host='0.0.0.0',port=80)

