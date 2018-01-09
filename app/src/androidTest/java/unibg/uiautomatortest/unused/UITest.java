package unibg.uiautomatortest.unused;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import unibg.uiautomatortest.ImageUtilities;
import unibg.uiautomatortest.WindowStatus;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class UITest {

    private static final String CACIUPPO_PACKAGE
            = "unibg.caciuppo";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice mDevice;




    @Before
    public void startMainActivityFromHomeScreen() {




        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());


        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(CACIUPPO_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(CACIUPPO_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);


    }

    @Test
    public void test() throws IOException, InterruptedException {


        // TODO: SELEZIONE APP

        assertEquals(mDevice.getCurrentPackageName(),CACIUPPO_PACKAGE);







        // TODO: VIEWS TESTABILI (TEXTVIEW, BUTTON,...)



        //List<UiObject2> lista = mDevice.findObjects(By.clazz(TextView.class));
        List<UiObject2> lista =  mDevice.findObjects(By.pkg(CACIUPPO_PACKAGE));
        WindowStatus status1 = new WindowStatus(lista);


        mDevice.findObject(By.descContains("Altre opzioni")).click();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mDevice.dumpWindowHierarchy(stream);
        String string = new String(stream.toByteArray());
        saveToFile(string, "prima.xml");



        mDevice.wait(Until.hasObject(By.pkg(CACIUPPO_PACKAGE).depth(1)),
                LAUNCH_TIMEOUT);

        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        mDevice.dumpWindowHierarchy(stream2);
        String string2 = new String(stream2.toByteArray());
        saveToFile(string2, "dopo.xml");


        List<UiObject2> settingsList =  mDevice.findObjects(By.pkg(CACIUPPO_PACKAGE));

        WindowStatus statusSett = new WindowStatus(settingsList);


        mDevice.pressBack();
        List<UiObject2> scamaz =  mDevice.findObjects(By.pkg(CACIUPPO_PACKAGE));
        WindowStatus statusScamaz = new WindowStatus(scamaz);


        Log.d("STATUS SCAMAZ", Boolean.toString(status1.equals(statusScamaz)));
        Log.d("STATUS SETTINGS", Boolean.toString(status1.equals(statusSett)));


        Log.d("lista vuota?", Integer.toString(lista.size()));

        for(UiObject2 obj:lista){
            if((obj.getResourceName()==null || !obj.getResourceName().startsWith("com.android.systemui")) && obj.getClassName().equals("android.widget.TextView"))
            Log.d("textview", obj.getText());
        }

        UiObject2 object2 = null;

        for(UiObject2 obj:lista){

            boolean contentDesc = obj.getContentDescription()!= null && obj.getContentDescription().equals("Hello World!");
            //boolean bounds = obj.getVisibleBounds().toString().equals("Rect(440, 977 - 640, 1028)");

            if(contentDesc){
                object2=obj;
            }


        }


        //TestCaseGenerator.generateTestCase(CACIUPPO_PACKAGE,object2);

        // TODO: APP CRAWLING



        //TODO: SCREENSHOT
        File dir = new File(Environment.getExternalStorageDirectory() + "/UIAccessibilityTests/screenshots/");
        dir.mkdirs();
        File file = new File(dir, "screenshot.png");
        mDevice.takeScreenshot(file);

        Bitmap croppedImage = ImageUtilities.cropImage(Environment.getExternalStorageDirectory() + "/testing/screenshots/screenshot.png",object2.getVisibleBounds());

        Log.d("contrast ratio", Double.toString(ImageUtilities.contrastRatio(croppedImage)));


        UiObject2 buttoobj= mDevice.findObject(By.clazz(Button.class));

        Bitmap buttonimg = ImageUtilities.cropImage(Environment.getExternalStorageDirectory() + "/testing/screenshots/screenshot.png",buttoobj.getVisibleBounds());

        Log.d("contrast ratio bottone", Double.toString(ImageUtilities.contrastRatio(buttonimg)));



        Log.d("contrast ratio bottone NUOVO METODO", Double.toString(ImageUtilities.contrastRatio(file.getAbsolutePath(),buttoobj.getVisibleBounds())));



       /* File dir2 = new File(Environment.getExternalStorageDirectory() + "/testing/viewsimg/");
        dir2.mkdirs();
        File file2 = new File(dir2, "viewimage2.png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file2);
            croppedImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        */


    }


    public void saveToFile(String text) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/testing/");
            dir.mkdirs();
            File file = new File(dir, "dump.xml");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] data = text.getBytes();
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveToFile(String text, String name) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/testing/");
            dir.mkdirs();
            File file = new File(dir, name);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] data = text.getBytes();
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
