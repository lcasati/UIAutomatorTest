package unibg.uiautomatortest.unused;


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

    public static double contrastRatio(String filename, Rect bounds){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filename, options);


        int left = bounds.left - 3 <= 0 ? 0 : bounds.left - 3;
        int top = bounds.top - 3 <= 0 ? 0 : bounds.top - 3;
        int right = bounds.right + 3 >= bitmap.getWidth() ? bitmap.getWidth() - bounds.right : bounds.right + 3;
        int bottom = bounds.bottom + 3 >= bitmap.getHeight() ? bitmap.getHeight() - bounds.bottom : bounds.bottom + 3;


        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, bounds.left, bounds.top, bounds.width(), bounds.height());

        Bitmap topBackground;
        int topbackgroundSize;

        if(top==bounds.top){
            topBackground=null;
            topbackgroundSize=0;
        }
        else{

            topBackground= Bitmap.createBitmap(bitmap,bounds.left,top,bounds.width(),bounds.top - top);
            topbackgroundSize = topBackground.getWidth() * topBackground.getHeight();
        }

        Bitmap leftBackground;
        int leftbackgroundSize;

        if(left==bounds.left){
            leftBackground=null;
            leftbackgroundSize=0;
        }
        else{
            leftBackground = Bitmap.createBitmap(bitmap,left,bounds.top,bounds.left - left,bounds.height());
            leftbackgroundSize = leftBackground.getWidth() * leftBackground.getHeight();
        }

        Bitmap rightBackground;
        int rightbackgroundSize;

        if(right == bounds.right){
            rightBackground=null;
            rightbackgroundSize=0;
        }
        else{
            rightBackground =  Bitmap.createBitmap(bitmap,bounds.right,bounds.top, right - bounds.right, bounds.height());
            rightbackgroundSize = rightBackground.getWidth() * rightBackground.getHeight();
        }

        Bitmap bottomBackground;
        int bottombackgroundSize;

        if(bottom == bounds.bottom){
            bottomBackground=null;
            bottombackgroundSize=0;
        }
        else {
            bottomBackground = Bitmap.createBitmap(bitmap, bounds.left, bounds.bottom,bounds.width(), bottom - bounds.bottom);
            bottombackgroundSize = bottomBackground.getWidth() * bottomBackground.getHeight();
        }

        int[] pixels =  new int[croppedBitmap.getHeight() * croppedBitmap.getWidth()];
        croppedBitmap.getPixels(pixels,0,croppedBitmap.getWidth(),0,0,croppedBitmap.getWidth(),croppedBitmap.getHeight());

        int foregroundMeanPixel = getDominantColor(croppedBitmap);

        Log.d("FOREGROUND MEAN COLOR" , Integer.toString(foregroundMeanPixel));


        int topbackgroundColor = getDominantColor(topBackground);
        Log.d("TOP BACKGROUND COLOR", Integer.toString(topbackgroundColor));

        int leftbackgroundColor = getDominantColor(leftBackground);
        Log.d("LFFT BACKGROUND COLOR", Integer.toString(leftbackgroundColor));

        int rightbackgroundColor = getDominantColor(rightBackground);
        Log.d("RIGHT BACKGROUND COLOR", Integer.toString(rightbackgroundColor));

        int bottombackgroundColor = getDominantColor(bottomBackground);
        Log.d("BOTTOM BACKGROUND COLOR", Integer.toString(bottombackgroundColor));


        int backgroundSize = topbackgroundSize + leftbackgroundSize + rightbackgroundSize + bottombackgroundSize;



        int redBucket = ((topbackgroundColor >> 16) & 0xFF) * topbackgroundSize + ((leftbackgroundColor >> 16) & 0xFF) * leftbackgroundSize + ((rightbackgroundColor >> 16) & 0xFF) * rightbackgroundSize + ((bottombackgroundColor >> 16) & 0xFF) * bottombackgroundSize;
        int greenBucket = ((topbackgroundColor >> 8) & 0xFF) * topbackgroundSize + ((leftbackgroundColor >> 8) & 0xFF) * leftbackgroundSize + ((rightbackgroundColor >> 8) & 0xFF) * rightbackgroundSize + ((bottombackgroundColor >> 8) & 0xFF) * bottombackgroundSize;
        int blueBucket = (topbackgroundColor & 0xFF) * topbackgroundSize + (leftbackgroundColor & 0xFF) * leftbackgroundSize + (rightbackgroundColor & 0xFF) * rightbackgroundSize + (bottombackgroundColor & 0xFF) * bottombackgroundSize;


        int backgroundMeanPixel = Color.rgb(redBucket/backgroundSize, greenBucket/backgroundSize, blueBucket/backgroundSize);

        Log.d("BACKGROUND MEAN COLOR" , Integer.toString(backgroundMeanPixel));

        double rBg = Color.red(backgroundMeanPixel) <= 10 ? Color.red(backgroundMeanPixel)/3294 : Math.pow(Color.red(backgroundMeanPixel)/269 + 0.0513,2.4);
        double gBg = Color.green(backgroundMeanPixel) <= 10 ? Color.green(backgroundMeanPixel)/3294 : Math.pow(Color.green(backgroundMeanPixel)/269 + 0.0513,2.4);
        double bBg = Color.blue(backgroundMeanPixel) <= 10 ? Color.blue(backgroundMeanPixel)/3294 : Math.pow(Color.blue(backgroundMeanPixel)/269 + 0.0513,2.4);


        double lBg = 0.2126 * rBg + 0.7152 * gBg + 0.0722 * bBg;

        double rFg = Color.red(foregroundMeanPixel) <= 10 ? Color.red(foregroundMeanPixel)/3294 : Math.pow(Color.red(foregroundMeanPixel)/269 + 0.0513,2.4);
        double gFg = Color.green(foregroundMeanPixel) <= 10 ? Color.green(foregroundMeanPixel)/3294 : Math.pow(Color.green(foregroundMeanPixel)/269 + 0.0513,2.4);
        double bFg = Color.blue(foregroundMeanPixel) <= 10 ? Color.blue(foregroundMeanPixel)/3294 : Math.pow(Color.blue(foregroundMeanPixel)/269 + 0.0513,2.4);


        double lFg = 0.2126 * rFg + 0.7152 * gFg + 0.0722 * bFg;

        return (lBg + 0.05)/(lFg + 0.05);
    }



    public static int getDominantColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++)
        {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++)
            {
                int color = pixels[x + y * w]; // x + y * width
                redBucket += (color >> 16) & 0xFF; // Color.red
                greenBucket += (color >> 8) & 0xFF; // Color.greed
                blueBucket += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }

    public static Bitmap cropImage(String filename, Rect bounds) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filename, options);


        int left = bounds.left - 3 <= 0 ? 0 : bounds.left - 3;
        int top = bounds.top - 3 <= 0 ? 0 : bounds.top - 3;
        int width = bounds.right + 3 >= bitmap.getWidth() ? bitmap.getWidth() : bounds.width() + 6;
        int height = bounds.bottom + 3 >= bitmap.getHeight() ? bitmap.getHeight() : bounds.height() + 6;


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


        return (foregroundLum + 0.05)/(backgroundLum + 0.05);
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