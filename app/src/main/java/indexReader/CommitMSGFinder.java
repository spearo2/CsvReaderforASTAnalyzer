package indexReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.SerializationUtils;
import org.checkerframework.checker.units.qual.A;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommitMSGFinder {

    private String URLPath;
    private String savePath;
    private ArrayList<ArrayList<String>> listRead = new ArrayList<ArrayList<String>>();
    private static int nameColumn = 0;
    private static int issueKeyColumn = 1;
    private static int URLColumn = 2;

    public CommitMSGFinder (String path2) {
        this.savePath = path2;
        readURLList();
        fillUpMSG();
    }
    public void readURLList2 () {
        ArrayList<String> temp = new ArrayList<>();

    }
    public void readURLList () {
        try {
            Reader in = new FileReader("/data/CGYW/ASTChangeAnalyzer/data/apacheURLList.csv");
//            Reader in = new FileReader("/Users/leechanggong/Projects/ASTChangeAnalyzer/ASTChangeAnalyzer/data/apacheURLList.csv");
            CSVParser parser = CSVFormat.EXCEL.parse(in);
            boolean a = false;
            for (CSVRecord record : parser) {
                ArrayList<String> temp = new ArrayList<>();
                for (String content:record) {
                   if (!a) {
                       a = true;
                       break;

                   }
                   temp.add(content);
                    System.out.println(content);
                }
                listRead.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void fillUpMSG () {
        ArrayList<ArrayList<String>> temp = null;
        Pattern pattern = Pattern.compile("(git@|ssh|https://)github.com/()(.*?)$");
        String fileName = "";

        for (ArrayList<String> col : listRead) {
            if (col.size() != 0) {
                Matcher matcher = pattern.matcher(col.get(2));
                String [] parsed = col.get(2).split("/");
                if (matcher.find())
                    fileName = matcher.group(3);
                    temp = getCommitMSG(fileName);
                if (temp == null)
                    continue;
                String newPath = "/data/CGYW/IRdata/" + fileName.replaceAll("/","~") + ".csv";
                try {
                    FileOutputStream fos = new FileOutputStream(newPath);
                    PrintWriter out = new PrintWriter(fos);

                    for (ArrayList<String> content : temp) {
                        int i = 0;
                        for (String contents : content) {
                            if (contents != null) {
                                if (i++ != 0)
                                    out.print(",");
                                String msg = contents.trim();
                                if(msg.contains("\n"))
                                    msg.replaceAll("\n","\\n");
                                if(msg.contains(","))
                                    msg.replaceAll(","," ");
                                out.print(msg);
                            }

                        }
                            out.print("\n");
                    }
                    out.flush();
                    out.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }
    public ArrayList<ArrayList<String>> getCommitMSG (String projectName) {
        ArrayList<ArrayList<String>> commitMSG = new ArrayList<ArrayList<String>>();
        ArrayList<String> title = new ArrayList<>();
        title.add("CommitID");
        title.add("Message");
        commitMSG.add(title);
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;

        try {
            File file = new File("/data/CGYW/clones/"+projectName+"/.git/.git");
            if(!file.exists())
                return null;
            repo = builder.setGitDir(file).setMustExist(true).build();

            Git git = new Git(repo);
            Iterable<RevCommit> log = null;
            try {
                log = git.log().call();
                for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
                    RevCommit rev = iterator.next();
                        String msg = rev.getFullMessage();
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(rev.getName());
                        msg = msg.replaceAll("\n"," ");
                        temp.add(msg);
                        commitMSG.add(temp);
                }
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commitMSG;
    }
}
