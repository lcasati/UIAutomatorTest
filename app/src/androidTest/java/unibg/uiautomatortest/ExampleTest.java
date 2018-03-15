package unibg.uiautomatortest;

import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import unibg.accessibilitytestgenerator.testgeneration.ATG;

import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ExampleTest {


   @Test
    public void test(){
        ATG atg = new ATG("unibg.caciuppo");
        atg.addStringToView("editText", "Cassandra");
        atg.generateTestCases();

    }




}
