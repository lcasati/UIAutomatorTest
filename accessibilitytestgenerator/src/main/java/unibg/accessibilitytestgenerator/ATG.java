package unibg.accessibilitytestgenerator;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ATG {

    private UiDevice mDevice;
    private List<String> classes = TestCaseGenerator.classesToTest();
    private static  String PACKAGE_NAME;
    private static final int LAUNCH_TIMEOUT = 5000;
    private int i=0;



    public ATG(String packageName){
        PACKAGE_NAME = packageName;
    }

    public  void generateTestCases(){
        startMainActivityFromHomeScreen();
        populateEditText();
        List<UiObject2> lista = mDevice.findObjects(By.pkg(PACKAGE_NAME));
        WindowStatus status0 = new WindowStatus(lista);

        ListGraph.addVisitedStatus(status0);
        ListGraph.status0 = status0;

        // TODO: CONTROLLA ECCEZIONI
        try {
            crawl(status0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startMainActivityFromHomeScreen()  {


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


    }


    public void crawl(WindowStatus status) throws IOException {

        populateEditText();

        for (Node node : status.getNodes()) {

            if (classes.contains(node.getClassName())) {
                TestCaseGenerator.generateTestCase(PACKAGE_NAME, node, "Test" + i, status.getNumber());
                i++;
            }


            if (node.isClickable() || node.isCheckable()) {
                status.getTransitions().add(new Transition(UIActions.CLICK, node));
            }


           /* if (node.isScrollable()) {
                status.getTransitions().add(new Transition(UIActions.LONG_CLICK, node));
            }


            if (node.isLong_clickable()) {
                status.getTransitions().add(new Transition(UIActions.SCROLL, node));
            }*/

        }

        for (Transition transition : status.getTransitions()) {
            Node node = transition.getNode();
            switch (transition.getAction()) {

                case CLICK:
                    mDevice.click(node.getBounds().centerX(), node.getBounds().centerY());
                    mDevice.waitForWindowUpdate(PACKAGE_NAME, LAUNCH_TIMEOUT);
                    populateEditText();
                    List<UiObject2> afterList = mDevice.findObjects(By.pkg(PACKAGE_NAME));
                    WindowStatus statusAfter = new WindowStatus(afterList);
                    if (!ListGraph.getVisitedStatuses().contains(statusAfter)) {

                        ListGraph.addVisitedStatus(statusAfter);
                        takeScreenshot(statusAfter);
                        List<Transition> transitionList = new ArrayList<>();

                        if (status.getNumber() != 0)
                            transitionList.addAll(ListGraph.getPath(status.getNumber()));

                        transitionList.add(transition);
                        ListGraph.addPath(statusAfter.getNumber(), transitionList);
                        crawl(statusAfter);
                    } else {

                    }
                    break;

            }
            if (!currentStatus().equals(status)) {

                comeBackToStatus(status);
            } else {

            }

        }

        //closeAndOpenApp();

    }

    private String readApkName() throws IOException {

        File dir = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(dir + "/UIAutomatorTest/apk/", "apk");

        //Read text from file
        StringBuilder text = new StringBuilder();


        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            text.append(line);
        }
        br.close();


        Log.d("APK NAME", text.toString());

        return text.toString();
    }



    private void takeScreenshot(WindowStatus status) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/UIAccessibilityTests/screenshots/");
        dir.mkdirs();
        File file = new File(dir, "status" + status.getNumber() + ".png");
        mDevice.takeScreenshot(file);
    }

    private WindowStatus currentStatus() {
        return new WindowStatus(mDevice.findObjects(By.pkg(PACKAGE_NAME)));
    }

    private void populateEditText() {
        List<UiObject2> editTextViews = mDevice.findObjects(By.clazz("android.widget.EditText"));

        if (editTextViews.size() != 0) {
            for (UiObject2 obj : editTextViews) {
                obj.setText("test");
            }
        }
    }

    private void comeBackToStatus(WindowStatus status) {

        if (!currentStatus().equals(ListGraph.status0)) {
            closeAndOpenApp();
        }

        populateEditText();
        List<Transition> transitions = ListGraph.getPath(status.getNumber());

        if (transitions != null) {
            for (Transition t : transitions) {
                Node node = t.getNode();
                switch (t.getAction()) {

                    case CLICK:
                        mDevice.click(node.getBounds().centerX(), node.getBounds().centerY());
                        mDevice.waitForWindowUpdate(PACKAGE_NAME, LAUNCH_TIMEOUT);
                        populateEditText();
                        break;
                }
            }
        }


    }

    private void closeAndOpenApp() {

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


    }



}