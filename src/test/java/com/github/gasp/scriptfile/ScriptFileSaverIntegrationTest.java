package com.github.gasp.scriptfile;

import com.google.common.io.Files;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ScriptFileSaverIntegrationTest {

    private ScriptFileSaver saver;
    private File downloadDir;
    private ScriptFile scriptFile;

    private static final String FILE_NAME = "fileName";
    public static final ScriptFileType FILE_TYPE = ScriptFileType.JAVASCRIPT;

    @BeforeMethod
    public void setUp() throws Exception {
        saver = new ScriptFileSaver();
        downloadDir = Files.createTempDir();
        scriptFile = new ScriptFile();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        downloadDir.delete();
    }

    @Test
    public void shouldSaveScriptToFile() throws IOException {
        // given
        scriptFile.setFileType(FILE_TYPE);
        scriptFile.setName(FILE_NAME);
        scriptFile.setSource("script\nfile\ncontent\n");
        // when
        saver.save(downloadDir, scriptFile);
        // then
        File file = new File(downloadDir, FILE_NAME + "." + FILE_TYPE.getExtension());
        assertTrue(file.exists(), "file doesn't exists");
        List<String> fileLines = Files.readLines(file, Charset.forName("UTF-8"));
        assertEquals(fileLines.size(), 3);
        assertEquals(fileLines.get(0), "script");
        assertEquals(fileLines.get(1), "file");
        assertEquals(fileLines.get(2), "content");
    }

}
