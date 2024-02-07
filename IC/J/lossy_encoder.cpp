#include "opencv2/opencv.hpp"
#include <vector>
#include <string>
//#include <string.h>
#include "Golomb.h"
#include <iostream>
#include <fstream>
#include "ExtractVideoData.h"
#include "BitStream.h"
#include "opencv2/core.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"


using namespace cv;
using namespace std;

int main( int argc, char* argv[]){

    //nonlinear predictor(Aula teorica)
    auto predict = [](int a, int b, int c){
        if (c >= max(a,b) ){
            return min(a,b);
        }
        else if(c <= min(a,b)){
            return max(a,b);
        }
        else{
            return a + b - c;
        }
    };
    //function to calculate m based on p
    //m usando a probabilidade p de encontrar um bit '0' 
    /*
    auto calc_m = [](int p) {
        return (int) - (1/log(p / (1 + p)));
        //return (int) - (log(1+p)/log(p));
    };
    */
    if (argc != 6){
        cout << "Usage: "<<argv[0] <<"<input file> <IntraFramePeriod> <Block Size> <quantization><m>" << endl;
        return 1;
    }
    FILE* inputFile = fopen(argv[1], "r");
    if (inputFile == NULL){
        cout << "Error opening the file" << endl;
        return 1;
    }
    int KIntraFrame = atoi(argv[2]);
    int blocSize = atoi(argv[3]);
    int quant = atoi(argv[4]);
    int m = atoi(argv[5]); 
    
    if(m < 1){
        cout << "m must be > 0" << endl;
    }
    
    //Ler o cabeçalho do video
    ExtractVideoData header(argv[1]);
    //char* format = header.getFormat();
    int colorSpace = header.getColourSpace();
    int width = header.getWidth();
    int height = header.getHeight();
    int aspectRatioNum = header.getAspectRatioNum();
    int aspectRatioDen = header.getAspectRatioDen();
    int numFrame = header.getNumFrame();
    int frameRate = header.getFrameRate();

    
    //int blocSize = 8; // Tamanho do bloco;
    int searchDistance = blocSize / 2;
    vector<int> encdo_Ybits, encode_Cbbits, encode_Crbits;
    //Criação de vectores
    
    vector<int> Y,U,V;
    vector<int> motionVectorXs; //x
    vector<int> motionVectorYs; //y
    //Pensando em calcular o m dinamico(Melhor Solução)
    vector<short> Ym, Cbm, Crm;

    Y = vector<int>(width * height);
    //UV:2:2;4:4,2:0
    if(colorSpace == 420){
        U = vector<int>(width * height / 4);
        V = vector<int>(width * height / 4);
    }
    else if (colorSpace == 422){
        U = vector<int>(width * height / 2);
        V = vector<int>(width * height / 2);
    }
    else if(colorSpace == 444) { 
        U = vector<int>(width * height);
        V = vector<int>(width * height);
    }
    else{
        cout << "Error Color space not supported" << endl;
        return 1;
    }
    int padWidth = width;
    int padHeight = height;
    if(width % blocSize != 0){
        padWidth = width + (blocSize - (width % blocSize));
    }
    if(height % blocSize != 0){
       padHeight = height + (blocSize - (height % blocSize)); 
    }

    int frameIdx = 0;
    int numFrames = 0;
    Mat kYmat, kUmat, kVmat;
    int totalYresid = 0;
    int totalCbresid = 0;
    int totalCrresid = 0;
    //int KIntraFrame = 5; // Index para indicar o frame a ser codificado como intra:
    bool done = false;
    int cnt = 0;
    while(numFrames <= numFrame ){
        numFrames++;
        
        for(int i = 0; i < width * height; i++){
            Y[i] = fgetc(inputFile);
            if(Y[i] < 0){ //no data
                numFrames--;
                done = true;  
                break;
            }
           
        }
        
        if(done == true)
           break;
           
        if(colorSpace == 444){
            for(int i = 0;i < width * height; i++ ){
                U[i] = fgetc(inputFile);
            }
            for(int i = 0;i < width * height; i++ ){
                V[i] = fgetc(inputFile);
            }
        }
        else if(colorSpace == 422){
            for(int i = 0;i < (width * height)/2; i++ ){
                U[i] = fgetc(inputFile);
            }
            for(int i = 0;i <(width * height)/2; i++ ){
                V[i] = fgetc(inputFile);
            }
        }
        else if(colorSpace == 420){
            for(int i = 0;i < (width * height)/4; i++ ){
                U[i] = fgetc(inputFile);
            }
            for(int i = 0;i <(width * height)/4; i++ ){
                V[i] = fgetc(inputFile);
            }
        }
        
        
        Mat YMat = Mat(height,width,CV_8UC1);
        Mat UMat;
        Mat VMat;
        if(colorSpace == 444){
            UMat = Mat(height, width, CV_8UC1);
            VMat = Mat(height, width, CV_8UC1);
        }
        else  if(colorSpace == 422){
            UMat = Mat(height, width/2, CV_8UC1);
            VMat = Mat(height, width/2, CV_8UC1);
        }
        else  if(colorSpace == 420){
            UMat = Mat(height/2, width/2, CV_8UC1);
            VMat = Mat(height/2, width/2, CV_8UC1);
        }
       //copy the Y, U, and V data into the Mat objects
        for(int i = 0; i< height; i++){
            for(int j = 0; j < width; j++){
                YMat.at<uchar>(i,j) = Y[i * width + j];
            }
        }
        if(colorSpace == 444){
            for(int i = 0; i< height; i++){
                for(int j = 0; j < width; j++){
                    UMat.at<uchar>(i,j) = U[i * width + j];
                    VMat.at<uchar>(i,j) = V[i * width + j];
                }
            }
        }
        else if(colorSpace == 422){
            for(int i = 0; i< height; i++){
                for(int j = 0; j < width/2; j++){
                    UMat.at<uchar>(i,j) = U[i * width/2 + j];
                    VMat.at<uchar>(i,j) = V[i * width/2 + j];
                }
            }
        }
        else if(colorSpace == 420){
            for(int i = 0; i< height/2; i++){
                for(int j = 0; j < width/2; j++){ 
                    UMat.at<uchar>(i,j) = U[i * width/2 + j];  
                    VMat.at<uchar>(i,j) = V[i * width/2 + j];
                }
            }
        }

        //Aqui. Criar um periodo para ser o inter frame
        if(frameIdx == 0){
            kYmat = YMat.clone();
            kUmat = UMat.clone();
            kVmat = VMat.clone();
        }
        else if(frameIdx % KIntraFrame == 0){
            kYmat = YMat.clone();
            kUmat = UMat.clone();
            kVmat = VMat.clone();
        }
        vector<int> Yresid;
        vector<int> Cbresid;
        vector<int> Crresid;
        //prediction Intra Frame beford Inter Frame
        // se for 1º frame ou se for multiplo de KIntraFrame  será codificado como intra frama
        if(frameIdx == 0 || (frameIdx % KIntraFrame == 0 )){
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    int y = YMat.at<uchar>(i,j);
                    int u = UMat.at<uchar>(i,j);
                    int v = VMat.at<uchar>(i,j);
                    int Y_error = 0;
                    int U_error = 0;
                    int V_error = 0;

                    if(i == 0 && j == 0){//primeiro pixel
                        Y_error = y;
                        U_error = u;
                        V_error = v;
                        Yresid.push_back(Y_error);
                        Cbresid.push_back(U_error);
                        Crresid.push_back(V_error);
                    }
                    else if(i == 0){
                        //primeira linha, tem uma codificação especial
                        Y_error = y - YMat.at<uchar>(i,j-1);
                        Yresid.push_back(Y_error);
                        if(colorSpace == 420 || colorSpace == 422){
                            if(j < (width/2)){
                                Cbresid.push_back(u - UMat.at<uchar>(i,j-1));
                                Crresid.push_back(v - VMat.at<uchar>(i,j-1));
                            }
                        }
                        else if(colorSpace == 444){
                            Cbresid.push_back(u - UMat.at<uchar>(i,j-1));
                            Crresid.push_back(v - VMat.at<uchar>(i,j-1));
                        }
                    }
                    else if(j == 0){
                        //  1º pixel da linha
                        Y_error = y - YMat.at<uchar>(i-1,j);
                        Yresid.push_back(Y_error);
                        if(colorSpace == 420){
                           
                            if(i <(height/2)){
                                Crresid.push_back(v - VMat.at<uchar>(i-1,j));
                                Cbresid.push_back(u - UMat.at<uchar>(i-1,j));
                            }
                        }else{
                            Cbresid.push_back(u - UMat.at<uchar>(i-1,j));
                            Crresid.push_back(v - VMat.at<uchar>(i-1,j));
                        }

                    }
                    else{
                        Y_error = y - predict(YMat.at<uchar>(i,j-1), YMat.at<uchar>(i-1,j),YMat.at<uchar>(i-1,j-1));
                        Yresid.push_back(Y_error);
                        if(colorSpace == 420){
                            if(i < (width/2) && j < (height/2)){
                                Cbresid.push_back(u - predict(UMat.at<uchar>(i,j-1),UMat.at<uchar>(i-1,j),UMat.at<uchar>(i-1,j-1)));
                                Crresid.push_back(v - predict(VMat.at<uchar>(i,j-1),VMat.at<uchar>(i-1,j),VMat.at<uchar>(i-1,j-1)));
                            }
                        }
                        else if(colorSpace == 422){
                            if(j < (width/2)){
                                Cbresid.push_back(u - predict(UMat.at<uchar>(i,j-1),UMat.at<uchar>(i-1,j),UMat.at<uchar>(i-1,j-1)));
                                Crresid.push_back(v - predict(VMat.at<uchar>(i,j-1),VMat.at<uchar>(i-1,j),VMat.at<uchar>(i-1,j-1)));
                            }
                        }
                        else if(colorSpace == 444){
                            Cbresid.push_back(u - predict(UMat.at<uchar>(i,j-1),UMat.at<uchar>(i-1,j),UMat.at<uchar>(i-1,j-1)));
                            Crresid.push_back(v - predict(VMat.at<uchar>(i,j-1),VMat.at<uchar>(i-1,j),VMat.at<uchar>(i-1,j-1)));
                        }
                    }
                }
            }    
        }
        else{
            //Inter frame : P
            Mat frameMat = Mat(padHeight, padWidth,CV_8UC3);
            Mat frame = Mat(padHeight, padWidth,CV_8UC3);
            //colsp = 420
            for(int i = 0; i < padHeight; i++){
                for(int j = 0; j < padWidth; j++){
                    frame.at<Vec3b>(i, j)[0] = YMat.at<uchar>(i, j);
                    frame.at<Vec3b>(i, j)[1] = UMat.at<uchar>(i, (j/2));
                    frame.at<Vec3b>(i, j)[2] = VMat.at<uchar>((i/2), (j/2));
                    frameMat.at<Vec3b>(i, j)[0] = kYmat.at<uchar>(i, j);
                    frameMat.at<Vec3b>(i, j)[1] = kUmat.at<uchar>(i, (j/2));
                    frameMat.at<Vec3b>(i, j)[2] = kVmat.at<uchar>((i/2), (j/2));    
                }
            }
            

            Mat frameBlock = Mat(blocSize, blocSize, CV_8UC3);
            int nBlocWid = padWidth/blocSize;
            int nBlocHei = padHeight/blocSize;
           

            //motion vactor calculation
            for(int nbw = 0; nbw < nBlocWid; nbw++ ){
                for(int nbh = 0; nbh < nBlocHei; nbh++){
                  for (int i = 0; i < blocSize; i++){
                        for (int j = 0; j < blocSize; j++){
                            frameBlock.at<Vec3b>(i, j)[0] = frame.at<Vec3b>(nbh*blocSize + i, nbw*blocSize + j)[0];
                            frameBlock.at<Vec3b>(i, j)[1] = frame.at<Vec3b>(nbh*blocSize + i, nbw*blocSize + j)[1];
                            frameBlock.at<Vec3b>(i, j)[2] = frame.at<Vec3b>(nbh*blocSize + i, nbw*blocSize + j)[2];
                        }
                    } 
                    int motionVectorX = 0;
                    int motionVectorY = 0;

                    int searchAreaTopX = nbw*blocSize - searchDistance;
                    int searchAreaTopY = nbh*blocSize - searchDistance;
                    int searchAreaBottomX = nbw*blocSize + blocSize + searchDistance;
                    int searchAreaBottomY = nbh*blocSize + blocSize + searchDistance;
                    
                    if(searchAreaTopX < 0){
                         searchAreaTopX = 0;
                    }        
                    if(searchAreaTopY < 0){
                        searchAreaTopY = 0;
                    } 
                    if(searchAreaBottomX > width){
                        searchAreaBottomX = width;
                    } 
                    /*if(searchAreaBottomY > height){
                        searchAreaBottomY = height;
                    }*/
                    int minMSE = 1000000000;
                    for (int y = searchAreaTopY; y < searchAreaBottomY; y++) {
                        for (int x = searchAreaTopX; x < searchAreaBottomX; x++) {
                            int mse = 0;
                            for (int i = 0; i < blocSize; i++) {
                                for (int j = 0; j < blocSize; j++) {
                                    int diff = abs(frameBlock.at<Vec3b>(i, j)[0] - frameMat.at<Vec3b>(y, x)[0]);
                                    mse += diff * diff;
                                }
                            }

                            if (mse < minMSE) {
                                minMSE = mse;
                                motionVectorX = x - nbw * blocSize;
                                motionVectorY = y - nbh * blocSize;
                            }
                        }
                    }

                    /*int minSum = 1000000000;
                    for (int i = nbh*blocSize - searchDistance; i <= nbh*blocSize + searchDistance; i++){
                        for (int j = nbw*blocSize - searchDistance; j <= nbw*blocSize + searchDistance; j++){
                            if(i < 0 || j < 0 || i + blocSize > padHeight || j + blocSize > padWidth){
                                continue;
                            }
                            Mat ref_block = frameMat(Rect(j, i, blocSize, blocSize));
                            int sum = 0;
                            for (int k = 0; k < blocSize; k++){
                                for (int l = 0; l < blocSize; l++){
                                    sum += abs(frameBlock.at<uchar>(k,l) - ref_block.at<uchar>(k,l));
                                }
                            }
                            if (sum < minSum){
                                minSum = sum;
                                motionVectorX = j - nbw*blocSize;
                                motionVectorY = i - nbh*blocSize;
                            }
                        }
                    }*/
                    motionVectorXs.push_back(motionVectorX);
                    motionVectorYs.push_back(motionVectorY);
                    ///////////////

                     //predict the current block using the motion vector
                    for (int i = 0; i < blocSize; i++){ //height
                        for (int j = 0; j < blocSize; j++){ //width
                            int y_error  = frame.at<Vec3b>(nbh*blocSize + i, nbw*blocSize + j)[0] - frameMat.at<Vec3b>(nbh*blocSize + i + motionVectorY, nbw*blocSize + j + motionVectorX)[0];
                            int Cb_error = frame.at<Vec3b>(nbh*blocSize + i, nbw*blocSize + j)[1] - frameMat.at<Vec3b>(nbh*blocSize + i + motionVectorY, nbw*blocSize + j + motionVectorX)[1];
                            int Cr_error = frame.at<Vec3b>(nbh*blocSize + i, nbw*blocSize + j)[2] - frameMat.at<Vec3b>(nbh*blocSize + i + motionVectorY, nbw*blocSize + j + motionVectorX)[2];
                            Yresid.push_back(y_error);
                            //only save the Cb and Cr residuals every 2 pixels 
                            if (i % 2 == 0 and j % 2 == 0){
                                Cbresid.push_back(Cb_error);
                                Crresid.push_back(Cr_error);
                            }
                        }
                    }
                }

            }
           

        }
      
        vector<int> Ym_vector;
        vector<int> Cbm_vector;
        vector<int> Crm_vector;
        string Yencod = "";
        string Uencod = "";
        string Vencod = "";
        //Calacular o m.
        /*
        for (long unsigned int i = 0; i < Yresid.size(); i++) {
            if (i % blocSize == 0 and i != 0) {
                int sum = 0;
                for (long unsigned int j = i - blocSize; j < i; j++) sum += abs(Yresid[j]);
                int prob = sum / blocSize;
                int m = calc_m(prob);
                if (m == 0) m = 1;
                Ym_vector.push_back(m);
                if (i < Cbresid.size()) {
                    int sumCb = 0;
                    int sumCr = 0;
                    for (long unsigned int j = i - blocSize; j < i; j++) {
                        sumCb += abs(Cbresid[j]);
                        sumCr += abs(Crresid[j]);
                    }
                    int probCb = sumCb / blocSize;
                    int probCr = sumCr / blocSize;
                    int mCb = calc_m(probCb);
                    int mCr = calc_m(probCr);
                    if (mCb == 0) mCb = 1;
                    if (mCr == 0) mCr = 1;
                    Cbm_vector.push_back(mCb);
                    Crm_vector.push_back(mCr);
                }
            }
            if (i == Yresid.size() - 1) {
                int sum = 0;
                for (long unsigned int j = i - (i % blocSize); j < i; j++) sum += abs(Yresid[j]);
                
                int prob = sum / (i % blocSize);
                int m = calc_m(prob);
                if (m == 0) m = 1;
                Ym_vector.push_back(m);
            }

            if (i == Cbresid.size() - 1) {
                int sumCb = 0;
                int sumCr = 0;
                for (long unsigned int j = i - (i % blocSize); j < i; j++) {
                    sumCb += abs(Cbresid[j]);
                    sumCr += abs(Crresid[j]);
                }
                int probCb = sumCb / (i % blocSize);
                int probCr = sumCr / (i % blocSize);
                int mCb = calc_m(probCb);
                int mCr = calc_m(probCr);
                if (mCb == 0) mCb = 1;
                if (mCr == 0) mCr = 1;
                Cbm_vector.push_back(mCb);
                Crm_vector.push_back(mCr);
            }
        }
        */
        
        //Encoding
        Golomb g;
        for (long unsigned int i = 0; i < Yresid.size(); i++){
        
            //Quantization
            if((frameIdx != 0) && (frameIdx % KIntraFrame != 0)){
                Yresid[i] = Yresid[i] >> quant;
            }
            Yencod += g.encodeStr(Yresid[i],m); //ALterei o Golomb
           
        }
        
        for (long unsigned int i = 0; i < Cbresid.size(); i++) {
           
            if(Cbresid[i] < 0){
                Cbresid[i] = 0;
            }
            Uencod += g.encodeStr(Cbresid[i], m);
            
            if(Crresid[i] < 0){
                Crresid[i] = 0;
            }
            Vencod += g.encodeStr(Crresid[i], m);
        }
        totalYresid  += Yresid.size();
        totalCbresid += Cbresid.size();
        totalCrresid += Crresid.size();
        for (long unsigned int i = 0; i < Yencod.length(); i++)
            encdo_Ybits.push_back(Yencod[i] - '0');
        for (long unsigned int i = 0; i < Uencod.length(); i++)
            encode_Cbbits.push_back(Uencod[i] - '0');
        for (long unsigned int i = 0; i < Vencod.length(); i++)
            encode_Crbits.push_back(Vencod[i] - '0');

        frameIdx++;
    }


    string motionXencoded = "";
    string motionYencoded = "";
    Golomb g;
    cout<< "Golomb"<< endl;
    for(long unsigned int i = 0; i < motionVectorXs.size(); i++){
        motionXencoded += g.encodeStr(motionVectorXs[i], 8);
    }
    for(long unsigned int i = 0; i < motionVectorYs.size(); i++){
        motionYencoded += g.encodeStr(motionVectorYs[i], 8);
    }
    vector<int> encoded_motionXbits;
    vector<int> encoded_motionYbits;
    for (long unsigned int i = 0; i < motionXencoded.length(); i++){
        encoded_motionXbits.push_back(motionXencoded[i] - '0');
    }   
    for (long unsigned int i = 0; i < motionYencoded.length(); i++){
        encoded_motionYbits.push_back(motionYencoded[i] - '0');
    }
    cout<< "BSsssss"<< endl;
    BitStream bs("lossy_video_encode.bin", "w");
    vector<int> bits;

    for(int i = 15; i >= 0; i--){
        bits.push_back((width >> i) & 1); 
    }                    
    for(int i = 15; i >= 0; i--){
        bits.push_back((height >> i) & 1); 
    }
    for(int i = 15; i >= 0; i--){
        bits.push_back((numFrames >> i) & 1);  
    }
    for(int i = 15; i >= 0; i--){
        bits.push_back((colorSpace >> i) & 1); 
    }
    for(int i = 15; i >= 0; i--){
        bits.push_back((aspectRatioNum >> i) & 1);  
    } 
    for(int i = 15; i >= 0; i--){
        bits.push_back((aspectRatioDen >> i) & 1); 
    }
    for(int i = 15; i >= 0; i--){
        bits.push_back((frameRate >> i) & 1);
    }
    for(int i = 15;  i >= 0; i--){
        bits.push_back((blocSize >> i) & 1); 
    }
    for(int i = 15; i >= 0; i--){
        bits.push_back((searchDistance >> i) & 1);
    } 
    for(int i = 15; i >= 0; i--){
        bits.push_back((quant >> i) & 1);
    }
    for(int i = 15; i >= 0; i--){
        bits.push_back((KIntraFrame >> i) & 1);  
    }
    for(int i = 15; i >= 0; i--){
        bits.push_back((m >> i) & 1);  
    }
    for(int i = 15; i >= 0; i--){
        bits.push_back((padWidth >> i) & 1);
    }      
    for(int i = 15; i >= 0; i--){
        bits.push_back((padHeight >> i) & 1); 
    }
    for(int i = 31; i >= 0; i--){
        bits.push_back((encdo_Ybits.size() >> i) & 1);
    }
    for(int i = 31; i >= 0; i--){
        bits.push_back((encode_Cbbits.size() >> i) & 1); 
    }
    for(int i = 31; i >= 0; i--){
        bits.push_back((encode_Crbits.size() >> i) & 1); 
    }
    for(int i = 31; i >= 0; i--){
        bits.push_back((encoded_motionXbits.size() >> i) & 1);
    }
    for(int i = 31; i >= 0; i--){
        bits.push_back((encoded_motionYbits.size() >> i) & 1);
    }
    for(long unsigned int i = 0; i < encoded_motionXbits.size(); i++){
        bits.push_back(encoded_motionXbits[i]);
    } 
    for(long unsigned int i = 0; i < encoded_motionYbits.size(); i++){
        bits.push_back(encoded_motionYbits[i]);
    }
    for(long unsigned int i = 0; i < encdo_Ybits.size(); i++){
        bits.push_back(encdo_Ybits[i]);
    }
    for(long unsigned int i = 0; i < encode_Cbbits.size(); i++){
        bits.push_back(encode_Cbbits[i]);
    }
    for(long unsigned int i = 0; i < encode_Crbits.size(); i++){
        bits.push_back(encode_Crbits[i]);
    }
    //write in bs
    for(auto bit : bits){
        bs.writeBit(bit - '0');
    }
    bs.close();
    fclose(inputFile);;
    return 0;


}