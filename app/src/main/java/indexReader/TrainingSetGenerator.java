package indexReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TrainingSetGenerator {

    private String path;
    private ArrayList<String> ambari = new ArrayList<>();
    private ArrayList<String> beam = new ArrayList<>();
    private ArrayList<String> camel = new ArrayList<>();
    private ArrayList<String> cassandra = new ArrayList<>();
    private ArrayList<String> flink = new ArrayList<>();
    private ArrayList<String> hbase = new ArrayList<>();
    private ArrayList<String> ignite = new ArrayList<>();
    private ArrayList<String> isis = new ArrayList<>();



    public TrainingSetGenerator(String path) {
        this.path = path;
        read("ambari");
        read("beam");
        read("camel");
        read("cassandra");
        read("flink");
        read("hbase");
        read("ignite");
        read("isis");
        writeTenFold("ambari");
        writeTenFold("beam");
        writeTenFold("camel");
        writeTenFold("cassandra");
        writeTenFold("flink");
        writeTenFold("hbase");
        writeTenFold("ignite");
        writeTenFold("isis");
    }
    public void writeTenFold(String projectName) {
        ArrayList<String> issues = new ArrayList<>();
        if (projectName.equals("ambari")) {
            for (int i = 1; i < ambari.size(); i++) {
                issues.add(ambari.get(i));
            }
        } else if (projectName.equals("beam")) {
            for (int i = 1; i < beam.size(); i++) {
                issues.add(beam.get(i));
            }
        } else if (projectName.equals("camel")) {
            for (int i = 1; i < camel.size(); i++) {
                issues.add(camel.get(i));
            }
        } else if (projectName.equals("cassandra")) {
            for (int i = 1; i < cassandra.size(); i++) {
                issues.add(cassandra.get(i));
            }
        } else if (projectName.equals("flink")) {
            for (int i = 1; i < flink.size(); i++) {
                issues.add(flink.get(i));
            }
        } else if (projectName.equals("hbase")) {
            for (int i = 1; i < hbase.size(); i++) {
                issues.add(hbase.get(i));
            }
        } else if (projectName.equals("ignite")) {
            for (int i = 1; i < ignite.size(); i++) {
                issues.add(ignite.get(i));
            }
        } else if (projectName.equals("isis")) {
            for (int i = 1; i < isis.size(); i++) {
                issues.add(isis.get(i));
            }
        }
        Set<String> set = new HashSet<>(issues);
        issues.clear();
        issues.addAll(set);

        for (int j = 1; j < 11; j++) {
            ArrayList<String> trainSet = new ArrayList<>();
            ArrayList<String> testSet = new ArrayList<>();
            for (int i = issues.size()/10 * (j-1); i < issues.size()/10 * j; i++) {
                testSet.add(issues.get(i));
            }
            for (String temp:issues) {
                if (!testSet.contains(temp))
                    trainSet.add(temp);
            }
            try {
//                FileOutputStream fos = new FileOutputStream("/Users/leechanggong/Desktop/newtest/" + projectName + "train" + j + ".csv");
                FileOutputStream fos = new FileOutputStream("/home/leechanggong/test_set/" + projectName + "train" + j + ".csv");
                PrintWriter out = new PrintWriter(fos);


                for (String a : trainSet) {
                        out.print(a);
                        out.print("\n");
                }


                out.flush();
                out.close();
                fos.close();
                //fos = new FileOutputStream("/Users/leechanggong/Desktop/newtest/" + projectName + "test" + j + ".csv");
                fos = new FileOutputStream("/home/leechanggong/test_set/" + projectName + "test" + j + ".csv");
                out = new PrintWriter(fos);
                for (String a : testSet) {
                    out.print(a);
                    out.print("\n");
                }
                out.flush();
                out.close();
                fos.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void read (String projectName) {
        try {
            Reader in = new FileReader(path + projectName + ".csv");
//            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);
            int i = 0;
            for (CSVRecord record : parser) {
                ArrayList<String> temp = new ArrayList<>();
                boolean a = false;
                String name = "";
                for (String content:record) {
                    if(!a) {
                        a = true;
                        if (projectName.equals("ambari")) {
                            ambari.add(content);
                        } else if (projectName.equals("beam")) {
                            beam.add(content);
                        } else if (projectName.equals("camel")) {
                            camel.add(content);
                        } else if (projectName.equals("cassandra")) {
                            cassandra.add(content);
                        } else if (projectName.equals("flink")) {
                            flink.add(content);
                        } else if (projectName.equals("hbase")) {
                            hbase.add(content);
                        } else if (projectName.equals("ignite")) {
                            ignite.add(content);
                        } else if (projectName.equals("isis")) {
                            isis.add(content);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read () {
        try {
            Reader in = new FileReader(path);
//            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);
            int i = 0;
            for (CSVRecord record : parser) {
                ArrayList<String> temp = new ArrayList<>();
                boolean a = false;
                String name = "";
                for (String content:record) {
                    if (!a) {
                        a = true;
                        continue;
                    }
                    switch (i) {
                        case 0:
                            ambari.add(content);
                        case 1:
                            beam.add(content);
                        case 2:
                            camel.add(content);
                        case 3:
                            cassandra.add(content);
                        case 4:
                            flink.add(content);
                        case 5:
                            hbase.add(content);
                        case 6:
                            ignite.add(content);
                        case 7:
                            isis.add(content);
                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
