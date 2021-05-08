package com.cs453.group5.symbolic;

import java.util.Set;
import java.util.function.Consumer;

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

        /* Execute JBSE for each living mutants */
        final MutationsParser mutParser = new MutationsParser(pathManager.getRecentPitestReportPath());
        final MutantDetailsParser mutDetailParser = new MutantDetailsParser(
                pathManager.getMutantsDirPath(classBinaryName));
        final JbseExecutor jbseExecutor = new JbseExecutor();

        final Set<MutantId> mutIdSet = mutParser.getSurvivedMutantIds();

        MutantId mutId;
        int i = 0;
        while ((mutId = mutDetailParser.getMutantDetails(i++)) != null) {
            if (mutIdSet.contains(mutId)) {
                System.out.println(String.format("mutant#%d was survived.", i - 1));
                /* TODO: Instrument java byte code of this mutant */
                /* TODO: Run JBSE */
            }
        }
    }
}
