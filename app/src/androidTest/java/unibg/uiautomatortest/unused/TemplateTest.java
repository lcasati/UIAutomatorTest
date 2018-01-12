package unibg.uiautomatortest.unused;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.DisplayMetrics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class TemplateTest {

    private static final String CACIUPPO_PACKAGE
            = "package_name";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;
    private UiObject2 targetView = null;


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


        List<UiObject2> list = mDevice.findObjects(By.clazz("class_name"));


       /* for (UiObject2 obj : list) {

            boolean text = conditionText;
            boolean resourceId = conditionRes;
            boolean packageName = conditionPack;
            boolean contentDesc = conditionContentDesc;
            boolean bounds = conditionBounds;

            if (text && resourceId && packageName && contentDesc && bounds) {
                targetView = obj;
            }


        }
        */
    }

    @Test
    public void testContentDesc() {

        String contentDesc = targetView.getContentDescription();
        assertTrue(contentDesc!= null && contentDesc != "");

    }

    @Test
    public void testSize() {

        // TODO: TEST DELLE DIMENSIONI - check if this is good

        DisplayMetrics metrics = InstrumentationRegistry.getContext().getResources().getDisplayMetrics();

        int displayDpi = metrics.densityDpi;
        Rect viewModel = targetView.getVisibleBounds();
        int heightDP= (int) (Math.abs(viewModel.height())/displayDpi);
        int widthDP= (int) (Math.abs(viewModel.width())/displayDpi);
        assertFalse(heightDP<48 || widthDP<48);
    }


    @Test
    public void testContrast() {

        // TODO: TEST DEL CONTRASTO - SCREENSHOT

    }


}

