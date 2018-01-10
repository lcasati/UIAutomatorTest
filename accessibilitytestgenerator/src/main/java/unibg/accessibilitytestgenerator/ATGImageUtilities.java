package unibg.accessibilitytestgenerator;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

public class ATGImageUtilities {

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


        Log.d("CONTRASTO", "background " + Float.toString(backgroundMeanPixel));
        Log.d("CONTRASTO", "foreground " + Float.toString(foregroundMeanPixel));

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


}