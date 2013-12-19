package com.github.asu.service.upload;

import com.github.asu.service.list.ScriptFilesLister;
import com.github.asu.service.scriptfile.ScriptFile;
import com.github.asu.service.scriptfile.ScriptFileBuilder;
import com.github.asu.service.scriptfile.ScriptFileType;
import com.github.asu.service.scriptfile.ScriptFiles;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import testng.MockitoTestNGListener;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

@Listeners({MockitoTestNGListener.class})
public class ScriptFilesUploaderTest {

    private ScriptFilesUploader uploader;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ScriptFilesLister filesLister;
    @Mock
    private File sourceDir;
    @Mock
    private File file1;
    @Mock
    private File file2;
    @Mock
    private HttpHeaders headers;
    @Mock
    private ScriptFileBuilder scriptFileBuilder;
    @Captor
    private ArgumentCaptor<HttpEntity<ScriptFiles>> requestCaptor;

    private static final String PROJECT_ID = "project_id";
    private static final String FILE_1_ID = "file 1 id";
    private static final String FILE_1_NAME = "file 1 name";
    private static final ScriptFileType FILE_1_TYPE = ScriptFileType.JAVASCRIPT;
    private static final String FILE_2_ID = "file 2 id";
    private static final String FILE_2_NAME = "file 2 name";
    private static final ScriptFileType FILE_2_TYPE = ScriptFileType.HTML;
    private ScriptFile scriptFile1;
    private ScriptFile scriptFile2;
    private URI uploadUri;

    @BeforeMethod
    public void setUp() throws Exception {
        given(sourceDir.isDirectory()).willReturn(true);
        given(file1.getName()).willReturn(FILE_1_NAME + "." + FILE_1_TYPE.getExtension());
        given(file2.getName()).willReturn(FILE_2_NAME + "." + FILE_2_TYPE.getExtension());
        scriptFile1 = givenScript(FILE_1_ID, FILE_1_NAME, FILE_1_TYPE);
        scriptFile2 = givenScript(FILE_2_ID, FILE_2_NAME, FILE_2_TYPE);
        given(scriptFileBuilder.build(file1)).willReturn(scriptFile1);
        given(scriptFileBuilder.build(file2)).willReturn(scriptFile2);
        uploader = new ScriptFilesUploader(restTemplate, headers, filesLister, PROJECT_ID, scriptFileBuilder);
        uploadUri = new URI("https://www.googleapis.com/upload/drive/v2/files/" + PROJECT_ID);
    }

    @Test
    public void shouldUploadExistingFiles() throws URISyntaxException {
        // given
        given(filesLister.listFiles(PROJECT_ID)).willReturn(givenScripts(newArrayList(scriptFile1, scriptFile2)));
        given(sourceDir.listFiles()).willReturn(files(file1, file2));
        // when
        uploader.upload(sourceDir);
        // then
        verify(filesLister).listFiles(PROJECT_ID);
        verify(restTemplate).exchange(eq(uploadUri), eq(HttpMethod.PUT), requestCaptor.capture(), eq(String.class));
        ScriptFiles request = requestCaptor.getValue().getBody();
        List<ScriptFile> files = request.getFiles();
        assertEquals(files.size(), 2);
        assertEquals(files.get(0).getName(), FILE_1_NAME);
        assertEquals(files.get(0).getFileType(), FILE_1_TYPE);
        assertEquals(files.get(0).getId(), FILE_1_ID);
        assertEquals(files.get(1).getName(), FILE_2_NAME);
        assertEquals(files.get(1).getFileType(), FILE_2_TYPE);
        assertEquals(files.get(1).getId(), FILE_2_ID);
    }

    @Test
    public void shouldUploadNotExistingFileWithEmptyId() throws URISyntaxException {
        // given
        given(filesLister.listFiles(PROJECT_ID)).willReturn(givenScripts(new ArrayList<ScriptFile>()));
        given(sourceDir.listFiles()).willReturn(files(file1));
        // when
        uploader.upload(sourceDir);
        // then
        verify(filesLister).listFiles(PROJECT_ID);
        verify(restTemplate).exchange(eq(uploadUri), eq(HttpMethod.PUT), requestCaptor.capture(), eq(String.class));
        ScriptFiles request = requestCaptor.getValue().getBody();
        List<ScriptFile> files = request.getFiles();
        assertEquals(files.size(), 1);
        assertEquals(files.get(0).getName(), FILE_1_NAME);
        assertEquals(files.get(0).getFileType(), FILE_1_TYPE);
        assertEquals(files.get(0).getId(), "");
    }

    private File[] files(File... files) {
        return newArrayList(files).toArray(new File[files.length]);
    }

    private ScriptFile givenScript(String id, String name, ScriptFileType type) {
        ScriptFile scriptFile = new ScriptFile();
        scriptFile.setId(id);
        scriptFile.setName(name);
        scriptFile.setFileType(type);
        scriptFile.setSource("some source");
        return scriptFile;
    }

    private ScriptFiles givenScripts(List<ScriptFile> files) {
        ScriptFiles scriptFiles = new ScriptFiles();
        scriptFiles.setFiles(files);
        return scriptFiles;
    }
}
