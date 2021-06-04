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
            // TODO: run original
            List<Pair<Integer, MutantId>> pairs = aliveMutantIds.get(method);

            for (Pair<Integer, MutantId> pair : pairs) {
                int mutantNumber = pair.getFirst();
                MutantId mutId = pair.getSecond();

                final int mutatedLine = mutId.getLine();
                final MethodInfo methodInfo = classInfo.getMethodInfo(method);

                final Assertion falseAssert = new Assertion(mutatedLine, "false");
                final Assertion falseAssertAfter = new Assertion(mutatedLine + 1, "false");

                final String jbseRPath = pathManager.getJbseResultPath(classBinName, "reachability", mutantNumber);
                final String jbseIPath = pathManager.getJbseResultPath(classBinName, "infection", mutantNumber);
                final String jbsePPath = pathManager.getJbseResultPath(classBinName, "propagation", mutantNumber);
                final String jbseOPath = pathManager.getJbseResultPath(classBinName, "origin", mutantNumber);

                // Finding R condition
                classFileManager.applyMutatedClass(mutantNumber);
                javssManager.insert(methodInfo, falseAssert);
                jbseManager.runAndExtract(methodInfo, jbseRPath, true);
                Assumption reachabilityCond = jbseManager.findPathCond(methodInfo, jbseRPath);

                // Finding I condition
                classFileManager.applyMutatedClass(mutantNumber);
                javssManager.insert(methodInfo, falseAssertAfter, reachabilityCond);
                jbseManager.runAndExtract(methodInfo, jbseIPath, true);
                Assumption infectionCond;
                try {
                    infectionCond = jbseManager.findPathCond(methodInfo, jbseIPath);
                } catch (IllegalPathFinderOutputException e) {
                    System.out.println("No infection condition. Proceed with reachability condition.");
                    infectionCond = reachabilityCond;
                }

                // Comparing mutant and origin
                // mutant
                classFileManager.applyMutatedClass(mutantNumber);
                javssManager.insert(methodInfo, infectionCond);
                jbseManager.runAndExtract(methodInfo, jbsePPath, false);

                // TODO: Remove this part
                // origin
                classFileManager.restoreOriginalClass();
                javssManager.insert(methodInfo, infectionCond);
                jbseManager.runAndExtract(methodInfo, jbseOPath, false);

                // TODO: Call kill mutant
            }
        }

        classFileManager.restoreOriginalClass();
    }

    public void runOriginal(List<String> methodNames) {
        for (int i = 0; i < methodNames.size(); i++) {
            final String method = methodNames.get(i);
            final String jbseResultPath = pathManager.getJbseResultPath(classBinName, "origin/" + method, 0);
            final MethodInfo methodInfo = classInfo.getMethodInfo(method);

            if (methodNames.contains(method)) {
                jbseManager.run(methodInfo, jbseResultPath);
            }
        }
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
