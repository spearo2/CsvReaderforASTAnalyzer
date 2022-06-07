package indexReader;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexCombiner {
    HashMap<String, ArrayList<String>> csv = new HashMap<>();
    String path;
    public IndexCombiner(String path) {
        this.path = path;
        readCSV();
        writeMap();
    }
    public void readCSV () {
        try {
            Pattern pattern = Pattern.compile("(-\\d+)");
            Reader in = new FileReader(path);
//            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : parser) {
                ArrayList<String> temp = new ArrayList<>();
                boolean a = false;
                String key = "";
                for (String content : record) {
                    if (!a) {
                        a = true;
                        key = content;
                        continue;
                    }
                    Matcher matcher = pattern.matcher(content);
                    if(content!=null && matcher.find())
                        temp.add(content);
                    //System.out.println(content);
                }
                if (temp.size()!=0)
                    csv.put(key,temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeMap () {
        System.out.println(csv.size());
        try {
            FileOutputStream fos = new FileOutputStream(path.replace(".csv","_refined.csv"));
            PrintWriter out = new PrintWriter(fos);

            for (String key: csv.keySet()) {
                if (csv.get(key).size() != 0) {
                    out.print(key+",");
                    int i = 0;
                    for (String content: csv.get(key)) {
                        out.print(content);
                        if (i++ < csv.get(key).size())
                            out.print(",");
                    }
                    out.print("\n");
                }
            }
            out.flush();
            out.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
