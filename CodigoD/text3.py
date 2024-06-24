import pandas as pd
import numpy as np
from geopy.geocoders import Nominatim
from tqdm import tqdm
import time

# Arquivo Excel
df = pd.read_excel(r'C:\Users\ASUS\Documents\Equipamentos1.xlsx')

# Função para obter as coordenadas geográficas com atraso entre solicitações
def get_coordinates_with_delay(address):
    geolocator = Nominatim(user_agent="geoapiExercises")
    location = None
    while not location:
        try:
            location = geolocator.geocode(address)
        except Exception as e:
            print("Erro ao obter coordenadas:", e)
            time.sleep(1)  # Aguarda 1 segundo antes de tentar novamente
    if location:
        return location.latitude, location.longitude
    else:
        return np.nan, np.nan

# Adicionar colunas para as coordenadas geográficas
df['Latitude'] = np.nan
df['Longitude'] = np.nan

# Iterar sobre os equipamentos e obter as coordenadas geográficas com atraso entre solicitações
for index, row in tqdm(df.iterrows(), total=len(df)):
    address = row['Equipamentos']  # Substitua 'Endereço' pelo nome da coluna que contém os endereços dos equipamentos
    latitude, longitude = get_coordinates_with_delay(address)
    df.at[index, 'Latitude'] = latitude
    df.at[index, 'Longitude'] = longitude

# Salvar o DataFrame em um novo arquivo Excel
df.to_excel(r'C:\caminho\para\equipamentos_com_coordenadas.xlsx', index=False)
