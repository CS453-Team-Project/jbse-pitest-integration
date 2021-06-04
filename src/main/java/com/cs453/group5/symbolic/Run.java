package com.cs453.group5.symbolic;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs453.group5.symbolic.entities.Assertion;
import com.cs453.group5.symbolic.entities.Assumption;
import com.cs453.group5.symbolic.entities.ClassBinName;
import com.cs453.group5.symbolic.entities.ClassInfo;
import com.cs453.group5.symbolic.entities.MethodInfo;
import com.cs453.group5.symbolic.entities.MutantId;
import com.cs453.group5.symbolic.entities.Pair;
import com.cs453.group5.symbolic.exceptions.IllegalPathFinderOutputException;
import com.cs453.group5.symbolic.managers.ClassFileManager;
import com.cs453.group5.symbolic.managers.JbseManager;
import com.cs453.group5.symbolic.managers.MutantManager;
import com.cs453.group5.symbolic.managers.PathManager;

public class Run {
    ClassInfo classInfo;
    ClassBinName classBinName;

    PathManager pathManager;
    ClassFileManager classFileManager;
    MutantManager mutantManager;
    JbseManager jbseManager;
    JavassistManager javssManager;

    public Run(PathManager pathManager, ClassFileManager classFileManager, MutantManager mutantManager,
            JbseManager jbseManager, JavassistManager javssManager, ClassInfo classInfo) {

        this.pathManager = pathManager;
        this.classFileManager = classFileManager;
        this.mutantManager = mutantManager;
        this.jbseManager = jbseManager;
        this.javssManager = javssManager;
        this.classInfo = classInfo;

        this.classBinName = classInfo.getBinaryName();
    }

    public void run(List<String> specifiedMethods) {
        // Backup original class
        classFileManager.backupOriginalClass();

        // Get alived Mutants
        Map<String, List<Pair<Integer, MutantId>>> aliveMutantIds = mutantManager.getAliveMutants();
        Set<String> methods = aliveMutantIds.keySet().stream()
                .filter((m) -> specifiedMethods == null || specifiedMethods.contains(m)).collect(Collectors.toSet());

        for (String method : methods) {
            runOriginal(method, false);

            List<Pair<Integer, MutantId>> pairs = aliveMutantIds.get(method);
            for (Pair<Integer, MutantId> pair : pairs) {
                int mutantNumber = pair.getFirst();
                MutantId mutId = pair.getSecond();

                final int mutatedLine = mutId.getLine();
                final MethodInfo methodInfo = classInfo.getMethodInfo(method);

                final Assertion falseAssert = new Assertion(mutatedLine, "false");
                final Assertion falseAssertAfter = new Assertion(mutatedLine + 1, "false");

                final String relativePath = String.format("%s/mutants/%d", method, mutantNumber);
                final String jbsePath = pathManager.getJbseResultPath(classBinName, relativePath, mutantNumber);

                // Finding R condition
                classFileManager.applyMutatedClass(mutantNumber);
                javssManager.insert(methodInfo, falseAssert);
                jbseManager.runAndExtract(methodInfo, jbsePath, true);
                Assumption reachabilityCond = jbseManager.findPathCond(methodInfo, jbsePath);

                // Finding I condition
                classFileManager.applyMutatedClass(mutantNumber);
                javssManager.insert(methodInfo, falseAssertAfter, reachabilityCond);
                jbseManager.runAndExtract(methodInfo, jbsePath, true);
                Assumption infectionCond;
                try {
                    infectionCond = jbseManager.findPathCond(methodInfo, jbsePath);
                } catch (IllegalPathFinderOutputException e) {
                    System.out.println("No infection condition. Proceed with reachability condition.");
                    infectionCond = reachabilityCond;
                }

                // Comparing mutant and origin
                // mutant
                classFileManager.applyMutatedClass(mutantNumber);
                javssManager.insert(methodInfo, infectionCond);
                jbseManager.runAndExtract(methodInfo, jbsePath, false);

                // TODO: Call kill mutant
            }
        }

        classFileManager.restoreOriginalClass();
    }

    public void runOriginal(List<String> methodNames) {
        for (int i = 0; i < methodNames.size(); i++) {
            final String method = methodNames.get(i);

            if (methodNames.contains(method)) {
                runOriginal(method, false);
            }
        }
    }

    public void runOriginal(String method, Boolean violation) {
        final String relativePath = String.format("%s/original", method);
        final String jbseResultPath = pathManager.getJbseResultPath(classBinName, relativePath, 0);
        final MethodInfo methodInfo = classInfo.getMethodInfo(method);

        jbseManager.runAndExtract(methodInfo, jbseResultPath, violation);
    }

    public void runMutants(List<Integer> mutantNumbers, List<String> methodNames) {
        classFileManager.backupOriginalClass();

        for (int mutantNumber : mutantNumbers) {
            MutantId mutId = mutantManager.getMutantId(mutantNumber);
            final String mutatedMethod = mutId.getMutatedMethod();

            MethodInfo methodInfo = classInfo.getMethodInfo(mutatedMethod);
            final String jbseResultPath = pathManager.getJbseResultPath(classBinName, "mutants", mutantNumber);

            if (methodNames.contains(mutatedMethod)) {
                classFileManager.applyMutatedClass(mutantNumber);
                jbseManager.run(methodInfo, jbseResultPath);
            }
        }

        classFileManager.restoreOriginalClass();
    }
}
