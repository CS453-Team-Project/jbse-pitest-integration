package com.cs453.group5.symbolic.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cs453.group5.symbolic.entities.Assumption;
import com.cs453.group5.symbolic.entities.ClassBinName;

import org.json.JSONObject;
import org.json.JSONArray;

public class UserAssumeParser {
    private final String jsonPath;
    private final ClassBinName classBinName;

    public UserAssumeParser(String jsonPath, ClassBinName classBinName) {
        this.jsonPath = jsonPath;
        this.classBinName = classBinName;
    }

    public List<Assumption> parseUserAssume() {
        List<Assumption> result = new ArrayList<>();

        File jsonFile = new File(this.jsonPath);

        String jsonString;
        String dotClassName = classBinName.getDot();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(jsonFile));
            jsonString = bufferedReader.lines().collect(Collectors.joining(""));
            bufferedReader.close();
        } catch (FileNotFoundException e1) {
            throw new IllegalArgumentException("File not found: " + jsonPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException during user assume parsing");
        }

        JSONArray jsonArray = new JSONArray(jsonString);
        JSONArray classAssumes = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject candidate = (JSONObject) jsonArray.get(i);
            if (dotClassName.equals(candidate.getString("class"))) {
                classAssumes = candidate.getJSONArray("assumes");
                break;
            }
        }

        if (classAssumes != null) {
            for (int i = 0; i < classAssumes.length(); i++) {
                JSONObject assumeInfo = classAssumes.getJSONObject(i);

                String assume = assumeInfo.getString("assume");
                int line = assumeInfo.getInt("line");

                result.add(new Assumption(line, assume));
            }
        }

        return result;
    }
}
