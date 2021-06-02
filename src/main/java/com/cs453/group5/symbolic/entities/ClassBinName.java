package com.cs453.group5.symbolic.entities;

public class ClassBinName {
    private String dotFormName;
    private String slashFormName;

    public ClassBinName(String classBinName) {
        if (classBinName.contains(".")) {
            dotFormName = classBinName;
            slashFormName = classBinName.replace(".", "/");
        } else {
            slashFormName = classBinName;
            dotFormName = classBinName.replace("/", ".");
        }
    }

    public String getDot() {
        return dotFormName;
    }

    public String getSlash() {
        return slashFormName;
    }
}
