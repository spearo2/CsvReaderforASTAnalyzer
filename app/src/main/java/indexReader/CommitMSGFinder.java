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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;

public class CommitMSGFinder {

    private String URLPath;
    private String savePath;
    private ArrayList<ArrayList<String>> listRead = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> commitMSG = new ArrayList<ArrayList<String>>();
    private static int nameColumn = 0;
    private static int issueKeyColumn = 1;
    private static int URLColumn = 2;

    public CommitMSGFinder (String path2) {
        this.savePath = path2;
        readURLList();
        fillUpMSG();
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
        for (ArrayList<String> col : listRead) {
            if (col.size() != 0) {
                String [] parsed = col.get(2).split("/");
                getCommitMSG("apache/"+parsed[parsed.length-1]);

            }

        }

    }
    public void getCommitMSG (String projectName) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;

        try {
            repo = builder.setGitDir(new File("/data/CGYW/clones/"+projectName+"/.git/.git")).setMustExist(true).build();

            Git git = new Git(repo);
            Iterable<RevCommit> log = null;
            try {
                log = git.log().call();
                for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
                    RevCommit rev = iterator.next();
//                    if(ID.equals(rev.getName())) {
//                    System.out.println(projectName + "/" + ID + ":" + rev.getFullMessage());
//                    break;
                        String msg = rev.getFullMessage();
                        System.out.println(msg);
                        //Matcher matcher = pattern.matcher(msg);
                        //while(matcher.find()) {
//                            if (projectKey.get("https://github.com/" + projectName)!=null)
//                                if(matcher.group(1).toUpperCase().contains(projectKey.get("https://github.com/" + projectName).toUpperCase()))
//                                    IssueNum += "~" + matcher.group(1);
                        //}
                        //break;
                    //}
                }
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
