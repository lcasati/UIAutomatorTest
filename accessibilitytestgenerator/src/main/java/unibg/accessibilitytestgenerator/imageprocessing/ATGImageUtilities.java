package unibg.accessibilitytestgenerator.imageprocessing;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.Arrays;

/**
 * Provides methods for the evaluation of the contrast ratio of a component
 */
public class ATGImageUtilities {


    private static int numOfLevels=1001;

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



    /**
     * Returns the contrast ratio, using the Otsu threshold to establish what's in the background and what's in the foreground
     *
     * @param filename The path of the screenshot where the component is
     * @param bounds The coordinates of the component
     * @return the contrast ratio
     */
    public static double contrastRatioOtsu(String filename, Rect bounds) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filename, options);
        int left= bounds.left - 5 >= 0 ? bounds.left-5 : 0;
        int top = bounds.top - 5 >= 0 ? bounds.top - 5 : 0;
        int width = bounds.right + 5 <= bitmap.getWidth() ? bounds.right + 5 - left : bitmap.getWidth() - left;
        int height = bounds.bottom + 5 <= bitmap.getHeight() ? bounds.bottom +5 - top : bitmap.getHeight() - top;

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, left, top, width, height);
        int[] image = new int[croppedBitmap.getHeight() * croppedBitmap.getWidth()];
        croppedBitmap.getPixels(image, 0, croppedBitmap.getWidth(), 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight());


        // Total number of pixels [N]
        int total = image.length;

        // Allocate space for the histogram
        int[] histogram = new int[numOfLevels];
        // Compute the histogram of the grayscale input
        for (int i = 0; i < image.length; i++) {
            histogram[(int)(getLuminance(image[i])*1000)]++; // [ni]
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


        //calculate the mean luminance before and after the threshold

        double sum1=0;
        int total1=0;
        double sum2=0;
        int total2=0;

        for(int i=0; i<histogram.length;i++){
            if(i<=thresholdValue){
                sum1+= ((double)i/1000) * histogram[i];
                total1+=histogram[i];
            }
            else{
                sum2+= ((double)i/1000) * histogram[i];
                total2+=histogram[i];
            }
        }

        double mean1 =  sum1 / total1;
        double mean2 =  sum2/total2;

        //return the contrast ratio
        return (mean2 + 0.05)/(mean1 + 0.05);

    }


}