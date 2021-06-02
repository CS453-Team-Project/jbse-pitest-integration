package com.cs453.group5.symbolic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cs453.group5.symbolic.entities.ClassBinName;
import com.cs453.group5.symbolic.entities.ClassInfo;
import com.cs453.group5.symbolic.entities.MethodInfo;
import com.cs453.group5.symbolic.entities.MutantId;
import com.cs453.group5.symbolic.managers.ClassFileManager;
import com.cs453.group5.symbolic.managers.JbseManager;
import com.cs453.group5.symbolic.managers.MutantManager;
import com.cs453.group5.symbolic.managers.PathManager;

public class Run {
    ClassFileManager classFileManager;
    PathManager pathManager;
    ClassInfo classInfo;
    ClassBinName classBinName;

    MutantManager mutantManager;
    JbseManager jbseManager;

    public Run(PathManager pathManager, ClassFileManager classFileManager, MutantManager mutantManager,
            JbseManager jbseManager, ClassInfo classInfo) {

        this.pathManager = pathManager;
        this.classFileManager = classFileManager;
        this.mutantManager = mutantManager;
        this.jbseManager = jbseManager;
        this.classInfo = classInfo;

        this.classBinName = classInfo.getBinaryName();
    }

    public void run(List<String> methodNames) {
        final UserAssume userAssume = new UserAssume(classBinName.getDot());

        // Backup original class
        classFileManager.backupOriginalClass();

        // Get alived Mutants
        Map<Integer, MutantId> aliveMutantIds = mutantManager.getAliveMutants();
        List<Map.Entry<Integer, MutantId>> mutantsWithNumber = aliveMutantIds.entrySet().stream()
                .collect(Collectors.toList());

        for (Map.Entry<Integer, MutantId> mutNum : mutantsWithNumber) {
            int mutantNumber = mutNum.getKey();
            MutantId mutId = mutNum.getValue();

            final String mutatedMethod = mutId.getMutatedMethod();
            if (methodNames != null && !methodNames.contains(mutatedMethod)) {
                continue;
            }

            final int mutatedLine = mutId.getLine();
            final MethodInfo methodInfo = classInfo.getMethodInfo(mutatedMethod);

            final String jbseRPath = pathManager.getJbseResultPath(classBinName, "reachability", mutantNumber);
            final String jbseIPath = pathManager.getJbseResultPath(classBinName, "infection", mutantNumber);
            final String jbsePPath = pathManager.getJbseResultPath(classBinName, "propagation", mutantNumber);
            final String jbseOPath = pathManager.getJbseResultPath(classBinName, "origin", mutantNumber);

            MutantTransformer mutTransformer = new MutantTransformer(classBinName.getDot(), mutatedMethod,
                    pathManager.getClassDirPath(), userAssume);

            // Finding R condition
            classFileManager.applyMutatedClass(mutantNumber);
            mutTransformer.insertBytecode(mutatedLine, "jbse.meta.Analysis.ass3rt(false);");
            jbseManager.runAndExtract(methodInfo, jbseRPath, true);
            String reachabilityCond = jbseManager.findPathCond(methodInfo, jbseRPath);

            // Finding I condition
            classFileManager.applyMutatedClass(mutantNumber);
            mutTransformer.insertBoth(mutatedLine, reachabilityCond, "jbse.meta.Analysis.ass3rt(false);");
            jbseManager.runAndExtract(methodInfo, jbseIPath, true);
            String infectionCond;
            try {
                infectionCond = jbseManager.findPathCond(methodInfo, jbseIPath);
            } catch (Exception e) {
                e.printStackTrace();
                infectionCond = reachabilityCond;
            }

            // Comparing mutant and origin
            // mutant
            classFileManager.applyMutatedClass(mutantNumber);
            mutTransformer.insertBoth(mutatedLine, infectionCond, "");
            jbseManager.runAndExtract(methodInfo, jbsePPath, false);

            // origin
            classFileManager.restoreOriginalClass();
            mutTransformer.insertBoth(mutatedLine, infectionCond, "");
            jbseManager.runAndExtract(methodInfo, jbseOPath, false);
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
