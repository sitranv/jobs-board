package com.jobs.sitran.helper;

import com.jobs.sitran.domain.subdocument.Panel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

public class CSVHelper {

    public static String TYPE = "text/csv";

    public static List<Panel> getPanelsFromCSV(InputStream is) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Panel> panels = new ArrayList<Panel>();

            Iterable<CSVRecord> records = csvParser.getRecords();

            for (CSVRecord record : records) {
                Panel panel = new Panel(
                        record.get("job_role"),
                        record.get("image")
                );
                panels.add(panel);
            }
            return panels;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map<String, String>> getHashtagsFromCSV(InputStream is) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Map<String, String>> hashtags = new ArrayList<>();
            Iterable<CSVRecord> records = csvParser.getRecords();

            for (CSVRecord record : records) {
                Map<String, String> hashtag = new HashMap<>();
                hashtag.put("label", record.get("hashtag"));
                hashtag.put("value", record.get("hashtag"));
                hashtags.add(hashtag);
            }
            return hashtags;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getHashtagsFromCSV1(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<String> hashtags = new ArrayList<>();
            Iterable<CSVRecord> records = csvParser.getRecords();

            for (CSVRecord record : records) {
                hashtags.add(record.get("hashtag").toUpperCase());
            }
            return hashtags;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
