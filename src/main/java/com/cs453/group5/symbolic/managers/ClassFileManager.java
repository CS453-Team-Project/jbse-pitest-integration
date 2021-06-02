package com.cs453.group5.symbolic.managers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.cs453.group5.symbolic.entities.ClassBinName;

public class ClassFileManager {
    private PathManager pathManager;
    private ClassBinName classBinName;

    public ClassFileManager(PathManager pathManager, ClassBinName classBinName) {
        this.pathManager = pathManager;
        this.classBinName = classBinName;
    }

    private void copyFile(String sourcePath, String targetPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path target = Paths.get(targetPath);

        if (!Files.exists(target.getParent())) {
            Files.createDirectories(target.getParent());
        }

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public void backupOriginalClass() {
        try {
            String source = pathManager.getClassPath(classBinName);
            String target = pathManager.getBackupClassPath(classBinName);
            copyFile(source, target);
        } catch (IOException e) {
            System.err.println(String.format("Original class backup failed: %s.class", classBinName.getDot()));
            e.printStackTrace();
        }
    }

    public void restoreOriginalClass() {
        try {
            String source = pathManager.getBackupClassPath(classBinName);
            String target = pathManager.getClassPath(classBinName);
            copyFile(source, target);
        } catch (IOException e) {
            System.err.println(String.format("Original class restoring failed: %s.class", classBinName.getDot()));
            e.printStackTrace();
        }
    }

    public void applyMutatedClass(int mutantNumber) {
        try {
            String source = pathManager.getMutantClassPath(classBinName, mutantNumber);
            String target = pathManager.getClassPath(classBinName);
            copyFile(source, target);
        } catch (IOException e) {
            System.err.println(String.format("Original class restoring failed: %s.class", classBinName.getDot()));
            e.printStackTrace();
        }
    }
}
