package com.cs453.group5.symbolic.entities;

/**
 * This class represents the class binary name. This class sanitizes the raw
 * class binary name that the user wrote. By using this class, we can clearly
 * find dot-syntax name and slash-syntax name.
 * 
 * @author Yeongil Yoon
 */
public class ClassBinName {
    private String dotFormName;
    private String slashFormName;

    public ClassBinName(String classBinName) {
        String slashFormName;

        if (classBinName.contains("/")) {
            slashFormName = classBinName;
        } else {
            slashFormName = classBinName.replace(".", "/");
        }

        this.slashFormName = sanitize(slashFormName);
        this.dotFormName = slashFormName.replace("/", ".");
    }

    /**
     * 
     * @return Class binary name with dot-syntax
     */
    public String getDot() {
        return dotFormName;
    }

    /**
     * 
     * @return Class binary name with slash-syntax
     */
    public String getSlash() {
        return slashFormName;
    }

    private String sanitize(String classBinName) {
        classBinName = classBinName.trim();
        int startIdx, endIdx;

        for (endIdx = classBinName.length() - 1; endIdx >= 0 && classBinName.charAt(endIdx) == '/'; endIdx--)
            ;
        for (startIdx = 0; startIdx < classBinName.length() && classBinName.charAt(startIdx) == '/'; startIdx++)
            ;

        if (startIdx == classBinName.length() || endIdx < 0) {
            throw new IllegalArgumentException("Illegal class binary name");
        }

        return classBinName.substring(startIdx, endIdx + 1);
    }
}
