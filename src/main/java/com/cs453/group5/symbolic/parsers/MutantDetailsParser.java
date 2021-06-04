package com.cs453.group5.symbolic.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.cs453.group5.symbolic.entities.MutantId;
import com.cs453.group5.symbolic.entities.Pair;

public class MutantDetailsParser {
    private String mutantsDirPath;
    private final String splitStr = "clazz=|method=|methodDesc=|indexes=|mutator=|block=|lineNumber=";

    public MutantDetailsParser(String mutantsDirPath) {
        this.mutantsDirPath = mutantsDirPath;
    }

    /**
     * Parse the details.txt which is in MutantsDirPath/{mutantIndex} and return
     * MutantId. If there is no such mutant, return null.
     * 
     * @param mutantIndex
     * @return MutantId if exists. Otherwise, null
     */
    public MutantId getMutantDetails(int mutantIndex) {
        final String detailsPath = String.format("%s/%d/details.txt", mutantsDirPath, mutantIndex);
        String details = "";

        try {
            File detailsFile = new File(detailsPath);
            if (!detailsFile.exists()) {
                return null;
            }

            Scanner scanner = new Scanner(detailsFile);

            while (scanner.hasNextLine()) {
                details += scanner.nextLine();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            return null;
        }

        String[] strArr = details.split(splitStr);

        String className = strArr[1].split(",")[0];
        String method = strArr[2].split(",")[0];
        String methodDesc = strArr[3].split("],")[0];
        String mutator = strArr[5].split("],")[0];

        String index_s = strArr[4].replace("[", "").replace("]", "").split(",")[0];
        String block_s = strArr[6].split(",")[0];
        String lineNumber_s = strArr[7].split(",")[0];

        int index = Integer.parseInt(index_s);
        int block = Integer.parseInt(block_s);
        int lineNumber = Integer.parseInt(lineNumber_s);

        return new MutantId(className, method, methodDesc, mutator, index, block, lineNumber);
    }

    /**
     * Parse details of all mutants and return as a list.
     * 
     * @see #getMutantDetails
     * @return MutantId
     */
    public List<Pair<Integer, MutantId>> getAllMutantDetails() {
        List<Pair<Integer, MutantId>> result = new ArrayList<>();

        int i = 0;
        MutantId mutId;
        while ((mutId = getMutantDetails(i++)) != null) {
            result.add(new Pair<Integer, MutantId>(i - 1, mutId));
        }

        assert (false);

        return result;
    }
}
