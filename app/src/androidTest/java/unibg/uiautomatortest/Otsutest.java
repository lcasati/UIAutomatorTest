package unibg.uiautomatortest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import unibg.accessibilitytestgenerator.ATGImageUtilities;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class Otsutest {
    private static final String PACKAGE_NAME
            = "unibg.caciuppo";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;
    private UiObject2 targetView = null;

    @Test
    public void useAppContext() throws Exception {

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
                .getLaunchIntentForPackage(PACKAGE_NAME);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)),
                LAUNCH_TIMEOUT);

        File dir = new File(Environment.getExternalStorageDirectory() + "/UIAccessibilityTests/");
        dir.mkdirs();
        File file = new File(dir, "screenshot.png");
        mDevice.takeScreenshot(file);

        List<UiObject2> list = mDevice.findObjects(By.clazz("android.widget.TextView"));


        for (UiObject2 obj : list) {

            boolean text = obj.getText()!=null && obj.getText().equals("Caciuppo");
            boolean packageName = obj.getApplicationPackage()!=null && obj.getApplicationPackage().equals("unibg.caciuppo");


            if (text && packageName) {
                targetView = obj;
            }

        }

        Log.d("CONTRASTO", Double.toString(ATGImageUtilities.contrastRatioOtsu(Environment.getExternalStorageDirectory() + "/UIAccessibilityTests/screenshot.png",targetView.getVisibleBounds())));



    }

}
