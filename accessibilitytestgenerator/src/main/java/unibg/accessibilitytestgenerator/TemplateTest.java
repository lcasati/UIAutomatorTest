package unibg.accessibilitytestgenerator;

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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class TemplateTest {


    public static void createTemplate() throws IOException {

        String template = "import android.content.Context;\n" +
                "import android.content.Intent;\n" +
                "import android.graphics.Rect;\n" +
                "import android.os.Environment;\n" +
                "import android.support.test.InstrumentationRegistry;\n" +
                "import android.support.test.filters.SdkSuppress;\n" +
                "import android.support.test.runner.AndroidJUnit4;\n" +
                "import android.support.test.uiautomator.By;\n" +
                "import android.support.test.uiautomator.UiDevice;\n" +
                "import android.support.test.uiautomator.UiObject2;\n" +
                "import android.support.test.uiautomator.Until;\n" +
                "import android.util.DisplayMetrics;\n" +
                "import unibg.accessibilitytestgenerator.ATGImageUtilities;\n" +
                "\n" +
                "import org.junit.Before;\n" +
                "import org.junit.Test;\n" +
                "import org.junit.runner.RunWith;\n" +
                "\n" +
                "import java.io.File;\n" +
                "import java.util.List;\n" +
                "\n" +
                "import static junit.framework.Assert.assertFalse;\n" +
                "import static junit.framework.Assert.assertTrue;\n" +
                "import static org.hamcrest.core.IsNull.notNullValue;\n" +
                "import static org.junit.Assert.assertThat;\n" +
                "\n" +
                "@RunWith(AndroidJUnit4.class)\n" +
                "@SdkSuppress(minSdkVersion = 18)\n" +
                "public class testcase_name {\n" +
                "\n" +
                "    private static final String PACKAGE_NAME\n" +
                "            = \"package_name\";\n" +
                "    private static final int LAUNCH_TIMEOUT = 5000;\n" +
                "    private UiDevice mDevice;\n" +
                "    private UiObject2 targetView = null;\n" +
                "\n" +
                "\n" +
                "    @Before\n" +
                "    public void startMainActivityFromHomeScreen() {\n" +
                "\n" +
                "        // Initialize UiDevice instance\n" +
                "        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());\n" +
                "\n" +
                "        // Start from the home screen\n" +
                "        mDevice.pressHome();\n" +
                "\n" +
                "        // Wait for launcher\n" +
                "        final String launcherPackage = mDevice.getLauncherPackageName();\n" +
                "        assertThat(launcherPackage, notNullValue());\n" +
                "        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),\n" +
                "                LAUNCH_TIMEOUT);\n" +
                "\n" +
                "        // Launch the app\n" +
                "        Context context = InstrumentationRegistry.getContext();\n" +
                "        final Intent intent = context.getPackageManager()\n" +
                "                .getLaunchIntentForPackage(PACKAGE_NAME);\n" +
                "        // Clear out any previous instances\n" +
                "        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);\n" +
                "        context.startActivity(intent);\n" +
                "\n" +
                "        // Wait for the app to appear\n" +
                "        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)),\n" +
                "                LAUNCH_TIMEOUT);\n" +
                "\n" +
                "\n" +
                "        transitions_to_node\n" +
                "\n" +
                "\n" +
                "        File dir = new File(Environment.getExternalStorageDirectory() + \"/UIAccessibilityTests/\");\n" +
                "        dir.mkdirs();\n" +
                "        File file = new File(dir, \"screenshot.png\");\n" +
                "        mDevice.takeScreenshot(file);\n" +
                "\n" +
                "        List<UiObject2> list = mDevice.findObjects(By.clazz(\"class_name\"));\n" +
                "\n" +
                "\n" +
                "       for (UiObject2 obj : list) {\n" +
                "\n" +
                "            boolean text = conditionText;\n" +
                "            boolean resourceId = conditionRes;\n" +
                "            boolean packageName = conditionPack;\n" +
                "            boolean contentDesc = conditionContentDesc;\n" +
                "            boolean bounds = conditionBounds;\n" +
                "\n" +
                "            if (text && resourceId && packageName && contentDesc && bounds) {\n" +
                "                targetView = obj;\n" +
                "            }\n" +
                "\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void testContentDesc() {\n" +
                "\n" +
                "        String contentDesc = targetView.getContentDescription();\n" +
                "        String text = targetView.getText();\n" +
                "        assertTrue((contentDesc!= null && contentDesc.equals(\"\")) || (text!=null && !text.equals(\"\")));\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void testSize() {\n" +
                "\n" +
                "\n" +
                "        DisplayMetrics metrics = InstrumentationRegistry.getContext().getResources().getDisplayMetrics();\n" +
                "\n" +
                "        int displayDpi = metrics.densityDpi;\n" +
                "        Rect viewModel = targetView.getVisibleBounds();\n" +
                "        int heightDP= (int) (Math.abs(viewModel.height())/displayDpi);\n" +
                "        int widthDP= (int) (Math.abs(viewModel.width())/displayDpi);\n" +
                "        assertFalse(heightDP<48 || widthDP<48);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @Test\n" +
                "    public void testContrast() {\n" +
                "\n" +
                "        double contrastRatio = ATGImageUtilities.contrastRatioOtsu(Environment.getExternalStorageDirectory() + \"/UIAccessibilityTests/screenshot.png\", targetView.getVisibleBounds());\n" +
                "        File file = new File(Environment.getExternalStorageDirectory() + \"/UIAccessibilityTests/screenshot.png\");\n" +
                "        file.delete();\n" +
                "\t\tassertFalse(contrastRatio < 3);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "}\n";

        File file = new File(Environment.getExternalStorageDirectory()  + "/UIAccessibilityTests/");
        file.mkdirs();

        File output = new File(file, "template");
        FileOutputStream fos = new FileOutputStream(output);
        byte[] data = template.getBytes();
        fos.write(data);
        fos.flush();
        fos.close();

    }
}