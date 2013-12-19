package com.github.asu.scriptfile;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class ScriptFileBuilder {

    public ScriptFile build(File sourceFile) {
        ScriptFile file = new ScriptFile();
        String filename = sourceFile.getName();
        file.setName(Files.getNameWithoutExtension(filename));
        file.setFileType(getFileType(filename));
        try {
            file.setSource(Files.toString(sourceFile, Charset.forName("UTF-8")));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return file;
    }

    private ScriptFileType getFileType(String filename) {
        String fileExtension = Files.getFileExtension(filename);
        return ScriptFileType.typeByExt(fileExtension);
    }
}
