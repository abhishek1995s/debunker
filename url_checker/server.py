from flask import Flask,request,jsonify,render_template,redirect, url_for
import json
import requests
app = Flask(__name__)
from bs4 import BeautifulSoup


@app.route('/web/<string:query>/', methods=['GET', 'POST'])
def querye(query):
      
       header = {'User-Agent': "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.36"}
       
       text='amazon-gst-sale.com'
       response = requests.get('https://www.scamvoid.com/check/'+query)
       r="y"
       soup = BeautifulSoup(response.text, 'html.parser')
       for i in soup.findAll('h2'):
           r=i.text
       
       if r == " Potentially Safe" :
	         r="T"
       else:
	         r="F"
       wot=""
       co=1
       created=""
       country=""    
       for m in soup.findAll('font',{'class':'text-danger'}):
    	     wot=str(m.text)
       if(wot==""):
           for m in soup.findAll('font',{'class':'text-success'})[::5]:
              wot=str(m.text)
       if(wot==""):
           co=co+1
               
               
       for q in soup.findAll('font',{'class':'text-warning'})[1::2]:
    	     created=str(q.text)
       if (created==""):
           for q in soup.findAll('font',{'class':'text-success'})[1::2]:
              created=str(q.text)#sajh
                   		
       for tr in soup.find_all('tr')[21::22]:
           tds = tr.find_all('td')
           country=str(tds[1].text)
       return r+":::"+wot+":::"+created+":::"+country
if __name__ == "__main__":
    app.run()	

