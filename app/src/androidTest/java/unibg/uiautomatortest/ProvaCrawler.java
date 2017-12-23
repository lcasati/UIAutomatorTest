package unibg.uiautomatortest;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
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
public class ProvaCrawler {

    private static final String CACIUPPO_PACKAGE
            = "unibg.caciuppo";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice mDevice;
    private List<WindowStatus> statuses;
    private List<String> classes = new ArrayList<>();
    int i=0;

    @Before
    public void startMainActivityFromHomeScreen() {


        // Checked widgets
        classes.add("android.widget.TextView");

        statuses = new ArrayList<>();

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

        List<UiObject2> lista =  mDevice.findObjects(By.pkg(CACIUPPO_PACKAGE));
        WindowStatus status1 = new WindowStatus(lista);
        statuses.add(status1);

        // TODO: CONTROLLA ECCEZIONI
        crawl(status1);

    }

    public void crawl(WindowStatus status) throws IOException {

        for(Node node:status.getNodes()){

            if(classes.contains(node.getClassName())){
                TestCaseGenerator.generateTestCase(CACIUPPO_PACKAGE,node, "Test" + i, status.getTransitions() );
                i++;
            }


            if(node.isClickable()){
                mDevice.click(node.getBounds().centerX(),node.getBounds().centerY());
                mDevice.waitForWindowUpdate(CACIUPPO_PACKAGE,LAUNCH_TIMEOUT);
                List<UiObject2> afterList =  mDevice.findObjects(By.pkg(CACIUPPO_PACKAGE));
                WindowStatus statusAfter = new WindowStatus(afterList);
                if(!statuses.contains(statusAfter)){
                    statusAfter.getTransitions().addAll(status.getTransitions());
                    statusAfter.getTransitions().add(new Transition(UIActions.CLICK,node));
                    statuses.add(statusAfter);
                    crawl(statusAfter);
                }

            }

           if(node.isScrollable()){

           }


           if(node.isLong_clickable()){

           }

        }

        closeAndOpenApp();

    }

    private void closeAndOpenApp(){

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
