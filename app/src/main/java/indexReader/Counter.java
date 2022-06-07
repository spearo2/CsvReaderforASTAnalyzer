package indexReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Counter {
    private String path;
    private ArrayList<String> ambari = new ArrayList<>();
    private ArrayList<String> beam = new ArrayList<>();
    private ArrayList<String> camel = new ArrayList<>();
    private ArrayList<String> cassandra = new ArrayList<>();
    private ArrayList<String> flink = new ArrayList<>();
    private ArrayList<String> hbase = new ArrayList<>();
    private ArrayList<String> ignite = new ArrayList<>();
    private ArrayList<String> isis = new ArrayList<>();
    private ArrayList<String> spark = new ArrayList<>();
    public Counter(String subopt, String path) {
        this.path = path;
        if(subopt.equals("per"))
            readCSV();
        if(subopt.equals("change"))
            countChanges();
    }

    public void countChanges () {
        ArrayList<String> temp = new ArrayList<>();
        try {

            Reader in = new FileReader(path);
//            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : parser) {
                boolean a = false;
                String key = "";
                for (String content : record) {
                    if (!a) {
                        a = true;
                        key = content;
                        continue;
                    }
                    temp.add(content);
                }
            }

        } catch (IOException e) {
        e.printStackTrace();
        }
        System.out.println("Changes: " + temp.size());
    }
    public void readCSV () {
        try {
            Reader in = new FileReader(path);
//            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : parser) {
                boolean a = false;
                String key = "";
                for (String content : record) {
                    if (!a) {
                        a = true;
                        key = content;
                        continue;
                    }
                    if (content != null) {
                        String[] temp = content.split("&");
                        if (temp[0].contains("ambari")) {
                            ambari.add(temp[1]);
                        } else if (temp[0].contains("beam")) {
                            beam.add(temp[1]);
                        } else if (temp[0].contains("camel")) {
                            camel.add(temp[1]);
                        } else if (temp[0].contains("cassandra")) {
                            cassandra.add(temp[1]);
                        } else if (temp[0].contains("flink")) {
                            flink.add(temp[1]);
                        } else if (temp[0].contains("hbase")) {
                            hbase.add(temp[1]);
                        } else if (temp[0].contains("ignite")) {
                            ignite.add(temp[1]);
                        } else if (temp[0].contains("isis")) {
                            isis.add(temp[1]);
                        } else if (temp[0].contains("spark")) {
                            spark.add(temp[1]);
                        }

                    }

                    //System.out.println(content);
                }
            }
            Set<String> set = new HashSet<>(ambari);
            ambari.clear();
            ambari.addAll(set);

            set = new HashSet<>(beam);
            beam.clear();
            beam.addAll(set);

            set = new HashSet<>(camel);
            camel.clear();
            camel.addAll(set);

            set = new HashSet<>(cassandra);
            cassandra.clear();
            cassandra.addAll(set);

            set = new HashSet<>(flink);
            flink.clear();
            flink.addAll(set);

            set = new HashSet<>(hbase);
            hbase.clear();
            hbase.addAll(set);

            set = new HashSet<>(ignite);
            ignite.clear();
            ignite.addAll(set);

            set = new HashSet<>(isis);
            isis.clear();
            isis.addAll(set);

            set = new HashSet<>(spark);
            spark.clear();
            spark.addAll(set);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(          "ambari: " + ambari.size()
                            + "\n" + "beam: " + beam.size()
                            + "\n" + "camel: " + camel.size()
                            + "\n" + "cassandra: " + cassandra.size()
                            + "\n" + "ignite: " + ignite.size()
                            + "\n" + "isis: " + isis.size()
                            + "\n" + "spark: " + spark.size()
                            + "\n" + "flink: " + flink.size()
                            + "\n" + "hbase: " + hbase.size());

    }
}
