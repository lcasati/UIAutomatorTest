package unibg.uiautomatortest.unused;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class TestCaseGenerator {


    public static String uiautomatorName = "UIAutomatorTest";


    public static void generateTestCase(String appPackage, Node node, String fileName, List<Transition> transitions) throws IOException {


        String template = getTemplate();

        // TODO: NOME DEL TEST

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


        // TODO: TRANSIZIONI PER ARRIVARE ALLO STATUS

        if(transitions.isEmpty()){

            template = template.replaceAll("transitions_to_node", " ");
        }
        else{
            StringBuilder builder = new StringBuilder();
            for(Transition t:transitions){
                switch (t.getAction()){

                    case CLICK: builder.append("mDevice.click(" + node.getBounds().centerX() + ", " + node.getBounds().centerY() + "); \n");
                                builder.append("mDevice.waitForWindowUpdate(" + appPackage + ", " + 5000 + ");");


                }
            }
            template = template.replaceAll("transitions_to_node", builder.toString());
        }

        printTestCase(appPackage, template, fileName);
    }


    public static void generateTestCase(String appPackage, Node node, String fileName,int statusNumber) throws IOException {


        Log.d("CASO DI TEST", appPackage + "\n" + "STATUS NUMERO" + Integer.toString(statusNumber) + "\n" + node.toString());

        String template = getTemplate();

        // TODO: NOME DEL TEST

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


        // TODO: TRANSIZIONI PER ARRIVARE ALLO STATUS

        if(statusNumber==0){

            template = template.replaceAll("transitions_to_node", " ");
        }
        else{
            StringBuilder builder = new StringBuilder();
            List<Transition> transitions = ListGraph.getPath(statusNumber);
            for(Transition t:transitions){
                switch (t.getAction()){

                    case CLICK: builder.append("mDevice.click(" + node.getBounds().centerX() + ", " + node.getBounds().centerY() + "); \n");
                        builder.append("mDevice.waitForWindowUpdate(" + appPackage + ", " + 5000 + ");");


                }
            }
            template = template.replaceAll("transitions_to_node", builder.toString());
        }

        printTestCase(appPackage, template, fileName);
    }

    /*
    *
    *
    *
    * */


    private static String getTemplate() {
        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        File dir = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(dir + "/" + uiautomatorName  + "/", "template.txt");

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
