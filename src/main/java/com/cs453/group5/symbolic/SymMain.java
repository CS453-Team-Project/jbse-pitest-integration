package com.cs453.group5.symbolic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import com.cs453.group5.symbolic.entities.MutantId;

public class SymMain {
    public static void main(String[] args) {
        final PathManager pathManager = new PathManager();

        /* Check valid program execution */
        if (!pathManager.isProjectHome()) {
            System.out
                    .println("The program must be executed in the project root directory. Checkout CS453_PROJECT_HOME");
            return;
        } else if (!pathManager.checkPIT()) {
            System.out.println("Run PIT First");
            return;
        }

        if (args.length < 1) {
            System.out.println("ClassBinaryName required");
            return;
        } else if (args.length > 1) {
            System.out.println("Too many args. Only one argument(ClassBinaryName) needed.");
            return;
        }

        /* Get args */
        final String classBinaryName = args[0];

        /* Get paths */
        // file
        final String pitReportPath = pathManager.getRecentPitestReportPath();
        final String originalClassPath = pathManager.getClassPath(classBinaryName);
        final String backupClassPath = pathManager.getBackupClassPath(classBinaryName);
        // directories
        final String mutantsDirPath = pathManager.getMutantsDirPath(classBinaryName);
        final String targetClassDirPath = pathManager.getTargetClassDirPath();
        final String jbseResultsDirPath = pathManager.getJbseResultsDirPath();

        /* Execute JBSE for each living mutants */
        final MutationsParser mutParser = new MutationsParser(pitReportPath);
        final MutantDetailsParser mutDetailParser = new MutantDetailsParser(mutantsDirPath);
        final JbseResultParser jbseResultParser = new JbseResultParser(jbseResultsDirPath);
        final JbseExecutor jbseExecutor = new JbseExecutor();

        /**
         * Backup a original byte code at
         * '$CS453_PROJECT_HOME/target/classes/.../<classname>.class' to
         * '$CS453_PROJECT_HOME/pitest-reports/export/.../<classname>.class'. ... is a
         * '...' is a path related with the class. (e.g.
         * com/cs453/group5/examples/Parenthese/)
         */
        try {
            Path source = Paths.get(originalClassPath);
            Path target = Paths.get(backupClassPath);

            if (!Files.exists(target.getParent())) {
                Files.createDirectories(target.getParent());
            }

            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Set<MutantId> mutIdSet = mutParser.getSurvivedMutantIds();
        MutantId mutId;
        int i = 0;
        while ((mutId = mutDetailParser.getMutantDetails(i++)) != null) {
            if (mutIdSet.contains(mutId)) {
                System.out.println(String.format("mutant#%d was survived.", i - 1));

                final String mutantClass = mutId.getMutatedClass();
                final String mutatedMethod = mutId.getMutatedMethod();
                final int mutatedLine = mutId.getLine();

                final String classPath = mutantClass.replaceAll("[.]", "/");
                final String methodSignature = mutId.methodDescription();

                /* Apply Mutant's original byte code */
                try {
                    Path source = Paths.get(String.format("%s/%d/%s.class", mutantsDirPath, i - 1, classBinaryName));
                    Path target = Paths
                            .get(String.format("%s/%s.class", targetClassDirPath, classBinaryName.replace(".", "/")));

                    if (!Files.exists(target.getParent())) {
                        Files.createDirectories(target.getParent());
                    }

                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /* Modify java byte code of this mutant. */
                MutantTransformer mutTransformer = new MutantTransformer(mutantClass, mutatedMethod,
                        targetClassDirPath);
                mutTransformer.inertBytecode(mutatedLine, "jbse.meta.Analysis.ass3rt(false);");

                /* Run JBSE. Dump the output of the JBSE. */
                jbseExecutor.runJbse(i - 1, classPath, methodSignature, mutatedMethod);
                jbseResultParser.extract(i - 1, classPath);
            }
        }
    }
}
