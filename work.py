
import os, json, sys
import requests

from bs4 import BeautifulSoup
import collections
header = {'User-Agent': "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.36"}
text='amazon-gst-sale.com'
response = requests.get('https://www.scamvoid.com/check/'+text)

soup = BeautifulSoup(response.text, 'html.parser')
for i in soup.findAll('h2'):
	print(i.text)
