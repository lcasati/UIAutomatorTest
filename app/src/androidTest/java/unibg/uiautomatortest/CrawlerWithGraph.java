package unibg.uiautomatortest;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class CrawlerWithGraph {

    private static final String CACIUPPO_PACKAGE
            = "unibg.caciuppo";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;
    private List<String> classes = new ArrayList<>();
    int i = 0;

    @Before
    public void startMainActivityFromHomeScreen() {


        // Checked widgets
        classes.add("android.widget.TextView");
        classes.add("android.widget.ImageView");
        classes.add("android.widget.ImageButton");


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


    }


    @Test
    public void test() throws IOException {
        populateEditText();
        List<UiObject2> lista = mDevice.findObjects(By.pkg(CACIUPPO_PACKAGE));
        WindowStatus status0 = new WindowStatus(lista);

        ListGraph.addVisitedStatus(status0);
        ListGraph.status0=status0;

        // TODO: CONTROLLA ECCEZIONI
        crawl(status0);

    }

    public void crawl(WindowStatus status) throws IOException {

        populateEditText();

        for (Node node : status.getNodes()) {

            if (classes.contains(node.getClassName())) {
                TestCaseGenerator.generateTestCase(CACIUPPO_PACKAGE, node, "Test" + i, status.getNumber());
                i++;
            }


            if (node.isClickable()) {
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
                    mDevice.waitForWindowUpdate(CACIUPPO_PACKAGE, LAUNCH_TIMEOUT);
                    populateEditText();
                    List<UiObject2> afterList = mDevice.findObjects(By.pkg(CACIUPPO_PACKAGE));
                    WindowStatus statusAfter = new WindowStatus(afterList);
                    if (!ListGraph.getVisitedStatuses().contains(statusAfter)) {
                        Log.d("TRANSIZIONE", transition.toString());
                        ListGraph.addVisitedStatus(statusAfter);
                        List<Transition> transitionList = new ArrayList<>();

                        if(status.getNumber()!=0)
                            transitionList.addAll(ListGraph.getPath(status.getNumber()));

                        transitionList.add(transition);
                        crawl(statusAfter);
                    }
                    else{
                        Log.d("STATO UGUALE","STATO UGUALE");
                    }
                    break;

            }
            comeBackToStatus(status);
        }

        //closeAndOpenApp();

    }


    private void populateEditText(){
        List<UiObject2> editTextViews = mDevice.findObjects(By.clazz("android.widget.EditText"));

        if(editTextViews.size()!=0){
            for(UiObject2 obj:editTextViews){
                obj.setText("test");
            }
        }
    }

    private void comeBackToStatus(WindowStatus status){

        closeAndOpenApp();
        populateEditText();
        List<Transition> transitions = ListGraph.getPath(status.getNumber());

        if(transitions!=null){
            for(Transition t:transitions){
                Node node = t.getNode();
                switch (t.getAction()) {

                    case CLICK:
                        mDevice.click(node.getBounds().centerX(), node.getBounds().centerY());
                        mDevice.waitForWindowUpdate(CACIUPPO_PACKAGE, LAUNCH_TIMEOUT);
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
                .getLaunchIntentForPackage(CACIUPPO_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(CACIUPPO_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);


    }


}
