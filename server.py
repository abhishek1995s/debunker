from flask import Flask,request,jsonify,render_template,redirect, url_for
import json
import requests
app = Flask(__name__)
from bs4 import BeautifulSoup

@app.route("/")
def hello():
	return render_template('index.html')

@app.route('/web/<string:query>/', methods=['GET', 'POST'])
def querye(query):
      
       header = {'User-Agent': "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.36"}
       
       text='amazon-gst-sale.com'
       response = requests.get('https://www.scamvoid.com/check/'+query)

       soup = BeautifulSoup(response.text, 'html.parser')
       for i in soup.findAll('h2'):
           r=i.text
       
       return r
@app.route('/text/<string:query>/', methods=['GET', 'POST'])
def texte(query):
       header = {'User-Agent': "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.36"
    }
 
       payload = {'q' : query}
       response = requests.get('https://www.google.com/search', headers=header, params=payload)
       urls = []

       soup = BeautifulSoup(response.text, 'html.parser')
       # Search for all relevant 'h3' tags
   
       for h3 in soup.findAll('h3',{'class':'r'}):

          links = h3.find('a')
          #print(links.getText())append({'trend': trend_each})
          #urls.append([links.getText(),links.get('href')])
          urls.append({'title': links.getText(),'link':links.get('href')})

          words=['spam','fake','scam','hoax']   
          ic=1
          urls = urls[:5]
          for i in urls:
  
             response = requests.get(i['link']) 
             soup = BeautifulSoup(response.text, 'html.parser')
             if any(word in soup.text for word in words):
                 return "fake"
          return "real"
if __name__ == "__main__":
    app.run()	
