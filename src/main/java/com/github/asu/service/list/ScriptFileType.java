package com.github.asu.service.list;

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

    public static final ScriptFileType getType(String description) {
        for (ScriptFileType type : values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknow script file description");
    }
}
