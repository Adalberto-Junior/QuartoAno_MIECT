# -*- coding: utf-8 -*-
#!/usr/bin/env python

import sys
#reload(sys)
#sys.setdefaultencoding( "utf-8" )
import re
import csv


from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import StaleElementReferenceException

from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

data = []

browser = webdriver.Chrome()

for i in range(0,5):
	
	browser.get('https://www.cartasocial.pt/resultados-da-pesquisa?p_p_id=SocialLetterPortlet_WAR_cartasocialportlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_SocialLetterPortlet_WAR_cartasocialportlet__facesViewIdRender=%2Fviews%2FsocialLetter%2Flist%2Fview%2Fequipment%2Fequipment_maps.xhtml&_SocialLetterPortlet_WAR_cartasocialportlet_idEquipment=' + str(i))
	src = browser.page_source
	print(":::::::::::::::::::::::::::::::::::::::::::::::::")
	#print(src)
	#with open("Logg.txt","a",encoding="UTF-8") as f:
	#	f.write(src)
	#	f.write("\n")
	
	lat = re.search(r'google.maps.LatLng\((.*)\,\s', src)
	lon = re.search(r'\,\s(.*)\);', src)
	print("lon: ",lon)
	latitude_element  = browser.find_element(By.CSS_SELECTOR,"coords")
	longitude_element = browser.find_element(By.CSS_SELECTOR,"LatIng")
	print("Lati: ", latitude_element)
	if lat is not None and lon is not None:
#		print(lat.group(1))
		lon = re.search(r'\,\s(.*)\);', src)
		print("------------------------------------------------------")
		print ('id: '+ str(i) + '  lat: ' + lat.group(1) + '  lon: ' + lon.group(1))
		data.append([i,lat.group(1),lon.group(1)])
	
for coord in data:
	print([coord[0],coord[1],coord[2]])
	print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")


with open('./coordenadas_carta_social.csv', 'a+') as outcsv: 
    print("???????????????????????????????????????")
    #configure writer to write standard csv file
    writer = csv.writer(outcsv, delimiter=';', quotechar='|', quoting=csv.QUOTE_MINIMAL, lineterminator='\n')
    writer.writerow(['id','lat', 'lon'])
    for coord in data:
        #Write item to outcsv
        	writer.writerow([coord[0],coord[1],coord[2]])