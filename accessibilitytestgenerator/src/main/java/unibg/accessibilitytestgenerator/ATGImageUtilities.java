package unibg.accessibilitytestgenerator;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.Arrays;

public class ATGImageUtilities {


    private static int numOfLevels=101;

    private static float getLuminance(int color) {

        double red = (double) Color.red(color) / 255;
        double blue = (double) Color.blue(color) / 255;
        double green = (double) Color.green(color) / 255;

        if (red <= 0.03928) {
            red = red / 12.92;
        } else {
            red = Math.pow((red + 0.055) / 1.055, 2.4);
        }

        if (blue <= 0.03928) {
            blue = blue / 12.92;
        } else {
            blue = Math.pow((blue + 0.055) / 1.055, 2.4);
        }


        if (green <= 0.03928) {
            green = green / 12.92;
        } else {
            green = Math.pow((green + 0.055) / 1.055, 2.4);
        }

        return (float) ((0.2126 * red) + (0.7152 * green) + (0.0722 * blue));
    }

    public static double contrastRatio(String filename, Rect bounds) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filename, options);


        int left = bounds.left + 10;
        int top = bounds.top + 15;
        int right = bounds.right - 10;
        int bottom = bounds.bottom - 15;


        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top);

        Bitmap topBackground;
        int topbackgroundSize;

        if (top == bounds.top) {
            topBackground = null;
            topbackgroundSize = 0;
        } else {

            topBackground = Bitmap.createBitmap(bitmap, left, bounds.top, right - left, 15);
            topbackgroundSize = topBackground.getWidth() * topBackground.getHeight();
        }


        Bitmap leftBackground;
        int leftbackgroundSize;

        if (left == bounds.left) {
            leftBackground = null;
            leftbackgroundSize = 0;
        } else {
            leftBackground = Bitmap.createBitmap(bitmap, bounds.left, top, 10 , bottom - top);
            leftbackgroundSize = leftBackground.getWidth() * leftBackground.getHeight();
        }


        Bitmap rightBackground;
        int rightbackgroundSize;

        if (right == bounds.right) {
            rightBackground = null;
            rightbackgroundSize = 0;
        } else {
            rightBackground = Bitmap.createBitmap(bitmap, right, top, 10, bottom-top);
            rightbackgroundSize = rightBackground.getWidth() * rightBackground.getHeight();
        }


        Bitmap bottomBackground;
        int bottombackgroundSize;

        if (bottom == bounds.bottom) {
            bottomBackground = null;
            bottombackgroundSize = 0;
        } else {
            bottomBackground = Bitmap.createBitmap(bitmap, left, bottom, right - left, 15);
            bottombackgroundSize = bottomBackground.getWidth() * bottomBackground.getHeight();
        }

        int[] pixels = new int[croppedBitmap.getHeight() * croppedBitmap.getWidth()];
        croppedBitmap.getPixels(pixels, 0, croppedBitmap.getWidth(), 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight());

        float foregroundMeanPixel = getMeanLuminance(croppedBitmap);


        float topbackgroundColor = getMeanLuminance(topBackground);


        float leftbackgroundColor = getMeanLuminance(leftBackground);


        float rightbackgroundColor = getMeanLuminance(rightBackground);


        float bottombackgroundColor = getMeanLuminance(bottomBackground);


        int backgroundSize = topbackgroundSize + leftbackgroundSize + rightbackgroundSize + bottombackgroundSize;

        float backgroundMeanPixel = ((topbackgroundColor * topbackgroundSize) + (leftbackgroundColor * leftbackgroundSize) + (rightbackgroundColor * rightbackgroundSize) + (bottombackgroundColor * bottombackgroundSize)) / backgroundSize;

        if (backgroundMeanPixel > foregroundMeanPixel) {
            return (backgroundMeanPixel + 0.05) / (foregroundMeanPixel + 0.05);
        } else {
            return (foregroundMeanPixel + 0.05) / (backgroundMeanPixel + 0.05);
        }
    }

    private static float getMeanLuminance(Bitmap image) {
        float sum = 0;
        int[] pixels = new int[image.getHeight() * image.getWidth()];
        image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            float luminance = getLuminance(pixels[i]);
            sum = sum + luminance;

        }
        return sum / pixels.length;

    }

    public static double contrastRatioOtsu(String filename, Rect bounds) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filename, options);
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, bounds.left, bounds.top, bounds.width(), bounds.height());
        int[] image = new int[croppedBitmap.getHeight() * croppedBitmap.getWidth()];
        croppedBitmap.getPixels(image, 0, croppedBitmap.getWidth(), 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight());


        // Total number of pixels [N]
        int total = image.length;

        // Allocate space for the histogram
        int[] histogram = new int[numOfLevels];
        // Compute the histogram of the grayscale input
        for (int i = 0; i < image.length; i++) {
            histogram[(int)(getLuminance(image[i])*100)]++; // [ni]
        }


        int meanTotal = 0; // [muT]
        // Compute mean value for the overall histogram
        for (int i = 0; i < numOfLevels; i++) {
            meanTotal += i * histogram[i];
        }

        int meanCurrent = 0; // [mu(k)]
        // The probability of the first class occurrence
        int omega0 = 0;
        // The probability of the second class occurrence
        int omega1 = 0;

        float maximumVariance = 0;
        int thresholdValue = 0;

        for (int k = 0; k < numOfLevels; k++) {
            // Compute omega0 for current k
            omega0 += histogram[k];
            if (omega0 == 0) {
                continue;
            }

            // Compute omega1 for current k
            omega1 = total - omega0;
            if (omega1 == 0) {
                break;
            }

            meanCurrent +=  (k * histogram[k]);

            // Mean value for class 0 [mu0]
            float mean0 = (float)meanCurrent / (float)omega0;
            // Mean value for class 1 [mu1]
            float mean1 = ((float)meanTotal - (float)meanCurrent)
                    / (float)omega1;
            // Calculate the between-class variance [sigmasquareB]
            float varianceBetween = (float) omega0 * (float) omega1
                    * (mean1 - mean0) * (mean1 - mean0);

            // Check if new maximum found
            if (varianceBetween > maximumVariance) {
                maximumVariance = varianceBetween;
                thresholdValue = k;
            }
        }


        int sum1=0;
        int total1=0;
        int sum2=0;
        int total2=0;

        for(int i=0; i<histogram.length;i++){
            if(i<=thresholdValue){
                sum1+= i * histogram[i];
                total1+=histogram[i];
            }
            else{
                sum2+= i * histogram[i];
                total2+=histogram[i];
            }
        }

        double mean1 = (double) sum1 / total1;
        double mean2 = (double) sum2/total2;

        return (mean2 + 0.05)/(mean1 + 0.05);


    }


}