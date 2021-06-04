package com.cs453.group5.symbolic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONArray;

public class UserAssume {
    private final String jsonPath;
    private final JSONObject jsonObject;

    public UserAssume(String classBinaryName) {
        String projectHome = System.getenv("CS453_PROJECT_HOME");
        this.jsonPath = String.format("%s/assume/my_tool_assume.json", projectHome);

        // assume option
        JSONObject obj = null;
        try {
            File assumeInfo = new File(this.jsonPath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(assumeInfo));
            Stream<String> jsonStream = bufferedReader.lines();
            String jsonString = jsonStream.collect(Collectors.joining(""));
            bufferedReader.close();

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject candidate = (JSONObject) jsonArray.get(i);
                if (classBinaryName.equals(candidate.getString("class"))) {
                    obj = candidate;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.jsonObject = obj;
        }
    }

    // public int getLine() {
    // if (this.jsonObject == null) {
    // return -1;
    // }
    // return this.jsonObject.getInt("line");
    // }

    // public String getCommand() {
    // if (this.jsonObject == null) {
    // return "";
    // }
    // String command = this.jsonObject.getString("assume");
    // return command;
    // }

    public JSONArray getAssumes() {
        if (this.jsonObject == null) {
            return null;
        }
        return this.jsonObject.getJSONArray("assumes");
    }
}
