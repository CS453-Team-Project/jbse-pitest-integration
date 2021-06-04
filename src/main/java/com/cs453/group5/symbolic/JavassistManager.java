package com.cs453.group5.symbolic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cs453.group5.symbolic.entities.Assertion;
import com.cs453.group5.symbolic.entities.Assumption;
import com.cs453.group5.symbolic.entities.MethodInfo;
import com.cs453.group5.symbolic.executors.MutantTransformer;
import com.cs453.group5.symbolic.parsers.UserAssumeParser;

public class JavassistManager {
    private UserAssumeParser assumeParser;
    private MutantTransformer mutTransformer;
    private List<Assumption> userAssumes = null;

    public JavassistManager(UserAssumeParser assumeParser, MutantTransformer mutTransformer) {
        this.assumeParser = assumeParser;
        this.mutTransformer = mutTransformer;
    }

    public void insert(MethodInfo methodInfo, List<Assertion> assertions, List<Assumption> assumptions) {
        if (userAssumes == null) {
            userAssumes = assumeParser.parseUserAssume();
        }

        List<Assertion> assertList = new ArrayList<>();
        List<Assumption> assumeList = new ArrayList<>(userAssumes);

        assertList.addAll(assertions);
        assumeList.addAll(assumptions);

        mutTransformer.insert(methodInfo, assertList, assumeList);
    }

    public void insert(MethodInfo methodInfo, Assertion assertion) {
        this.insert(methodInfo, Arrays.asList(assertion), new ArrayList<Assumption>());
    }

    public void insert(MethodInfo methodInfo, Assumption assumption) {
        this.insert(methodInfo, new ArrayList<Assertion>(), Arrays.asList(assumption));
    }

    public void insert(MethodInfo methodInfo, Assertion assertion, Assumption assumption) {
        this.insert(methodInfo, Arrays.asList(assertion), Arrays.asList(assumption));
    }
}
