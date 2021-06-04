package com.cs453.group5.symbolic.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs453.group5.symbolic.entities.MutantId;
import com.cs453.group5.symbolic.entities.Pair;
import com.cs453.group5.symbolic.executors.PitestExecutor;
import com.cs453.group5.symbolic.parsers.MutantDetailsParser;
import com.cs453.group5.symbolic.parsers.MutationsParser;

public class MutantManager {
    private MutationsParser mutParser;
    private MutantDetailsParser detailsParser;
    private PitestExecutor pitExecutor;

    public MutantManager(MutationsParser mutParser, MutantDetailsParser detailsParser, PitestExecutor pitExecutor) {
        this.mutParser = mutParser;
        this.detailsParser = detailsParser;
        this.pitExecutor = pitExecutor;
    }

    public Map<String, List<Pair<Integer, MutantId>>> getAliveMutants() {
        Map<String, List<Pair<Integer, MutantId>>> result = new HashMap<>();

        if (!pitExecutor.pitReportExists()) {
            pitExecutor.runPitest();
        }

        Set<MutantId> survivedMutIdSet = mutParser.getSurvivedMutantIds();
        List<Pair<Integer, MutantId>> mutIds = detailsParser.getAllMutantDetails();

        List<Pair<Integer, MutantId>> survivedPairs = mutIds.stream()
                .filter((p) -> survivedMutIdSet.contains(p.getSecond())).collect(Collectors.toList());

        for (Pair<Integer, MutantId> pair : survivedPairs) {
            String method = pair.getSecond().getMutatedMethod();
            List<Pair<Integer, MutantId>> item = result.get(method);

            if (item == null) {
                result.put(method, new ArrayList<>(Arrays.asList(pair)));
            } else {
                item.add(pair);
            }
        }

        return result;
    }

    public MutantId getMutantId(int mutantNumber) {
        return detailsParser.getMutantDetails(mutantNumber);
    }
}
