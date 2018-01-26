package unibg.uiautomatortest;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Scanner;

import unibg.accessibilitytestgenerator.ATG;

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
