#include <iostream>
#include <opencv2/opencv.hpp>
#include <vector>
#include <fstream>
#include <bitset>
#include <iostream>
#include <bitset>
#include <string>
#include <vector>
#include <cmath>


#include "BitStream.h"

using namespace std;


class Golomb {
public:
    Golomb(int m, bool useSignAndMagnitude) : m(m), useSignAndMagnitude(useSignAndMagnitude) {}
    Golomb(){}

    int getPixel(int nCol , int nRows, cv::Mat image){

        if (nRows >= 0 && nCol >= 0 && nRows < image.rows && nCol < image.cols) {
        // Pixels within valid range
        return static_cast<int>(image.at<uchar>(nRows, nCol));
        } else if (nRows < 0) {
            // Edge case: Negative row index
            return 0;  // Return black color for negative row index
        } else if (nCol < 0) {
            // Edge case: Negative column index
            return image.at<uchar>(0, nCol);  // Access the first column pixel
        } else if (nRows >= image.rows) {
            // Edge case: Beyond top border
            return image.at<uchar>(image.rows - 1, nCol);  // Access the last row pixel
        } else if (nCol >= image.cols) {
            // Edge case: Beyond right border
            return image.at<uchar>(nRows, image.cols - 1);  // Access the last column pixel
        } else {
            std::cerr << "Error: pixel out of bounds" << std::endl;
        }
    }


    int predict(int left, int above, int diagAbove) {
        // Use the median predictor when above and diagonal-above pixels are available
        std::vector<int> values = {left, above, diagAbove};
        std::sort(values.begin(), values.end());
        
        return values[1];  // Return the median value
    }
    vector<int> encodePFrame(const char* inputImage, cv::Mat currentFrame, int blockSize, int searchArea) {
        vector<int> encodedBits;

        // Read the reference frame (previous frame)
        cv::Mat referenceFrame = cv::imread(inputImage, cv::IMREAD_GRAYSCALE);

        if (referenceFrame.empty()) {
            std::cerr << "Error: Could not read the reference frame." << std::endl;
            return encodedBits;
        }

        // Iterate over blocks in the current frame
        for (int y = 0; y < currentFrame.rows; y += blockSize) {
            for (int x = 0; x < currentFrame.cols; x += blockSize) {
                // Extract the current block from the current frame
                cv::Mat currentBlock = currentFrame(cv::Rect(x, y, blockSize, blockSize));

                // Search for the best matching block in the reference frame
                // This is a simple example using sum of absolute differences (SAD) as the matching criterion
                int bestMatchX = x;
                int bestMatchY = y;
                int minSAD = std::numeric_limits<int>::max();

                for (int dy = -searchArea; dy <= searchArea; ++dy) {
                    for (int dx = -searchArea; dx <= searchArea; ++dx) {
                        int refX = x + dx;
                        int refY = y + dy;

                        if (refX >= 0 && refX + blockSize <= referenceFrame.cols &&
                            refY >= 0 && refY + blockSize <= referenceFrame.rows) {
                            // Extract the reference block
                            cv::Mat referenceBlock = referenceFrame(cv::Rect(refX, refY, blockSize, blockSize));

                            // Calculate the sum of absolute differences (SAD)
                            int sad = cv::norm(currentBlock, referenceBlock, cv::NORM_L1);

                            // Update the best match if the current block has a lower SAD
                            if (sad < minSAD) {
                                minSAD = sad;
                                bestMatchX = refX;
                                bestMatchY = refY;
                            }
                        }
                    }
                }

                // Encode motion vector (you need to decide how to represent and encode it)
                // For simplicity, let's assume a simple representation using integers
                int motionVectorX = bestMatchX - x;
                int motionVectorY = bestMatchY - y;

                // Encode the motion vector (you need to adapt this part based on your requirements)
                // For simplicity, we'll encode motion vectors as raw differences
                encodedBits.push_back(motionVectorX);
                encodedBits.push_back(motionVectorY);

                // Calculate and encode residuals (difference between current block and best-matched block)
                cv::Mat residual = currentBlock - referenceFrame(cv::Rect(bestMatchX, bestMatchY, blockSize, blockSize));

                // Encode the residuals using Golomb coding
                vector<int> residualBits = encode("residual", residual);

                // Append the encoded residuals to the output
                encodedBits.insert(encodedBits.end(), residualBits.begin(), residualBits.end());
            }
        }
        return encodedBits;
    }


