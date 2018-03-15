package unibg.accessibilitytestgenerator.testgeneration;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import unibg.accessibilitytestgenerator.graph.ListGraph;
import unibg.accessibilitytestgenerator.graph.Node;
import unibg.accessibilitytestgenerator.graph.Transition;

/**
 * Contains the methods used to generate the test files
 */
 class TestCaseGenerator {


     static String uiautomatorName = "ATG";


    /**
     * Generates the java file containing the test cases for a certain component.
     * It fills the template file with the information about the component that needs to be tested.
     *
     * statusNumber -1 per generare test per la window corrente
     * @param appPackage   package of the tested app
     * @param node         node that needs to be tested
     * @param fileName     name of the generated java file
     * @param statusNumber number of the status of the node
     * @throws IOException
     */
     static void generateTestCase(String appPackage, Node node, String fileName, int statusNumber) throws IOException {

        //get template test from the file in ATG
        String template = getTemplate();

        //FILE NAME
        template = template.replaceAll("testcase_name", fileName);

        //PACKAGE NAME
        template = template.replaceAll("package_name", appPackage);

        //CLASS NAME
        template = template.replaceAll("class_name", node.getClassName());

        //LOAD APP
        if (statusNumber < 0) {
            template = template.replaceAll("start_application", "");
        } else {
            template = template.replaceAll("start_application", "        // Start from the home screen\n" +
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
                    "                LAUNCH_TIMEOUT);\n");
        }


        //OBJECT SEARCH CONDITIONS
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

        //USER STRING MAP
        if(statusNumber<0){
            template = template.replaceAll("string_to_resource_map","\n");
        }
        else{
            StringBuilder stringmapBuilder = new StringBuilder();
            Iterator it = ATG.stringMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                stringmapBuilder.append("put(\"" + pair.getKey() + "\",\"" + pair.getValue() +"\"); \n");
            }

            template = template.replaceAll("string_to_resource_map",
                    "    protected Map<String, String> stringMap = new HashMap<String, String>(){\n" +
                            "        {\n" +
                            "           " + stringmapBuilder.toString() +
                            "        }\n" +
                            "    };");
        }


        if (statusNumber < 0) {
            template = template.replaceAll("transitions_to_node", " ");
        } else {
            //steps to get to the right status
            if (statusNumber == 0) {

                template = template.replaceAll("transitions_to_node", "        populateEditText();\n");
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("        populateEditText();\n");
                List<Transition> transitions = ListGraph.getPath(statusNumber);
                for (Transition t : transitions) {

                    switch (t.getAction()) {

                        case CLICK:
                            builder.append("        mDevice.click(" + t.getNode().getBounds().centerX() + ", " + t.getNode().getBounds().centerY() + "); \n");
                            builder.append("        mDevice.waitForWindowUpdate(PACKAGE_NAME, " + 5000 + ");\n");
                            builder.append("        populateEditText();\n");

                    }
                }
                template = template.replaceAll("transitions_to_node", builder.toString());
            }
        }

        if(statusNumber<0){
            template = template.replaceAll("populate_edittext", "\n");
        }
        else{
            template = template.replaceAll("populate_edittext",
                    "    private void populateEditText() {\n" +
                    "        List<UiObject2> editTextViews = mDevice.findObjects(By.clazz(\"android.widget.EditText\"));\n" +
                    "\n" +
                    "        if (editTextViews.size() != 0) {\n" +
                    "            for (UiObject2 obj : editTextViews) {\n" +
                    "                if (stringMap.containsKey(obj.getResourceName())) {\n" +
                    "                    obj.setText(stringMap.get(obj.getResourceName()));\n" +
                    "                } else {\n" +
                    "                    obj.setText(\"" +ATG.TEST_STRING + "\");\n" +
                    "                }\n" +
                    "\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }");
        }

        printTestCase(appPackage, template, fileName);
    }


    private static String getTemplate() {
        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        File dir = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(dir + "/" + uiautomatorName + "/", "template");

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

        File file = new File(Environment.getExternalStorageDirectory() + "/" + uiautomatorName + "/" + appPackage + "/");
        file.mkdirs();

        File output = new File(file, fileName + ".java");
        FileOutputStream fos = new FileOutputStream(output);
        byte[] data = testCase.getBytes();
        fos.write(data);
        fos.flush();
        fos.close();
    }
}
