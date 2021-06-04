package com.cs453.group5.symbolic;

import java.util.List;
import java.util.concurrent.Callable;

import com.cs453.group5.symbolic.entities.ClassBinName;
import com.cs453.group5.symbolic.entities.ClassInfo;
import com.cs453.group5.symbolic.executors.CleanBuilder;
import com.cs453.group5.symbolic.executors.JbseExecutor;
import com.cs453.group5.symbolic.executors.PathFinderExecutor;
import com.cs453.group5.symbolic.executors.PitestExecutor;
import com.cs453.group5.symbolic.managers.ClassFileManager;
import com.cs453.group5.symbolic.managers.JbseManager;
import com.cs453.group5.symbolic.managers.MutantManager;
import com.cs453.group5.symbolic.managers.PathManager;
import com.cs453.group5.symbolic.parsers.JbseResultParser;
import com.cs453.group5.symbolic.parsers.MutantDetailsParser;
import com.cs453.group5.symbolic.parsers.MutationsParser;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class SymMain implements Callable<Integer> {
    // Arguments
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

    // Dependencies
    PathManager pathManager = new PathManager();

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SymMain()).execute(args);
        System.exit(exitCode);
    }

    public Integer call() throws Exception {
        checkExecutionValidity();

        if (cleanOpt) {
            CleanBuilder cleanBuilder = new CleanBuilder();
            cleanBuilder.cleanBuild();
        }

        Run run = getRunObj();
        if (run == null) {
            return -1;
        }

        if (originalOpt) {
            if (methods == null || methods.isEmpty()) {
                System.out.println("\n\n<WARN> No Method specified\n\n");
            } else {
                run.runOriginal(methods);
            }
        } else if (mutantNumbers != null) {
            if (methods == null || methods.isEmpty()) {
                System.out.println("\n\n<WARN> No Method specified\n\n");
            } else {
                run.runMutants(mutantNumbers, methods);
            }
        } else {
            run.run(methods);
        }

        return 0;
    }

    private Run getRunObj() {
        ClassBinName classBinName = new ClassBinName(classBinaryName);
        ClassInfo classInfo;

        classInfo = new ClassInfo(classBinName);

        MutationsParser mutParser = new MutationsParser(pathManager.getPitestBaseDirPath());
        MutantDetailsParser detailsParser = new MutantDetailsParser(pathManager.getMutantsDirPath(classBinName));
        PitestExecutor pitExecutor = new PitestExecutor(pathManager.getPitestBaseDirPath());

        JbseExecutor jbseExecutor = new JbseExecutor(pathManager.getClassDirPath(), pathManager.getJbseLibPath());
        JbseResultParser jbseResultParser = new JbseResultParser();
        PathFinderExecutor pathFinderExecutor = new PathFinderExecutor();

        PathManager pathManager = new PathManager();
        ClassFileManager classFileManager = new ClassFileManager(pathManager, classBinName);
        MutantManager mutantManager = new MutantManager(mutParser, detailsParser, pitExecutor);
        JbseManager jbseManager = new JbseManager(jbseExecutor, jbseResultParser, pathFinderExecutor);

        return new Run(pathManager, classFileManager, mutantManager, jbseManager, classInfo);
    }

    private void checkExecutionValidity() {
        if (!pathManager.isProjectHome()) {
            throw new IllegalStateException(
                    "The program must be executed in the project root directory. Checkout CS453_PROJECT_HOME");
        }
    }
}
