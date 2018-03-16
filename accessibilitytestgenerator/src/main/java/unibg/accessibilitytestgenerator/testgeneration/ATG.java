package unibg.accessibilitytestgenerator.testgeneration;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unibg.accessibilitytestgenerator.graph.ListGraph;
import unibg.accessibilitytestgenerator.graph.Node;
import unibg.accessibilitytestgenerator.graph.Transition;
import unibg.accessibilitytestgenerator.graph.UIActions;
import unibg.accessibilitytestgenerator.graph.WindowStatus;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Provides methods for the automated generation of accessibility tests
 */
public class ATG {

    private UiDevice mDevice;
    protected static String TEST_STRING = "test";
    private String PACKAGE_NAME;
    private final int LAUNCH_TIMEOUT = 5000;
    private int testNumber = 0;
    protected static boolean contrastTest = true;
    protected static boolean sizeTest = true;
    protected static boolean contentDescTest = true;
    protected static int size =48;
    protected static int contrastRatio=3;


    //components that needs to be tested, for every component it will be generated a java file with the test cases
    private List<String> classes = new ArrayList<String>() {
        {
            add("android.widget.TextView");
            add("android.widget.ImageView");
            add("android.widget.ImageButton");
            add("android.widget.Button");
            add("android.widget.CheckedTextView");
        }
    };

    protected static Map<String, String> stringMap = new HashMap<>();

    /**
     * @param packageName The name of the package of the app under test
     */
    public ATG(String packageName) {
        PACKAGE_NAME = packageName;
        stringMap = new HashMap<>();
    }


    /**
     * Generates a java file for every component of the app that should be tested.
     * The method explores the app and every time it finds something changed in the window, it generates tests for every component.
     */
    public void generateTestCases() {
        try {
            //check if template file is already created, if not it creates it
            checkTemplate();

            //start the app that needs to be tested
            startMainActivityFromHomeScreen();

            //populate the edittext views with a test string
            populateEditText();

            List<UiObject2> lista = mDevice.findObjects(By.pkg(PACKAGE_NAME));
            WindowStatus status0 = new WindowStatus(lista);
            ListGraph.addVisitedStatus(status0);
            ListGraph.status0 = status0;
            crawl(status0);
            testNumber = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkTemplate() throws IOException {
        File file = new File(Environment.getExternalStorageDirectory() + "/"+ TestCaseGenerator.uiautomatorName +"/template");
        if (!file.exists()) {
            TemplateTest.createTemplate();
        }

    }


    /**
     * Provide the class with a custom list of classes of the component that the user wants tested
     *
     * @param classes The list of the classes
     */
    public void setClassesToCheck(List<String> classes) {
        this.classes = classes;
    }

    /**
     * Add class to the list of classes to test
     *
     * @param className name of the class
     */
    public void addClassToCheck(String className){classes.add(className);}

    /**
     * Remove class from the list of classes to test
     *
     * @param className name of the class
     */
    public void removeClassToCheck(String className){classes.remove(className);}

    /**
     * Provide the string used to populate the EditText views when exploring the app
     *
     * @param string
     */
    public void setTestString(String string) {
        TEST_STRING = string;
    }

    /**
     * Add string you want to be used in a certain view during testing
     *
     * @param idresource id of the view
     * @param string string used
     */
    public void addStringToView(String idresource, String string) {

        stringMap.put(PACKAGE_NAME + ":id/" + idresource, string);
    }

    /**
     * Generate test files for the components in the current window
     *
     * @throws IOException
     */
    public void generateTestsForCurrentWindow() {


        try {

            checkTemplate();
            mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            List<UiObject2> lista = mDevice.findObjects(By.pkg(PACKAGE_NAME));
            WindowStatus status = new WindowStatus(lista);
            for (Node node : status.getNodes()) {

                if (classes.contains(node.getClassName())) {
                    TestCaseGenerator.generateTestCase(PACKAGE_NAME, node, "Test" + testNumber, -1);
                    testNumber++;
                }

            }
            testNumber = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the value of contrastTest
     * true - generate contrast tests
     * false - not generate contrast tests
     * @param contrastTest
     */
    public static void setContrastTest(boolean contrastTest) {
        ATG.contrastTest = contrastTest;
    }

    /**
     * Set the value of contentDescTest
     * true - generate content description tests
     * false - not generate contente description tests
     * @param contentDescTest
     */
    public static void setContentDescTest(boolean contentDescTest) {
        ATG.contentDescTest = contentDescTest;
    }

    /**
     * Set the value of sizeTest
     * true - generate size tests
     * false - not generate size tests
     * @param sizeTest
     */
    public static void setSizeTest(boolean sizeTest) {
        ATG.sizeTest = sizeTest;
    }

    /**
     * Setting the size in dp used in the test, default is 48dp
     * @param size
     */
    public static void setSize(int size) {
        ATG.size = size;
    }

    /**
     * Setting the minimum contrast ratio used in the test, dafault is 3
     * @param contrastRatio
     */
    public static void setContrastRatio(int contrastRatio) {
        ATG.contrastRatio = contrastRatio;
    }

    private void startMainActivityFromHomeScreen() {


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

    /**
     * Explore the app, generate tests for every component indicated by the list of classes.
     * Every time it finds a new window status (different content never found before), it goes deeper and explores it.
     *
     * @param status
     * @throws IOException
     */
    private void crawl(WindowStatus status) throws IOException {

        //put test string into EditText views
        populateEditText();

        //for every component in the status, generates test if their classes are in the list
        for (Node node : status.getNodes()) {

            if (classes.contains(node.getClassName())) {
                TestCaseGenerator.generateTestCase(PACKAGE_NAME, node, "Test" + testNumber, status.getNumber());
                testNumber++;
            }

            //add transaction to a possible new status
            if (node.isClickable() || node.isCheckable()) {
                status.getTransitions().add(new Transition(UIActions.CLICK, node));
            }


        }

        //check the transations, if one leads to a new status explore it
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

                        //add path to get to the new status from status 0
                        List<Transition> transitionList = new ArrayList<>();
                        if (status.getNumber() != 0)
                            transitionList.addAll(ListGraph.getPath(status.getNumber()));

                        transitionList.add(transition);
                        ListGraph.addPath(statusAfter.getNumber(), transitionList);
                        crawl(statusAfter);
                    }
                    break;

            }
            if (!currentStatus().equals(status)) {

                comeBackToStatus(status);
            }

        }

        //closeAndOpenApp();
    }


    private WindowStatus currentStatus() {
        return new WindowStatus(mDevice.findObjects(By.pkg(PACKAGE_NAME)));
    }

    private void populateEditText() {
        List<UiObject2> editTextViews = mDevice.findObjects(By.clazz("android.widget.EditText"));

        if (editTextViews.size() != 0) {
            for (UiObject2 obj : editTextViews) {
                if (stringMap.containsKey(obj.getResourceName())) {
                    obj.setText(stringMap.get(obj.getResourceName()));
                } else {
                    obj.setText(TEST_STRING);
                }

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
