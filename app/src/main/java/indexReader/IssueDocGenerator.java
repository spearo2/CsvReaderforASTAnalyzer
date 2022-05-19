package indexReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

public class IssueDocGenerator {
    private String path;
    private HashMap<String, ArrayList<String>> csv = new HashMap<>();

    public IssueDocGenerator (String path) {
        this.path = path;
        csvToMap();
    }

    public void csvToMap () {

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        int cnt = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
                cnt++;
            }
        }
        System.out.println(cnt);
//        ArrayList<String> temp = null;
//        String key = "";
//        try {
//                Reader in = new FileReader(path);
//                CSVParser parser = CSVFormat.EXCEL.parse(in);
//                for (CSVRecord record : parser) {
//                    temp = new ArrayList<String>();
//                    for (String str : record) {
//                        if(str.contains("~")) {
//                            temp.add(str.replace("]]", "").trim());
//                        } else {
//                            key = str;
//                        }
//                    }
//                    csv.put(key,temp);
//            }
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    }
}
