# -*- coding: utf-8 -*-
#!/usr/bin/env python


import csv

from selenium import webdriver
from selenium.webdriver.support.ui import Select, WebDriverWait
from selenium.common.exceptions import NoSuchElementException
import time

indice=[]
dados=[]
tabela1=[]
tabela1_aumentada=[]
tabela2=[]
tabela2_aumentada=[]
browser = webdriver.Chrome()


from selenium.common.exceptions import NoSuchElementException        
def check_exists_by_xpath(xpath):
    try:
        browser.find_element_by_xpath(xpath)
    except NoSuchElementException:
        return False
    return True

for i in range(1,30000):
	browser.get('http://www.cartasocial.pt/resultados_pesquisadetalhe.php?equip=' + str(i))
	
	if check_exists_by_xpath('//*[@id="corpo2as"]/table/tbody/tr[3]/td[2]'):
		nome=browser.find_element_by_xpath('//*[@id="corpo2as"]/table/tbody/tr[1]/td/div').text
		morada = browser.find_element_by_xpath('//*[@id="corpo2as"]/table/tbody/tr[3]/td[2]').text
		cod_postal= browser.find_element_by_xpath('//*[@id="corpo2as"]/table/tbody/tr[4]/td[2]').text
		telefone_fax= browser.find_element_by_xpath('//*[@id="corpo2as"]/table/tbody/tr[5]/td[2]').text
		email=browser.find_element_by_xpath('//*[@id="corpo2as"]/table/tbody/tr[6]/td[2]').text
		ent_prop=browser.find_element_by_xpath('//*[@id="corpo2as"]/table/tbody/tr[7]/td[2]').text
		nat_juridica=browser.find_element_by_xpath('//*[@id="corpo2as"]/table/tbody/tr[8]/td[2]').text
		dados.append([i,nome,morada,cod_postal,telefone_fax,email,ent_prop,nat_juridica])
		
		print('id: '+ str(i))
		print('nome: ' + nome)
		print(morada)
		print(cod_postal)
		print(telefone_fax)
		print(email)
		print(ent_prop)
		print(nat_juridica)

		if check_exists_by_xpath('//*[@id="corpo2as"]/div/table[1]/tbody/tr[3]/td[1]'):
			

			if check_exists_by_xpath('//*[@id="corpo2as"]/div/table[1]/tbody/tr[3]/td[1]'):
				if check_exists_by_xpath('//*[@id="cabresp7"]'):
					for tr in browser.find_elements_by_xpath('//*[@id="corpo2as"]/div/table[1]/tbody/tr')[2:]:
						tds = tr.find_elements_by_tag_name('td')
						if tds: 
							print([td.text for td in tds])

				else:
					for tr in browser.find_elements_by_xpath('//*[@id="corpo2as"]/div/table[1]/tbody/tr')[1:]:
						tds = tr.find_elements_by_tag_name('td')
						if tds:
							tabela1.append([td.text for td in tds])					
					for x in range(len(tabela1)):
						tabela1_aumentada.append([str(i)] + tabela1[x])
					tabela1.clear()


			if check_exists_by_xpath('//*[@id="corpo2as"]/div/table[2]/tbody/tr[2]/td'):
				for tr in browser.find_elements_by_xpath('//*[@id="corpo2as"]/div/table[2]/tbody/tr')[1:]:
					tds = tr.find_elements_by_tag_name('td')
					if tds: 
						tabela2.append([td.text for td in tds])	
				for x in range(len(tabela2)):
					tabela2_aumentada.append([str(i)] + tabela2[x])
				tabela2.clear()

print(dados)
print()
print()
print(tabela1_aumentada)
print()
print(tabela2_aumentada)

with open('/Users/luisjorge/software/entidadesAAAAA.csv', 'a+') as outcsv:   
    writer = csv.writer(outcsv, delimiter=';', quotechar='|', quoting=csv.QUOTE_MINIMAL, lineterminator='\n')
    writer.writerow(['id','nome','morada', 'cod_postal','telefone','email','entidade_roprietaria','natureza_juridica'])
    for dado in dados:
        	writer.writerow([dado[0],dado[1],dado[2],dado[3],dado[4],dado[5],dado[6], dado[7]])


with open('/Users/luisjorge/software/tabela1AAAAA.csv', 'a+') as outcsv1:   
    writer = csv.writer(outcsv1, delimiter=';', quotechar='|', quoting=csv.QUOTE_MINIMAL, lineterminator='\n')
    writer.writerow(['id','resposta_social','capacidade', 'utentes','horario','ultima_atualizaçao'])
    writer.writerows(tabela1_aumentada)

with open('/Users/luisjorge/software/tabela2AAAAA.csv', 'a+') as outcsv2:   
    writer = csv.writer(outcsv2, delimiter=';', quotechar='|', quoting=csv.QUOTE_MINIMAL, lineterminator='\n')
    writer.writerow(['id','outras_atividades'])
    writer.writerows(tabela2_aumentada)

 
