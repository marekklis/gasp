package com.github.asu.scriptfile;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import testng.MockitoTestNGListener;

import java.io.File;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

@Listeners({MockitoTestNGListener.class})
public class ScriptFileSaverTest {

    @InjectMocks
    private ScriptFileSaver saver;
    @Mock
    private File downloadDir;

    private static final ScriptFile SCRIPT_FILE = new ScriptFile();

    @Test
    public void shouldThrowExceptionIfDirIsNotDirectory() {
        // given
        given(downloadDir.isDirectory()).willReturn(false);
        try {
            // when
            saver.save(downloadDir, SCRIPT_FILE);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException exception) {
            // then
            assertEquals(exception.getMessage(), "downloadDir is not a directory");
        }
    }
}
