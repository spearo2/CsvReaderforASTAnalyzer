package indexReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BackReader {
    private String path;
    private ArrayList<ArrayList<String>> csv = new ArrayList<>();
    private ArrayList<ArrayList<String>> newIssue = new ArrayList<>();
    private HashMap<String, Set<String>> issueSet = new HashMap<>();
    public BackReader(String subopt ,String path) {
        this.path = path;

        if (subopt.equals("hashcode")) {
            readAll();
            hashCodePerProject("ambari");
            hashCodePerProject("beam");
            hashCodePerProject("camel");
            hashCodePerProject("cassandra");
            hashCodePerProject("flink");
            hashCodePerProject("hbase");
            hashCodePerProject("ignite");
            hashCodePerProject("isis");

        }
        if (subopt.equals("fast")) {
            readAll();
            issuePerHashcode("ambari");
            issuePerHashcode("beam");
            issuePerHashcode("camel");
            issuePerHashcode("cassandra");
            issuePerHashcode("flink");
            issuePerHashcode("hbase");
            issuePerHashcode("ignite");
            issuePerHashcode("isis");
        }

        if (subopt.equals("issue")) {
            readURLList();
            makeNewIssue("ambari");
            makeNewIssue("beam");
            makeNewIssue("camel");
            makeNewIssue("cassandra");
            makeNewIssue("flink");
            makeNewIssue("hbase");
            makeNewIssue("ignite");
            makeNewIssue("isis");
            makeNewIssue("spark");
            write();
        }
        //System.out.println(newIssue.get(0));
    }
    public void issuePerHashcode(String projectName) {
        issueSet = new HashMap<>();
        for (ArrayList<String> inner : csv) {
            boolean a = false;
            String hashCode = "";

            for (String content : inner) {
                if (!a) {
                    a = true;
                    hashCode = content;
                    continue;
                }
                for (String element : content.split("~")) {
                    if(element.length() < 20) {
                        if (element.contains("-")) {
                            if (element.toUpperCase().contains(projectName.toUpperCase())) {
                                String [] tmp = element.split("-");
                                if (tmp[0].length()==projectName.length() && !tmp[1].equals("0")) {
                                    if (issueSet.containsKey(element))
                                        issueSet.get(element).add(hashCode);
                                    else {
                                        Set<String> temp = new HashSet<>();
                                        temp.add(hashCode);
                                        issueSet.put(element.toUpperCase(),temp);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        writeMap(projectName);
    }




    public void hashCodePerProject(String projectName) {
        for (ArrayList<String> inner : csv) {
            boolean a = false;
            String hashCode = "";
            ArrayList<String> temp = new ArrayList<>();
            for (String content : inner) {
                if (!a) {
                    a = true;
                    hashCode = content;
                    continue;
                }
                for (String element : content.split("~")) {
                    if(element.length() < 20) {
                        if (element.contains("-")) {
                            if (element.toUpperCase().contains(projectName.toUpperCase())) {
                                String [] spt = element.split("-");
                                temp.add(element);
                            }
                        }
                    }
                }
            }
            if (temp.size() != 0) {
                ArrayList<String> temp2 = new ArrayList<>();
                temp2.add(hashCode);
                temp2.addAll(temp);
                newIssue.add(temp2);
            }
        }
        

        write(projectName);
    }
    public void writeMap(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream("/Users/leechanggong/Desktop/fast/" + fileName + ".csv");
            PrintWriter out = new PrintWriter(fos);

            for (String key : issueSet.keySet()) {
                out.print(key + ",");
                for (String content : issueSet.get(key)) {
                    out.print(content);
                    out.print(",");
                }
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


    public void write(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream("/Users/leechanggong/Desktop/hashcode_par_project/" + fileName + ".csv");
            PrintWriter out = new PrintWriter(fos);

            for (ArrayList<String> inner : newIssue) {
                int i = 0;
                for (String content : inner) {
                    out.print(content);
                    if (i++ < inner.size())
                        out.print(",");
                }
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

    public void write() {
        try {
            FileOutputStream fos = new FileOutputStream(path.replace(".csv", "_last.csv"));
            PrintWriter out = new PrintWriter(fos);

            for (ArrayList<String> inner : newIssue) {
                int i = 0;
                for (String content : inner) {
                    out.print(content);
                    if (i++ < inner.size())
                        out.print(",");
                }
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



    public void makeNewIssue(String name) {

        int cnt = 0;
        ArrayList<String> temp = new ArrayList<>();
        temp.add(name);
        for (ArrayList<String> inner: csv) {
            if (inner.size() != 0) {
                for (String str : inner) {
                    int i = 0;
                    for (String element : str.split("~")) {
                        if(i++ > 0 && element.length() < 20) {
                            if (element.contains("-")) {
                                if(element.toUpperCase().contains(name.toUpperCase()))
                                    cnt++;
                            }
                        }

                    }
                }
            }
        }
        temp.add(Integer.toString(cnt));
        for (ArrayList<String> inner: csv) {
            if (inner.size() != 0) {
                for (String str : inner) {
                    int i = 0;
                    for (String element : str.split("~")) {
                        if(i++ > 0 && element.length() < 20) {
                            if (element.contains("-")) {
                                if (element.toUpperCase().contains(name.toUpperCase()))
                                    temp.add(element);
                            }
                        }
                    }
                }
            }
        }
        newIssue.add(temp);
    }


    public void readURLList () {
        try {
            Reader in = new FileReader(path);
//            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);

            for (CSVRecord record : parser) {
                ArrayList<String> temp = new ArrayList<>();
                boolean a = false;
                for (String content:record) {
                    if (!a) {
                        a = true;
                        continue;
                    }
                    temp.add(content);
                    //System.out.println(content);
                }
                csv.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAll () {
        try {
            Reader in = new FileReader(path);
//            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);

            for (CSVRecord record : parser) {
                ArrayList<String> temp = new ArrayList<>();
                for (String content:record) {
                    temp.add(content);
                    //System.out.println(content);
                }
                csv.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
