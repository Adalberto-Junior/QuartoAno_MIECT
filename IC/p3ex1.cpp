#include <iostream>
#include <opencv2/opencv.hpp>
#include "Golomb.h"

int main(int argc, char* argv[]) {

    if (argc != 5) {
        std::cerr << "Usage: " << argv[0] << " input_image output_image m magAndSign" << std::endl;
        return 1;
    }

    const char* inputImage = argv[1];
    const char* outputImage = argv[2];
    int m = atoi(argv[3]);
    int magAndSign = atoi(argv[4]);


    
    cv::Mat image = cv::imread(inputImage, cv::IMREAD_COLOR); // Read the file in color mode
    cv::cvtColor(image, image, cv::COLOR_BGR2GRAY); // convert to grayscale

    Golomb g { m , magAndSign};

    g.encode(inputImage, image);



}
    