package indexReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IssueDocGenerator {
    private String path;
    private HashMap<String,String> keyList = new HashMap<>();
    private ArrayList<ArrayList<String>> csv = new ArrayList<>();
    private HashMap<String,ArrayList<String>> combined = new HashMap<>();


    public IssueDocGenerator (String path) {
        this.path = path;
        readKeyList();
        csvToMap();
        makeIssuePerProject();
        writeMap();
    }
    public void writeMap () {

        try {
            FileOutputStream fos = new FileOutputStream(path + "/issue_num_list.csv");
            PrintWriter out = new PrintWriter(fos);

            for (String key: combined.keySet()) {
                if (combined.get(key).size() != 0) {
                    int cnt = 0;
                    for (String str : combined.get(key)) {
                        if (str.contains("-")) {
                            cnt++;
                            System.out.println(str);
                        }

                    }
                    out.print(cnt + ",");
                    out.print(key+",");
                    int i = 0;
                    for (String content: combined.get(key)) {
                        out.print(content);
                        if (i++ < combined.get(key).size())
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
    public void makeIssuePerProject () {
        Pattern pattern = Pattern.compile("([a-zA-Z]+-\\d+)");
        for (ArrayList <String> row : csv) {
            if(row.size() < 3)
                continue;
            String projectName = row.get(0).replaceAll(".csv","");
            String ID = row.get(1);
            String msg = row.get(2);
            Matcher matcher = pattern.matcher(msg);
            while(matcher.find()) {
                String issueKey = matcher.group(1);
                String [] issueKeySplit = issueKey.split("-");
                if (keyList.get("https://github.com/"+projectName.replaceAll("~","/")) != null && keyList.get("https://github.com/"+projectName.replaceAll("~","/")).equals(issueKeySplit[0])) {
                    if (combined.containsKey(projectName)) {
                        combined.get(projectName).add(issueKey);
                    } else {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(issueKey);
                        combined.put(projectName,temp);
                    }
                }

            }

        }
    }

    public void readKeyList () {
        try {
            Pattern pattern = Pattern.compile("(git@|ssh|https://)github.com/()(.*?)$");

            Reader in = new FileReader("/data/CGYW/ASTChangeAnalyzer/data/apacheURLList.csv");
//            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);
            boolean a = false;

            for (CSVRecord record : parser) {
                //ArrayList<String> temp = new ArrayList<>();
                int b = 0;
                String temp = "";
                for (String content:record) {

                    if (!a) {
                        a = true;
                        break;

                    }
                    if (b == 1) {
                        temp = content;
                    } else if(b == 2) {
                        keyList.put(content,temp);
                        System.out.println(content + "|||" + temp);
                    }
                    b++;
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
                        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withQuote(null);
                        CSVParser parser = null;
                        parser = new CSVParser(in,csvFileFormat);
                        System.out.println(path + "/" + file.getName());
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
