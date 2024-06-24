import pandas as pd
import numpy as np
from geopy.geocoders import Nominatim
from tqdm import tqdm

# Carregar Excel
df = pd.read_excel(r"Equipamentos1.xlsx")

# Função para obter as coordenadas geográficas
def get_coordinates(address):
    geolocator = Nominatim(user_agent="geoapiExercises")
    location = geolocator.geocode(address)
    if location:
        return location.latitude, location.longitude
    else:
        return np.nan, np.nan

# Adicionar colunas para as coordenadas geográficas
df['Latitude'] = np.nan
df['Longitude'] = np.nan

# Com base nos equipamentos, obter as coordenadas geográficas
for index, row in tqdm(df.iterrows(), total=len(df)):
    address = row['Equipamentos']  # Substitua 'Endereço' pelo nome da coluna que contém os endereços dos equipamentos
    latitude, longitude = get_coordinates(address)
    df.at[index, 'Latitude'] = latitude
    df.at[index, 'Longitude'] = longitude

# Salvar o DataFrame em um novo arquivo Excel
df.to_excel('equipamentos_com_coordenadas.xlsx', index=False)




