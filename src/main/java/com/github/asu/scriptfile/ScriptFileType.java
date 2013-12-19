package com.github.asu.scriptfile;

public enum ScriptFileType {

    JAVASCRIPT("server_js", "gs"),
    HTML("html", "html");

    private final String description;
    private final String extension;

    ScriptFileType(String description, String extension) {

        this.description = description;
        this.extension = extension;
    }

    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }

    public static final ScriptFileType typeByDesc(String description) {
        for (ScriptFileType type : values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknow script file description");
    }

    public static final ScriptFileType typeByExt(String extension) {
        for (ScriptFileType type : values()) {
            if (type.extension.equals(extension)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown script file extension");
    }
}
