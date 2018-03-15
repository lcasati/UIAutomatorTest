package unibg.uiautomatortest;



import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import unibg.accessibilitytestgenerator.testgeneration.ATG;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class TestNBA {


    @Test
    public void test(){
        ATG atg = new ATG("com.radiosonline.radiofmitalia");
        atg.generateTestCases();

    }
}