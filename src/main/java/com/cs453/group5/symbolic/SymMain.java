package com.cs453.group5.symbolic;

import java.io.File;
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

        /* Execute JBSE for each living mutants */
        final MutationsParser mutParser = new MutationsParser(pitReportPath);
        final MutantDetailsParser mutDetailParser = new MutantDetailsParser(mutantsDirPath);
        final JbseExecutor jbseExecutor = new JbseExecutor();

        /**
         * TODO: Backup a original byte code at
         * '$CS453_PROJECT_HOME/target/classes/.../<classname>.class' to
         * '$CS453_PROJECT_HOME/pitest-reports/export/.../<classname>.class'. ... is a
         * '...' is a path related with the class. (e.g.
         * com/cs453/group5/examples/Parenthese/)
         */
        try {
            Path source = Paths.get(originalClassPath);
            Path target = Paths.get(backupClassPath);

            System.out.println(source.toString());

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
                /**
                 * TODO: Instrument java byte code of this mutant and write it at
                 * '$CS453_PROJECT_HOME/target/classes/.../<classname>.class'.
                 */
                final String mutantBinPath = String.format("%s/%d", mutantsDirPath, i - 1);
                final String mutantClass = mutId.getMutatedClass();
                final String mutatedMethod = mutId.getMutatedMethod();
                final int mutatedLine = mutId.getLine();

                MutantTransformer mutTransformer = new MutantTransformer(mutantClass, mutantBinPath, mutatedMethod,
                        targetClassDirPath);
                mutTransformer.inertBytecode(mutatedLine, "jbse.meta.Analysis.ass3rt(false);");

                /**
                 * TODO: Run JBSE. Capture the output of the JBSE. Related class is
                 * 'JbseExecutor'.
                 */
                final String classPath = mutantClass.replaceAll("[.]", "/"); // "com/cs453/group5/examples/Calculator"
                final String methodSignature = mutId.methodDescription(); // ([C)I

                jbseExecutor.runJbse(i - 1, classPath, methodSignature, mutatedMethod);
            }
        }
    }
}
