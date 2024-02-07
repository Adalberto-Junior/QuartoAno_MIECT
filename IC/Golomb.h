#include <iostream>
#include <opencv2/opencv.hpp>
#include <vector>
#include <fstream>
#include <bitset>
#include <iostream>

#include "BitStream.h"

using namespace std;


class Golomb {
public:
    Golomb(int m, bool useSignAndMagnitude) : m(m), useSignAndMagnitude(useSignAndMagnitude) {}


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


    // Function to encode an integer and return the corresponding bit sequence
    void encode(const std::string& inputImage, cv::Mat image) {
        std::vector<int> encodedBits;
        BitStream bs ("output.bin", "w");

        std::ifstream inputFile(inputImage, std::ios::binary);

        std::string format;
        int width, height, maxColor;
        inputFile >> format >> width >> height >> maxColor;

        inputFile.get(); // Read the newline character.
        
        std::string binaryString;

        // write format- ascii code translated to binary
        for (std::size_t i = 0; i < format.size(); ++i)
        {
            cout << bitset<16>(format.c_str()[i]) << endl;
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

        int predictedValue = 0;
        for(int nRows = 0; nRows < image.rows; nRows++){
            for(int nCol = 0; nCol < image.cols; nCol++){
                int pixel=0;
                if(nRows == 0){
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

                pixel = getPixel(nCol, nRows, image) - predictedValue;

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

            }
        }
        for(int i =0; i<encodedBits.size(); i++){
            bs.write_bit(encodedBits[i]);
        }
        
        bs.close();
    }

    // Function to decode a bit sequence into an integer
    void decode(std::vector<int> bits) {
        int idx = 0;
        int fLength = 0;
   
        for (int i = 0; i < fLength; i++){ 
            std::cout << bits[i];
        }
    
        // Decode the sign bit
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

        

        //return isNegative ? ((result % 2 == 0) ? -result / 2 : (result + 1) / -2) : result;
}



        

private:
    int m;  // Parameter for Golomb coding
    bool useSignAndMagnitude;  // Flag to indicate the chosen approach for negative numbers
};