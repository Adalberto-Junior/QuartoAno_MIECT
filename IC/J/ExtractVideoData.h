#include <iostream>
#include <fstream>
#include <string>


class ExtractVideoData {
    public:
    //
        ExtractVideoData(const std::string& fileName){
            my_file.open(fileName, std::ios:: in | std::ios::binary);
            if(!my_file.is_open()){
                throw std::runtime_error("Error opening the file: " + fileName);
                exit(1);
            }
            std::string header;
            getline(my_file,header);
            if(header.substr(0,9) != "YUV4MPEG2"){
               throw std::runtime_error("File invalid, it's not a file YUV4MPEG2-> " + fileName);
                exit(1); 
            }else{
                format = header.substr(0,9);
            }
            char* header_cstr = new char[header.length() + 1];
            strcpy(header_cstr, header.c_str());
            //Separa os campos do cabeçalho por espaços. Ajuda imenso.
            char* token = strtok(header_cstr, " ");

            //Analisar todos os valores dos campos do cabeçalho.
            while(token != NULL){
                //o inicio de cada campo diz os dados que queremos, i.e w = width
                if(token[0] == 'W'){
                    width = atoi(token + 1);
                }
                else if(token[0] == 'H'){
                    height = atoi(token + 1);
                }
                else if (token[0] == 'F'){
                    char* fps_num = strtok(token + 1, ":");
                    char* fps_den = strtok(NULL,":");
                    //calcula o fps como inteiro, isto porque no cabeçalho aparece num:den
                    frameRate = atoi(fps_num) / atoi(fps_den);
                }
                else if(token[0] == 'C'){
                    colourSpace = atoi(token + 1);
                }
                else if(token[0] == 'A'){
                    char* aspR_num = strtok(token + 1, ":");
                    char* aspR_den = strtok(NULL,":");
                    aspectRatioNum = atoi(aspR_num);
                    aspectRatioDen = atoi(aspR_den);
                }

                token = strtok(NULL, " ");
            };
            numFrame = 0;
            while(std::getline(my_file,header)){
                if(header.find("FRAME") != std::string::npos){
                    numFrame++;
                }
            }
            std::cout << "format: "<<format <<" color: "<<colourSpace <<" wid: " << width <<" hei: " << height <<" framR: "<< frameRate <<" aspect: " << aspectRatioNum <<" apect2: "<<aspectRatioDen <<" NumF: "<< numFrame << endl;
            delete[] header_cstr;
        }
        
        std::string getFormat() const{
            return format;
        }
       int getColourSpace() const{
            return colourSpace;
        }
        int getWidth() const{
            return width;
        }
        int getHeight() const{
            return height;
        }
        int getNumFrame() const{
            return numFrame;
        }
        int getFrameRate() const{
            return frameRate;
        }
        int getAspectRatioNum() const{
            return aspectRatioNum;
        }
        int getAspectRatioDen() const{
            return aspectRatioDen;
        }
       
    private:
        std::ifstream my_file;
        std::string format;
        int colourSpace;
        int width;
        int height;
        int numFrame;
        int frameRate;
        //int bitRate;
        int aspectRatioNum;
        int aspectRatioDen;
};