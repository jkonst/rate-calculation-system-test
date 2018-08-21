package main;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by JK on 8/21/2018.
 * This is for personal use. All Rights Reserved.
 */
public class MainAppTest {

    @Test
    public void mainDialogTest() {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        MainApp.calculator = null;
        MainApp.exit = true;
        MainApp.mainDialog(absolutePath, "1000");
        Assert.assertTrue(MainApp.calculator.getFailResponse().isEmpty());
    }
    @Test
    public void mainDialogTest1() {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        MainApp.calculator = null;
        MainApp.exit = true;
        MainApp.mainDialog(absolutePath, "400");
        Assert.assertNull(MainApp.calculator);
    }

}
