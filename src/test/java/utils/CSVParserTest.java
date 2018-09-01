package utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by JK on 8/18/2018.
 * This is for personal use. All Rights Reserved.
 */
public class CSVParserTest {
    private static CSVParser parser;

    @Test
    public void parseTest() throws FileNotFoundException {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();

        parser = new CSVParser(absolutePath);
        parser.parse();
        Assert.assertTrue(parser.isParsingSuccessful());
        //csv file contains more lines than the header
        Assert.assertTrue(parser.getParsedLines().size() > 1);
    }

    @Test
    public void parseTest2() throws FileNotFoundException {
        String resourcePath = "src/main/resources/MarkeatData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();

        parser = new CSVParser(absolutePath);
        parser.parse();
        Assert.assertFalse(parser.isParsingSuccessful());
    }
}
