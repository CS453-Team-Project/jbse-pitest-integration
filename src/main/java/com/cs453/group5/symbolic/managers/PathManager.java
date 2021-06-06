package com.cs453.group5.symbolic.managers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cs453.group5.symbolic.entities.ClassBinName;

/**
 * Class that manages absolute path of a file or a directory.
 * 
 * Member name terminate with DirPath means a path of a directory. Path means a
 * path of a file.
 * 
 * @author Yeongil Yoon
 */
public class PathManager {
    private static final String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    private final String toolHome = System.getenv("CS453_PROJECT_HOME");
    private final String projectHome = System.getProperty("user.dir");

    private final String classDirPath = String.format("%s/target/classes", projectHome);
    private final String backupClassDirPath = String.format("%s/target/backup-classes", projectHome);

    private final String pitestBaseDirPath = String.format("%s/target/pit-reports", projectHome);

    private final String jbseResultsDirPath = String.format("%s/target/jbse-results/%s", projectHome, timestamp);
    private final String jbseLibPath = String.format("%s/res/jbse-0.10.0-SNAPSHOT-shaded.jar", toolHome);

    private final String killReportPath = String.format("%s/kill_cond.txt", projectHome);

    private final String userAssumePath = String.format("%s/assume/my_tool_assume.json", projectHome);

    public Boolean isProjectHome() {
        final String currDir = System.getProperty("user.dir");
        return currDir.equals(projectHome) || currDir.equals(projectHome + "/") || (currDir + "/").equals(projectHome);
    }

    public String getClassDirPath() {
        return this.classDirPath;
    }

    public String getClassPath(ClassBinName classBinName) {
        return String.format("%s/%s.class", classDirPath, classBinName.getSlash());
    }

    public String getBackupClassPath(ClassBinName classBinName) {
        return String.format("%s/%s.class", backupClassDirPath, classBinName.getSlash());
    }

    public String getPitestBaseDirPath() {
        return pitestBaseDirPath;
    }

    public String getMutantsDirPath(ClassBinName classBinName) {
        return String.format("%s/export/%s/mutants", pitestBaseDirPath, classBinName.getSlash());
    }

    public String getMutantClassPath(ClassBinName classBinName, int mutantNumber) {
        return String.format("%s/%d/%s.class", getMutantsDirPath(classBinName), mutantNumber, classBinName.getDot());
    }

    public String getJbseResultPath(ClassBinName classBinName, String subdir, int mutantNumber) {
        if (subdir == null) {
            subdir = "";
        }

        return String.format("%s/%s/%s/mutant%d.txt", jbseResultsDirPath, classBinName.getSlash(), subdir,
                mutantNumber);
    }

    public String getJbseMethodDirPath(ClassBinName classBinName, String subdir) {
        return String.format("%s/%s/%s", jbseResultsDirPath, classBinName.getSlash(), subdir);
    }

    public String getJbseLibPath() {
        return jbseLibPath;
    }

    public String getKillReportPath() {
        return killReportPath;
    }

    public String getUserAssumePath() {
        return userAssumePath;
    }
}