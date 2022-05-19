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
    private ArrayList<String> keyList = new ArrayList<>();
    private ArrayList<ArrayList<String>> csv = new ArrayList<>();
    private ArrayList<ArrayList<String>> combined = new ArrayList<>();


    public IssueDocGenerator (String path) {
        this.path = path;
        readKeyList();
        csvToMap();
        makeIssuePerProject();
    }

    public void makeIssuePerProject () {
        for (ArrayList <String> row : csv) {
            for (String str: row) {
                System.out.println(str);
            }
        }
    }

    public void readKeyList () {
        try {
//            Reader in = new FileReader("/data/CGYW/ASTChangeAnalyzer/data/apacheURLList.csv");
            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);
            boolean a = false;

            for (CSVRecord record : parser) {
                //ArrayList<String> temp = new ArrayList<>();
                int b = 0;
                for (String content:record) {
                    if (!a) {
                        a = true;
                        break;

                    }
                    if (b++ == 1) {
                        keyList.add(content);
                        System.out.println(content);
                    }

                }
//                listRead.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void csvToMap () {

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        int cnt = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                //System.out.println(file.getName());
                cnt++;

                ArrayList<String> temp = null;
                String key = "";
                try {
                        Reader in = new FileReader(path + "/" + file.getName());
                        CSVParser parser = CSVFormat.EXCEL.parse(in);
                        for (CSVRecord record : parser) {
                            temp = new ArrayList<String>();
                            temp.add(file.getName());
                            for (String str : record) {
                                temp.add(str);
                            }
                            csv.add(temp);
                        }
                        in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            }
        }
        //System.out.println(cnt);

    //}
}
