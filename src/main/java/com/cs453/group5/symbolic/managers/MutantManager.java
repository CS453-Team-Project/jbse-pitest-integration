package com.cs453.group5.symbolic.managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import com.cs453.group5.symbolic.entities.MutantId;
import com.cs453.group5.symbolic.executors.PitestExecutor;
import com.cs453.group5.symbolic.parsers.MutantDetailsParser;
import com.cs453.group5.symbolic.parsers.MutationsParser;

import org.xml.sax.SAXException;

public class MutantManager {
    private MutationsParser mutParser;
    private MutantDetailsParser detailsParser;
    private PitestExecutor pitExecutor;

    public MutantManager(MutationsParser mutParser, MutantDetailsParser detailsParser, PitestExecutor pitExecutor) {
        this.mutParser = mutParser;
        this.detailsParser = detailsParser;
        this.pitExecutor = pitExecutor;
    }

    public Map<Integer, MutantId> getAliveMutants() {
        Map<Integer, MutantId> result = new HashMap<Integer, MutantId>();

        if (!pitExecutor.pitReportExists()) {
            int exitcode = pitExecutor.runPitest();
            if (exitcode != 0) {
                // throw
                return result;
            }
        }

        try {
            Set<MutantId> mutIdSet = mutParser.getSurvivedMutantIds();
            MutantId mutId;
            int i = 0;
            while ((mutId = detailsParser.getMutantDetails(i++)) != null) {
                if (mutIdSet.contains(mutId)) {
                    result.put(i - 1, mutId);
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException | InterruptedException e) {
            // throw
            e.printStackTrace();
        }

        return result;
    }

    public MutantId getMutantId(int mutantNumber) {
        return detailsParser.getMutantDetails(mutantNumber);
    }
}
