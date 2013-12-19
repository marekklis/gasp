package com.github.asu.scriptfile;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkArgument;

public class ScriptFileSaver {

    public void save(File dir, ScriptFile scriptFile) {
        checkArgument(dir.isDirectory(), "downloadDir is not a directory");
        File file = new File(dir, fileName(scriptFile));
        try {
            Files.append(scriptFile.getSource(), file, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String fileName(ScriptFile scriptFile) {
        return scriptFile.getName() + "." + scriptFile.getFileType().getExtension();
    }
}
