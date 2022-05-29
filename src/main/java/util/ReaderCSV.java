package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReaderCSV {

    public static List<List<String>> getRecords(String path, String delimiter){
        var file = getResourceFile(path);

        var records = new ArrayList<List<String>>();

        try(var scanner = new Scanner(file)){
            while(scanner.hasNext()){
                records.add(getRecordFromLine(scanner.nextLine(), delimiter));
            }
        } catch (FileNotFoundException exception){
            System.out.println("File doesn't exist: " + path);
        }
        return records;
    }

    private static File getResourceFile(String path){
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        try{
            return new File(url.toURI());
        } catch (Exception e){
            System.out.println("URI converting problem");
            return null;
        }
    }

    private static List<String> getRecordFromLine(String line, String delimiter){
        var values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(delimiter);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}
