package unibg.accessibilitytestgenerator;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Contains the methods used to generate the test files
 */
public class TestCaseGenerator {


    private static String uiautomatorName = "UIAccessibilityTests";


    /**
     * Generates the java file containing the test cases for a certain component.
     * It fills the template file with the information about the component that needs to be tested.
     * @param appPackage package of the tested app
     * @param node node that needs to be tested
     * @param fileName name of the generated java file
     * @param statusNumber number of the status of the node
     * @throws IOException
     */
    public static void generateTestCase(String appPackage, Node node, String fileName,int statusNumber) throws IOException {

        //get template test from the file in UIAccessibilityTests
        String template = getTemplate();

        template = template.replaceAll("testcase_name", fileName);

        template = template.replaceAll("package_name", appPackage);

        template = template.replaceAll("class_name", node.getClassName());


        String textCond;

        if (node.getText() == null)
            textCond = "obj.getText()==null";
        else
            textCond = "obj.getText()!=null && obj.getText().equals(\"" + node.getText() + "\")";

        template = template.replaceAll("conditionText", textCond);


        String resCond;

        if (node.getResourceName() == null)
            resCond = "obj.getResourceName()==null";
        else
            resCond = "obj.getResourceName()!=null && obj.getResourceName().equals(\"" + node.getResourceName() + "\")";

        template = template.replaceAll("conditionRes", resCond);


        String packCond;

        if (node.getApplicationPackage() == null)
            packCond = "obj.getApplicationPackage()==null";
        else
            packCond = "obj.getApplicationPackage()!=null && obj.getApplicationPackage().equals(\"" + node.getApplicationPackage() + "\")";

        template = template.replaceAll("conditionPack", packCond);


        String descCond;

        if (node.getContentDesc() == null)
            descCond = "obj.getContentDescription()==null";
        else
            descCond = "obj.getContentDescription()!=null && obj.getContentDescription().equals(\"" + node.getContentDesc() + "\")";

        template = template.replaceAll("conditionContentDesc", descCond);


        String boundsCond;

        if (node.getBounds() == null)
            boundsCond = "obj.getVisibleBounds()==null";
        else
            boundsCond = "obj.getVisibleBounds()!=null && obj.getVisibleBounds().toString().equals(\"" + node.getBounds().toString() + "\")";

        template = template.replaceAll("conditionBounds", boundsCond);


        //steps to get to the right status
        if(statusNumber==0){

            template = template.replaceAll("transitions_to_node", " ");
        }
        else{
            StringBuilder builder = new StringBuilder();
            List<Transition> transitions = ListGraph.getPath(statusNumber);
            for(Transition t:transitions){

                //populate textview
                builder.append(" List<UiObject2> editTextViews = mDevice.findObjects(By.clazz(\"android.widget.EditText\")); \n");
                builder.append("if (editTextViews.size() != 0) {\n");
                builder.append(" for (UiObject2 obj : editTextViews) {\n");
                builder.append(" obj.setText(\"test\");\n");
                builder.append(" }\n");
                builder.append("}\n");

                switch (t.getAction()){

                    case CLICK: builder.append("mDevice.click(" + t.getNode().getBounds().centerX() + ", " + t.getNode().getBounds().centerY() + "); \n");
                        builder.append("mDevice.waitForWindowUpdate(PACKAGE_NAME, " + 5000 + ");");




                }
            }
            template = template.replaceAll("transitions_to_node", builder.toString());
        }

        printTestCase(appPackage, template, fileName);
    }



    private static String getTemplate() {
        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        File dir = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(dir + "/" + uiautomatorName  + "/", "template");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return text.toString();
    }



    private static void printTestCase(String appPackage, String testCase, String fileName) throws IOException {

        File file = new File(Environment.getExternalStorageDirectory()  + "/" + uiautomatorName  + "/" + appPackage + "/");
        file.mkdirs();

        File output = new File(file, fileName+ ".java");
        FileOutputStream fos = new FileOutputStream(output);
        byte[] data = testCase.getBytes();
        fos.write(data);
        fos.flush();
        fos.close();
    }
}
