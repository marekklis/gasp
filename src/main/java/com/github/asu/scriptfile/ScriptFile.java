package com.github.asu.scriptfile;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ScriptFile {

    private String id;
    private String name;
    @JsonIgnore
    private ScriptFileType fileType;
    private String type;
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScriptFileType getFileType() {
        return fileType;
    }

    public void setFileType(ScriptFileType fileType) {
        this.fileType = fileType;
        this.type = fileType.getDescription();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.fileType = ScriptFileType.typeByDesc(type);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String fileName() {
        return name + "." + fileType.getExtension();
    }

    @Override
    public String toString() {
        return "ScriptFile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", source='" + source + '\'' +
                '}';
    }
}