    std::string encodeStr(int num, int m){
        calculateBits(m);
        std::string result = "";
        int quotient = 0;
        int remainder = 0;
        if (m != 0){
            quotient = abs(num) / m;
            remainder = abs(num) % m;
        }
    
        for (int i = 0; i < quotient; i++) {
            result += "0";
        }
        //cout<<"m: "<<m<<"; num: "<<num<<"; quoti: "<<quotient<<"; rem: "<<remainder<<endl;
        // bit (1) represent the end of the quotient
        result += "1";

        if (m != 1){
            if (remainder < n_minToBeRepresent) {
               result += remainderStr(remainder, minBits);
            } else {
                result += remainderStr(remainder + n_minToBeRepresent, maxBits);
            }
        }else{
            result += "0";
        }
        num < 0 ? result += "1" : result += "0";

        return result;
    }


    // Function to encode an integer and return the corresponding bit sequence
    vector<int> encode(const std::string& inputImage, cv::Mat image) {
        vector<int> sizest;
        std::vector<int> encodedBits;
        BitStream bs ("output.bin", "w");

        std::ifstream inputFile(inputImage, std::ios::binary);

        std::string format;
        int width, height, maxColor;
        inputFile >> format >> width >> height >> maxColor;

        inputFile.get(); // Read the newline character.
        
        std::string binaryString;
        //cerr << format << std::endl;
        //cerr << width << std::endl;
       // cerr << height << std::endl;
        vector<int> format_vec;

        // write format- ascii code translated to binary
       for (std::size_t i = 0; i < format.size(); ++i) {
        int g = static_cast<int>(format[i]); // Convert character to its ASCII value
        std::bitset<8> bits(g); // Convert ASCII value to a bitset of size 8 (assuming 8-bit ASCII)
        for (int j = bits.size() - 1; j >= 0; --j) {
            format_vec.push_back(bits[j]); // Store each bit in the vector
        }
    }

    for (int bit : format_vec) {
        bs.write_bit(bit - '0');
    }
        

        // write width
         for (int i = 15; i >= 0; i--) {
            if ((width & (1 << i)) != 0) {
                bs.write_bit('1'- '0');
            } else {
                bs.write_bit('0' -'0');
            }
        }

        // write height
         for (int i = 15; i >= 0; i--) {
            if ((height & (1 << i)) != 0) {
                bs.write_bit('1'- '0');
            } else {
                bs.write_bit('0' -'0');
            }
        }

        // write m
         for (int i = 15; i >= 0; i--) {
            if ((m & (1 << i)) != 0) {
                bs.write_bit('1'- '0');
            } else {
                bs.write_bit('0' -'0');
            }
        }

        // write sign_and_mag
         for (int i = 15; i >= 0; i--) {
            if ((useSignAndMagnitude & (1 << i)) != 0) {
                bs.write_bit('1'- '0');
            } else {
                bs.write_bit('0' -'0');
            }
        }
        int vf = 0;
        int this_size;

        int predictedValue = 0;
        int last_pixel = 0;
        for(int nRows = 0; nRows < image.rows; nRows++){
            for(int nCol = 0; nCol < image.cols; nCol++){
                int pixel=0;
                if(nRows == 0 && nCol == 0){
                    //pixel
                    pixel = getPixel(nCol, nRows, image);
                    

                }else if(nRows == 0 && nCol != 0){
                    predictedValue = getPixel(nCol - 1, nRows, image);
                }else{
                    predictedValue = predict(
                                getPixel(nCol - 1, nRows, image),
                                getPixel(nCol, nRows - 1, image),
                                getPixel(nCol - 1, nRows - 1, image)
                                );
                }
                /*if(getPixel(nCol, nRows, image) - predictedValue > 0){
                    pixel = getPixel(nCol, nRows, image) - predictedValue;
                    if(vf < 10){
                        cerr <<  predictedValue << endl;
                    }

                }
                else{
                        pixel = getPixel(nCol, nRows, image);
                        if(vf < 10){
                        cerr << "ola" << endl;
                    }


                }*/

                pixel = getPixel(nCol, nRows, image);
                
                if (pixel < 0) {
                // Handle negative numbers based on the chosen approach
                    if (useSignAndMagnitude) {
                        encodedBits.push_back(1);  // 1 represents the sign bit (negative)
                        pixel = -pixel;  // Use the magnitude for encoding
                    } else {
                        pixel = 2 * (-pixel) - 1;  // Interleave positive and negative values
                    }
                } 
                else {
                    // For positive numbers or zero
                    encodedBits.push_back(0);  // 0 represents the sign bit (positive or zero)
                    }

                // Calculate the unary part
                int quotient = pixel / m;
                for (int i = 0; i < quotient; ++i) {
                    encodedBits.push_back(0);
                }
                encodedBits.push_back(1);  // Separator bit

                // Calculate the binary part
                int remainder = pixel % m;
                for (int i = m - 2; i >= 0; --i) {
                    encodedBits.push_back((remainder >> i) & 1);
                    

                }
                if(sizest.size() == 0){
                sizest.push_back(encodedBits.size());
                this_size = encodedBits.size();
                //cerr << encodedBits.size() << endl;
                }
                else{
                    sizest.push_back(encodedBits.size()-this_size);
                    this_size = encodedBits.size();
                }

                if(vf < 10){
                   cerr << getPixel(nCol, nRows, image)  << endl;
                   //last_pixel = getPixel(nCol, nRows, image);
                
                }

                vf++;

            }
        }
        //cerr << sizest.size() << endl;
       cerr << last_pixel  << endl;
        for(int i =0; i<encodedBits.size(); i++){
            
         bs.write_bit(encodedBits[i]);
        }

        
        bs.close();

        return sizest;
    }

