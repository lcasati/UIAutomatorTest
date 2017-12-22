package unibg.uiautomatortest;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtilities {

    private final static int RADIX = 256;
    private static int threshold;

    public static Bitmap cropImage(String filename, Rect bounds) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filename, options);


        int left = bounds.left - 3 <= 0 ? 0 : bounds.left - 3;
        int top = bounds.top - 3 <= 0 ? 0 : bounds.top - 3;
        int width = bounds.right + 3 >= bitmap.getWidth() ? bitmap.getWidth() : bounds.width() + 6;
        int height = bounds.bottom + 3 >= bitmap.getHeight() ? bitmap.getHeight() : bounds.height() + 6;
        ;

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, left, top, width, height);
        return croppedBitmap;
    }


    public static double contrastRatio(Bitmap image) {

        Bitmap grayscale = toGrayscale(image);
        int[] pixels = new int[grayscale.getWidth()*grayscale.getHeight()];
        grayscale.getPixels(pixels,0,grayscale.getWidth(),0,0,grayscale.getWidth(),grayscale.getHeight());
        threshold(pixels);
        int[] histogram = histogram(pixels);

        double backgroundLum=0.0;
        int backPixel=0;
        double foregroundLum=0.0;
        int forePixel=0;

        for(int i=0;i<histogram.length;i++){
            if(i<=threshold){
                backgroundLum+=i*histogram[i];
                backPixel+=histogram[i];
            }
            else {
                foregroundLum+=i*histogram[i];
                forePixel+=histogram[i];
            }
        }

        backgroundLum=backgroundLum/backPixel;
        Log.d("backgroundlum", Double.toString(backgroundLum));
        foregroundLum=foregroundLum/forePixel;
        Log.d("foregroundlum", Double.toString(foregroundLum));


        return foregroundLum/backgroundLum;
    }


    private static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * Runs Otsu's method.
     * @param pixels
     */
    protected static void threshold(int[] pixels) {
        // create a histogram out of the pixels
        int[] n_t = histogram(pixels);

        // get sum of all pixel intensities
        int sum = sumIntensities(n_t);

        // perform Otsu's method
        calcThreshold(n_t, pixels.length, sum);
    }


    /**
     * Creates a histogram out of the pixels.
     * <P> Run-time: O(N) where N is the number of pixels.
     * @param pixels
     * @return
     */
    private static int[] histogram(int[]pixels) {
        int[] n_t = new int[RADIX];

        for (int i = 0; i < pixels.length; i++)
                n_t[Color.red(pixels[i])]++;

        return n_t;
    }


    /**
     * Returns sum of all the pixel intensities in image.
     * <P> Run time: constant (O(RADIX))
     * @param n_t
     * @return
     */
    private static int sumIntensities(int[] n_t) {
        int sum = 0;
        for (int i = 0; i < n_t.length; i++)
            sum += i * n_t[i];
        return sum;
    }


    /**
     * The core of Otsu's method.
     * <P> Run-time: constant (O(RADIX))
     */
    private static void calcThreshold(int[] n_t, int N, int sum) {
        double variance;                       // objective function to maximize
        double bestVariance = Double.NEGATIVE_INFINITY;

        double mean_bg = 0;
        double weight_bg = 0;

        double mean_fg = (double) sum / (double) N;     // mean of population
        double weight_fg = N;                           // weight of population

        double diff_means;

        // loop through all candidate thresholds
        int t = 0;
        while (t < RADIX) {
            // calculate variance
            diff_means = mean_fg - mean_bg;
            variance = weight_bg * weight_fg * diff_means * diff_means;

            // store best threshold
            if (variance > bestVariance) {
                bestVariance = variance;
                threshold = t;
            }


            // go to next candidate threshold
            while (t < RADIX && n_t[t] == 0)
                t++;
            if(t>=RADIX) break;
            mean_bg = (mean_bg * weight_bg + n_t[t] * t) / (weight_bg + n_t[t]);
            mean_fg = (mean_fg * weight_fg - n_t[t] * t) / (weight_fg - n_t[t]);
            weight_bg += n_t[t];
            weight_fg -= n_t[t];
            t++;
        }
    }

}