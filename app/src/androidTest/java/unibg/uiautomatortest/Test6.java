import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.DisplayMetrics;
import unibg.accessibilitytestgenerator.ATGImageUtilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class Test6 {

    private static final String PACKAGE_NAME
            = "unibg.caciuppo";
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
                .getLaunchIntentForPackage(PACKAGE_NAME);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)),
                LAUNCH_TIMEOUT);


         List<UiObject2> editTextViews = mDevice.findObjects(By.clazz("android.widget.EditText")); 
if (editTextViews.size() != 0) {
 for (UiObject2 obj : editTextViews) {
 obj.setText("test");
 }
}
mDevice.click(540, 949); 
mDevice.waitForWindowUpdate(PACKAGE_NAME, 5000);


        File dir = new File(Environment.getExternalStorageDirectory() + "/UIAccessibilityTests/");
        dir.mkdirs();
        File file = new File(dir, "screenshot.png");
        mDevice.takeScreenshot(file);

        List<UiObject2> list = mDevice.findObjects(By.clazz("android.widget.ImageView"));


       for (UiObject2 obj : list) {

            boolean text = obj.getText()==null;
            boolean resourceId = obj.getResourceName()==null;
            boolean packageName = obj.getApplicationPackage()!=null && obj.getApplicationPackage().equals("unibg.caciuppo");
            boolean contentDesc = obj.getContentDescription()!=null && obj.getContentDescription().equals("Altre opzioni");
            boolean bounds = obj.getVisibleBounds()!=null && obj.getVisibleBounds().toString().equals("Rect(975, 73 - 1080, 199)");

            if (text && resourceId && packageName && contentDesc && bounds) {
                targetView = obj;
            }


        }

    }

    @Test
    public void testContentDesc() {

        String contentDesc = targetView.getContentDescription();
        String text = targetView.getText();
        assertTrue((contentDesc!= null && contentDesc.equals("")) || (text!=null && !text.equals("")));

    }

    @Test
    public void testSize() {

        if(targetView.isClickable() || targetView.isCheckable()){
            DisplayMetrics metrics = InstrumentationRegistry.getContext().getResources().getDisplayMetrics();
            int displayDpi = metrics.densityDpi;
            Rect viewModel = targetView.getVisibleBounds();
            int heightDP= (int) (Math.abs(viewModel.height())/displayDpi);
            int widthDP= (int) (Math.abs(viewModel.width())/displayDpi);
            assertFalse(heightDP<48 || widthDP<48);
        }

    }


    @Test
    public void testContrast() {

        double contrastRatio = ATGImageUtilities.contrastRatioOtsu(Environment.getExternalStorageDirectory() + "/UIAccessibilityTests/screenshots/screenshot.png", targetView.getVisibleBounds());
        File file = new File(Environment.getExternalStorageDirectory() + "/UIAccessibilityTests/screenshot.png");
        file.delete();
		assertFalse(contrastRatio < 3);
    }


}