    // Function to decode a bit sequence into an integer

    int do_the_decoding(int idx, vector<int> bits, int m){

        bool isNegative = bits[idx++];

        // Decode the unary part
        int quotient = 0;
        while (bits[idx] == 0) {
            quotient++;
            idx++;
        }
        idx++; // Skip the separator bit

        // Decode the binary part
        int remainder = 0;
        for (int i = 0; i < m - 1; ++i) {
            remainder = (remainder << 1) | bits[idx++];
        }

        int result = quotient * m + remainder;

        

        return isNegative ? ((result % 2 == 0) ? -result / 2 : (result + 1) / -2) : result;


    }

 void createPPMImage(const std::vector<int>& pixels, int width, int height, const std::string& filename) {
    cv::Mat image(height, width, CV_8UC3);

    // Fill the image with pixel data
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            size_t index = y * width + x;
            if (index < pixels.size()) {
                image.at<cv::Vec3b>(y, x) = cv::Vec3b(pixels[index], pixels[index], pixels[index]);
            } else {
                std::cerr << "Not enough pixel data for the specified image dimensions." << std::endl;
                return;
            }
        }
    }

    // Write the image to a PPM file
    cv::imwrite(filename, image);
    std::cout << "PPM image created: " << filename << std::endl;
}

    void decode(vector<int> sizest) {

        BitStream inputFile ("output.bin", "r", "none") ;
        vector<int> bits = inputFile.readBits(inputFile.getFileSize() * 8);
        inputFile.close1();
        int p = 0;
        vector<int> aux;
        std::string format = "";
        int width = 0;
        int height = 0;
        int maxColor = 0;
        int m = 0;
        int useSignAndMagnitude = 0;
        //std::vector<int> bitset;
        while(p < 16){
            format.push_back(bits[p]);
            //cerr << bits[p] << endl;
            p++;
        }

        /*std::string bitset_string = std::bitset(bitset[0]).to_string();
       for (std::size_t i = 1; i < bitset.size(); ++i) {
    std::bitset bitset_string = std::bitset(static_cast<int>(bitset[i]));
    int character_value = std::stoi(bitset_string);
    format += static_cast<char>(character_value);
  }*/
       // cerr << format << endl;
        aux = {};
        while(p < 32){
            aux.push_back(bits[p]);
            //cerr << bits[p] << endl;
            p++;
        }


        for (int i = 0; i < aux.size(); ++i) {
            width = (width << 1) | aux[i];
        }
       // cerr << width << endl;
        aux = {};
        while(p <  48){
            aux.push_back(bits[p]);
            p++;
        }


        for (int i = 0; i < aux.size(); ++i) {
            height = (height << 1) | aux[i];
        }

        //cerr << height << endl;

        aux = {};

        while(p <  64){
            aux.push_back(bits[p]);
            //cerr << bits[p] << endl;            
            p++;
        }


        for (int i = 0; i < aux.size(); ++i) {
            m = (m << 1) | aux[i];
        }

        //cerr << m << endl;

        aux = {};

        while(p <  80){
            aux.push_back(bits[p]);
            //cerr << bits[p] << endl;            
            p++;
        }


        for (int i = 0; i < aux.size(); ++i) {
            useSignAndMagnitude = (useSignAndMagnitude << 1) | aux[i];
        }

        cerr << useSignAndMagnitude << endl;






        // Convert the bitset to an integer and then to a character, and append to the string
            

        int e = 0;
        int first_size = 0; 
        vector<int> full_image;
        int pixel_before = 0;
        cerr << sizest.size() << endl;
        cerr << bits.size()-p  << endl;
        int op_pixel = 0;

        
        while(first_size < sizest.size()){

        int idx = 0;
        aux = {};
        int li = 0;
        if(e < 5){
        //cerr << sizest[first_size] << endl;
        }


        while(li <  sizest[first_size]){
            aux.push_back(bits[p]);
            //cerr << bits[p] << endl;
            p++;
            li++;
        }
        first_size++;
        int pixel = do_the_decoding(idx, aux, m);
       /* if(pixel_before == 0){

            pixel_before = pixel;

        }
        
        else{

            if(pixel-pixel_before > 0){
            pixel = pixel_before+pixel;
            pixel_before = pixel;
            }

        }*/
        idx++;
        full_image.push_back(pixel);

        if(e < 10){
        cerr << pixel << endl;
        }
        e++;

        }
        //cerr << full_image[full_image.size()-2] << endl;

    format = "P6";
    const std::string& filename = "output.ppm";
    cerr << "ola|"  << endl;

    // Create the PPM image file
    createPPMImage(full_image, width, height, filename);


        
}



        

private:
    int m;  // Parameter for Golomb coding
    bool useSignAndMagnitude;  // Flag to indicate the chosen approach for negative numbers
    int maxBits = 0;
    int minBits = 0;
    int n_minToBeRepresent = 0;
    void calculateBits(int ml) {
        if (ml != 0){
            maxBits = ceil(log2(ml));
            minBits = maxBits - 1;
            n_minToBeRepresent = pow(2, maxBits) - ml;
        }

    }
    std::string remainderStr(int num, int num_bits){
        std::string result = "";
        for (int i = 0; i < num_bits; i++) {
            result = std::to_string(num % 2) + result;
            num /= 2;
        }

        return result;
    }
    int bitStringToInt(std::string bitString) {
        int result = 0;
        for (long unsigned int i = 0; i < bitString.length(); i++){
            result = result * 2 + (bitString[i] - '0');
        }
        return result;
    }
};