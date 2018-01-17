package unibg.uiautomatortest;

import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import unibg.accessibilitytestgenerator.ATG;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class TestApp2 {


    @Test
    public void test(){
        ATG atg = new ATG("unibg.testapp2");
        List<String> classes = new ArrayList<>();
        classes.add("android.widget.TextView");
        atg.setClassesToCheck(classes);
        atg.generateTestCases();

    }
}
