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
        try (InputStream in = this.getClass().getResourceAsStream(csvFile)) {
            if (in == null) {
                throw new FileNotFoundException();
            }
            String line = DEFAULT_LINE;
            String csvSplitBy = DEFAULT_SEPARATOR;

            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))){
                while ((line = br.readLine()) != null) {
                    String[] columns = line.split(csvSplitBy);
                    //debug
                    System.out.println("Lender: " + columns[0] + " ---- " + "Rate: " + columns[1] + " ---- " + "Available: " + columns[2]);
                    this.parsedLines.add(columns);
                }
                isParsingSuccessful = true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error with finding the csv file.");
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
