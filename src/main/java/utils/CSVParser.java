package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JK on 8/18/2018.
 * This is for personal use. All Rights Reserved.
 */
public class CSVParser {

    private static final String DEFAULT_SEPARATOR = ",";
    private static final String DEFAULT_LINE = "";

    private boolean isParsingSuccessful = false;
    private String csvFile;
    private List<String[]> parsedLines = new ArrayList<>();


    public CSVParser(String file) {
        csvFile = file;
    }

    public void parse() {
        readFile();
    }

    public void readFile() {
        String line = DEFAULT_LINE;
        String csvSplitBy = DEFAULT_SEPARATOR;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(csvSplitBy);
                this.parsedLines.add(columns);
            }
            isParsingSuccessful = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("The csv file was not found.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Error with opening the csv file.");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Getters - Setters

    public String getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }

    public List<String[]> getParsedLines() {
        return parsedLines;
    }

    public void setParsedLines(List<String[]> parsedLines) {
        this.parsedLines = parsedLines;
    }

    public boolean isParsingSuccessful() {
        return isParsingSuccessful;
    }
}
