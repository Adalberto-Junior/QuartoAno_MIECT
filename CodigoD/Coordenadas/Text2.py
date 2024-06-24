import pandas as pd
import numpy as np
from geopy.geocoders import Nominatim
from tqdm import tqdm


def readFile():

    # Carregar Excel
    df = pd.read_excel(r"Equipamentos2.xlsx")
    # faz o return do ficheiro;
    return df

# Função para obter as coordenadas geográficas
def get_coordinates(address):
    geolocator = Nominatim(user_agent="GoogleMapsGeocoding API", timeout = 10)
    
    #geoapiExercises
    location = geolocator.geocode(address)
    #print("location: ",location)
    if location:
        #print(" Location:",location.latitude)
        return location.latitude, location.longitude
    else:
        #print("UPS! Naods")
        return np.nan, np.nan
    

def main():
    df = readFile()

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
        df.to_excel('equipamentos_com_coordenadas2.xlsx', index=False)


if __name__=="__main__":
    main()




