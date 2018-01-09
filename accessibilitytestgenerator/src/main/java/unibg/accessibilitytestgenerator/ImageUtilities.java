package unibg.accessibilitytestgenerator;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class ImageUtilities {

    private static double contrastRatio(int color1, int color2){
        double contrastRatio;
        float l1= myLuminance(color1);
        float l2=myLuminance(color2);
        if(l1>l2){
            contrastRatio=(l1+0.05)/(l2+0.05);
        }
        else {
            contrastRatio=(l2+0.05)/(l1+0.05);
        }



        return contrastRatio;
    }

    private static float myLuminance(int color){

        double red = Color.red(color)/255;
        double blue = Color.blue(color)/255;
        double green = Color.green(color)/255;

        if(red<=0.03928){
            red = red/12.92;
        }
        else{
            red = Math.pow((red+0.055)/1.055, 2.4);
        }

        if(blue<=0.03928){
            blue = blue/12.92;
        }
        else{
            blue = Math.pow((blue+0.055)/1.055, 2.4);
        }


        if(green<=0.03928){
            green = green/12.92;
        }
        else{
            green = Math.pow((green+0.055)/1.055, 2.4);
        }

        return (float) ((0.2126 * red) + (0.7152 * green) + (0.0722 * blue));
    }

}