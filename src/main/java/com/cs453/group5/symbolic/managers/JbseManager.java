package com.cs453.group5.symbolic.managers;

import com.cs453.group5.symbolic.entities.Assumption;
import com.cs453.group5.symbolic.entities.MethodInfo;
import com.cs453.group5.symbolic.executors.JbseExecutor;
import com.cs453.group5.symbolic.executors.PathFinderExecutor;
import com.cs453.group5.symbolic.parsers.JbseResultParser;

public class JbseManager {
    private JbseExecutor jbseExecutor;
    private JbseResultParser jbseResultParser;
    private PathFinderExecutor pathFinderExecutor;

    public JbseManager(JbseExecutor jbseExecutor, JbseResultParser jbseResultParser,
            PathFinderExecutor pathFinderExecutor) {
        this.jbseExecutor = jbseExecutor;
        this.jbseResultParser = jbseResultParser;
        this.pathFinderExecutor = pathFinderExecutor;
    }

    /**
     * Run jbse
     * 
     * @param methodInfo
     * @param jbseResultPath
     */
    public void run(MethodInfo methodInfo, String jbseResultPath) {
        jbseExecutor.runJbse(jbseResultPath, methodInfo);
    }

    /**
     * Run jbse and extract violation
     * 
     * @param methodInfo
     * @param jbseResultPath
     * @param violation
     */
    public void runAndExtract(MethodInfo methodInfo, String jbseResultPath, Boolean violation) {
        jbseExecutor.runJbse(jbseResultPath, methodInfo);
        jbseResultParser.extract(jbseResultPath, violation);
    }

    /**
     * Find path condition with jbse report.
     * 
     * @param methodInfo
     * @param jbseResultPath
     * @return
     */
    public Assumption findPathCond(MethodInfo methodInfo, String jbseResultPath, int mutantId) {
        return new Assumption(0, pathFinderExecutor.findPathCond(jbseResultPath, methodInfo, mutantId));
    }

    public void findKillCond(MethodInfo methodInfo, String jbseResultPath, int mutantId) {
        pathFinderExecutor.findKillCond(jbseResultPath, methodInfo, mutantId);
    }
}
