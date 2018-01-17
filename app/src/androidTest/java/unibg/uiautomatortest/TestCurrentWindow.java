package unibg.uiautomatortest;


import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import unibg.accessibilitytestgenerator.ATG;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class TestCurrentWindow {


    @Test
    public void test(){
        ATG atg = new ATG("unibg.caciuppo");
        atg.generateTestsForCurrentWindow();
    }

}
