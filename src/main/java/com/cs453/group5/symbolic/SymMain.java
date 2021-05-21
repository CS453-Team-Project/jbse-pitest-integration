package com.cs453.group5.symbolic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import com.cs453.group5.symbolic.entities.MutantId;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class SymMain implements Callable<Integer> {
    private PathManager pathManager = new PathManager();

    @Parameters(index = "0", description = "Class name with dot syntax (e.g. com.cs453.group5.examples.Calculator)")
    private String classBinaryName;

    @Parameters(index = "1", arity = "0..*", description = "Method names (e.g. isPositive isNegative ...). Default will run all methods of survived mutants. If option -o or -m was specified, then default will run nothing.")
    private List<String> methods;

    @Option(names = { "-c", "--clean" }, defaultValue = "false", description = "Clear and rebuild.")
    private Boolean cleanOpt;

    @Option(names = { "-o",
            "--original" }, defaultValue = "false", description = "Run original class. In his run, Run original class. The program will not modify the byte code and return pure jbse report.")
    private Boolean originalOpt;

    @Option(names = { "-m",
            "--mutants" }, arity = "1..*", split = ",", description = "Run specific mutants. Parameters are the mutant indexes splitted with `,` (e.g. -m 1,2,3,4,5). The program will not modify the byte code and return pure jbse report.")
    private List<Integer> mutantNumbers;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SymMain()).execute(args);
        System.exit(exitCode);
    }

    public Integer call() throws Exception {
        if (!checkExecutionValidity()) {
            return 0;
        }

        if (cleanOpt) {
            System.out.println("Clean Build");
            cleanTest();
        }
        if (!pathManager.checkPIT()) {
            System.out.println("Running PIT");
            runPitest();
        }

        if (originalOpt) {
            if (methods == null || methods.isEmpty()) {
                System.out.println("\n\n<WARN> No Method specified\n\n");
            } else {
                runOriginal();
            }
        } else if (mutantNumbers != null) {
            if (methods == null || methods.isEmpty()) {
                System.out.println("\n\n<WARN> No Method specified\n\n");
            } else {
                runMutants();
            }
        } else {
            run();
        }

        return 0;
    }

    public void run() {
        /* Init instances */
        final MutationsParser mutParser = new MutationsParser(pathManager.getRecentPitestReportPath());
        final MutantDetailsParser mutDetailParser = new MutantDetailsParser(
                pathManager.getMutantsDirPath(classBinaryName));
        final JbseResultParser jbseResultParser = new JbseResultParser(pathManager.getJbseResultsDirPath());
        final JbseExecutor jbseExecutor = new JbseExecutor(pathManager.getClassDirPath(), pathManager.getJbseLibPath(),
                pathManager.getJbseResultsDirPath());
        final PathFinderExecutor pathFinderExecutor = new PathFinderExecutor();

        backupOriginalClass();

        /* For each survived mutant, modify byte code and run */
        final Set<MutantId> mutIdSet = mutParser.getSurvivedMutantIds();
        MutantId mutId;
        int i = 0;
        while ((mutId = mutDetailParser.getMutantDetails(i++)) != null) {
            if (mutIdSet.contains(mutId)) {
                final String mutantClass = mutId.getMutatedClass();
                final String mutatedMethod = mutId.getMutatedMethod();
                final String methodSignature = mutId.getMethodDescription();
                final int mutatedLine = mutId.getLine();
                int mutantNumber = i - 1;

                if (methods != null && !methods.contains(mutatedMethod)) {
                    continue;
                }

                printSurvivedMutant(mutId, mutantNumber);

                applyMutatedClass(mutantNumber);
                MutantTransformer mutTransformer = new MutantTransformer(mutantClass, mutatedMethod,
                        pathManager.getClassDirPath());
                mutTransformer.inertBytecode(mutatedLine, "jbse.meta.Analysis.ass3rt(false);");

                try {
                    pathFinderExecutor.execFinder(mutantClass, mutatedMethod);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                final String classPath = pathManager.classBinNameToPath(mutantClass);
                jbseExecutor.runJbse(mutantNumber, classPath, methodSignature, mutatedMethod);
                jbseResultParser.extract(mutantNumber, classPath);
            }
        }

        restoreOriginalClass();
    }

    public void runOriginal() {
        final MutationsParser mutParser = new MutationsParser(pathManager.getRecentPitestReportPath());
        final JbseExecutor jbseExecutor = new JbseExecutor(pathManager.getClassDirPath(), pathManager.getJbseLibPath(),
                pathManager.getJbseResultsDirPath());

        final String classPath = pathManager.classBinNameToPath(classBinaryName);
        for (int i = 0; i < methods.size(); i++) {
            final String method = methods.get(i);
            final String methodSignature = mutParser.getMethodSignature(method);

            if (methods.contains(method)) {
                jbseExecutor.runJbse(-(i + 1), classPath, methodSignature, methods.get(i));
            }
        }
    }

    public void runMutants() {
        final MutantDetailsParser mutDetailParser = new MutantDetailsParser(
                pathManager.getMutantsDirPath(classBinaryName));
        final JbseExecutor jbseExecutor = new JbseExecutor(pathManager.getClassDirPath(), pathManager.getJbseLibPath(),
                pathManager.getJbseResultsDirPath());

        backupOriginalClass();

        for (int mutantNumber : mutantNumbers) {
            MutantId mutId = mutDetailParser.getMutantDetails(mutantNumber);

            final String mutantClass = mutId.getMutatedClass();
            final String mutatedMethod = mutId.getMutatedMethod();
            final String methodSignature = mutId.getMethodDescription();

            if (methods.contains(mutatedMethod)) {
                applyMutatedClass(mutantNumber);

                final String classPath = pathManager.classBinNameToPath(mutantClass);
                jbseExecutor.runJbse(mutantNumber, classPath, methodSignature, mutatedMethod);
            }
        }

        restoreOriginalClass();
    }

    public void runPitest() {
        final String command = "mvn org.pitest:pitest-maven:mutationCoverage -Dfeatures=+EXPORT";
        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command).inheritIO();

        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cleanTest() {
        final String command = "mvn clean test";
        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command).inheritIO();

        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean checkExecutionValidity() {
        if (!pathManager.isProjectHome()) {
            System.out
                    .println("The program must be executed in the project root directory. Checkout CS453_PROJECT_HOME");
            return false;
        }

        return true;
    }

    private void backupOriginalClass() {
        try {
            String source = pathManager.getClassPath(classBinaryName);
            String target = pathManager.getBackupClassPath(classBinaryName);
            copyFile(source, target);
        } catch (IOException e) {
            System.err.println(String.format("Original class backup failed: %s.class", classBinaryName));
            e.printStackTrace();
        }
    }

    private void restoreOriginalClass() {
        try {
            String source = pathManager.getBackupClassPath(classBinaryName);
            String target = pathManager.getClassPath(classBinaryName);
            copyFile(source, target);
        } catch (IOException e) {
            System.err.println(String.format("Original class restoring failed: %s.class", classBinaryName));
            e.printStackTrace();
        }
    }

    private void applyMutatedClass(int mutantNumber) {
        try {
            String source = pathManager.getMutantClassPath(classBinaryName, mutantNumber);
            String target = pathManager.getClassPath(classBinaryName);
            copyFile(source, target);
        } catch (IOException e) {
            System.err.println(String.format("Original class restoring failed: %s.class", classBinaryName));
            e.printStackTrace();
        }
    }

    private void copyFile(String sourcePath, String targetPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path target = Paths.get(targetPath);

        if (!Files.exists(target.getParent())) {
            Files.createDirectories(target.getParent());
        }

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    private void printSurvivedMutant(MutantId mutantId, int mutantNumber) {
        System.out.println("=========================================");
        System.out.println(String.format("mutant#%d was survived.\n", mutantNumber));
        System.out.println(mutantId.toString());
        System.out.println("=========================================\n");
    }
}
