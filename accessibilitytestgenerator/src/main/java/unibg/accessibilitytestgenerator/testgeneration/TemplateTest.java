package unibg.accessibilitytestgenerator.testgeneration;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Provides the method used to generate the template test file
 */
public class TemplateTest {

    /**
     * Generates the template test file, stored in ATG in the external storage
     * @throws IOException
     */
     static void createTemplate() throws IOException {

        String template = "package packageName;\n" +
                "\n" +
                "import android.content.Context;\n" +
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
                "import unibg.accessibilitytestgenerator.imageprocessing.ATGImageUtilities;\n" +
                "\n" +
                "import org.junit.Before;\n" +
                "import org.junit.Test;\n" +
                "import org.junit.runner.RunWith;\n" +
                "\n" +
                "import java.io.File;\n" +
                "import java.util.List;\n" +
                "import java.util.HashMap;\n"+
                "import java.util.Map;\n"+
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
                "    private final String PACKAGE_NAME\n" +
                "            = \"package_name\";\n" +
                "    private final int LAUNCH_TIMEOUT = 5000;\n" +
                "    private UiDevice mDevice;\n" +
                "    private UiObject2 targetView = null;\n" +
                "string_to_resource_map"+
                "\n" +
                "\n" +
                "    @Before\n" +
                "    public void startMainActivityFromHomeScreen() {\n" +
                "\n" +
                "        // Initialize UiDevice instance\n" +
                "        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());\n" +
                "\n" +
                "start_application" +
                "\n" +
                "\n" +
                "transitions_to_node\n" +
                "\n" +
                "\n" +
                "        File dir = new File(Environment.getExternalStorageDirectory() + \"/ATG/\");\n" +
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
                "contentDesc_test" +
                "\n" +
                "size_test" +
                "\n" +
                "contrast_test" +
                "\n" +
                "populate_edittext \n" +
                "\n" +
                "}\n";

        File file = new File(Environment.getExternalStorageDirectory()  + "/" + TestCaseGenerator.uiautomatorName + "/");
        file.mkdirs();

        File output = new File(file, "template");
        FileOutputStream fos = new FileOutputStream(output);
        byte[] data = template.getBytes();
        fos.write(data);
        fos.flush();
        fos.close();

    }
}