package com.github.asu.upload;

import com.github.asu.download.ScriptFilesDownloader;
import com.github.asu.header.HttpHeadersProvider;
import com.github.asu.rest.RestTemplateProvider;
import com.github.asu.scriptfile.ScriptFile;
import com.github.asu.scriptfile.ScriptFileBuilder;
import com.github.asu.scriptfile.ScriptFileType;
import com.github.asu.scriptfile.ScriptFiles;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import testng.MockitoTestNGListener;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

@Listeners({MockitoTestNGListener.class})
public class ScriptFilesUploaderTest {

    private ScriptFilesUploader uploader;
    @Mock
    private RestTemplateProvider restTemplateProvider;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private HttpHeadersProvider headersProvider;
    @Mock
    private ScriptFilesDownloader downloader;
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
    private static final String FILE_1_CONTENT = "file 1 content";
    private static final String FILE_2_ID = "file 2 id";
    private static final String FILE_2_NAME = "file 2 name";
    private static final ScriptFileType FILE_2_TYPE = ScriptFileType.HTML;
    private static final String FILE_2_CONTENT = "file 2 content";
    private ScriptFile scriptFile1;
    private ScriptFile scriptFile2;
    private URI uploadUri;
    private List<String> ignoredFiles;

    @BeforeMethod
    public void setUp() throws Exception {
        given(restTemplateProvider.provide()).willReturn(restTemplate);
        given(headersProvider.provide()).willReturn(headers);
        given(sourceDir.isDirectory()).willReturn(true);
        given(file1.getName()).willReturn(FILE_1_NAME + "." + FILE_1_TYPE.getExtension());
        given(file2.getName()).willReturn(FILE_2_NAME + "." + FILE_2_TYPE.getExtension());
        scriptFile1 = givenScript(FILE_1_ID, FILE_1_NAME, FILE_1_TYPE, FILE_1_CONTENT);
        scriptFile2 = givenScript(FILE_2_ID, FILE_2_NAME, FILE_2_TYPE, FILE_2_CONTENT);
        given(scriptFileBuilder.build(file1)).willReturn(scriptFile1);
        given(scriptFileBuilder.build(file2)).willReturn(scriptFile2);
        ignoredFiles = new ArrayList<String>();
        uploader = new ScriptFilesUploader(restTemplateProvider, headersProvider, downloader, PROJECT_ID, scriptFileBuilder);
        uploadUri = new URI("https://www.googleapis.com/upload/drive/v2/files/" + PROJECT_ID);
    }

    @Test
    public void shouldUploadExistingFiles() {
        // given
        given(downloader.download(PROJECT_ID)).willReturn(givenScripts(newArrayList(scriptFile1, scriptFile2)));
        given(sourceDir.listFiles()).willReturn(files(file1, file2));
        // when
        uploader.upload(sourceDir, ignoredFiles);
        // then
        verify(downloader).download(PROJECT_ID);
        verify(restTemplate).put(eq(uploadUri), requestCaptor.capture());
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
    public void shouldUploadNotExistingFileWithEmptyId() {
        // given
        given(downloader.download(PROJECT_ID)).willReturn(givenScripts(new ArrayList<ScriptFile>()));
        given(sourceDir.listFiles()).willReturn(files(file1));
        // when
        uploader.upload(sourceDir, ignoredFiles);
        // then
        verify(downloader).download(PROJECT_ID);
        verify(restTemplate).put(eq(uploadUri), requestCaptor.capture());
        ScriptFiles request = requestCaptor.getValue().getBody();
        List<ScriptFile> files = request.getFiles();
        assertEquals(files.size(), 1);
        assertEquals(files.get(0).getName(), FILE_1_NAME);
        assertEquals(files.get(0).getFileType(), FILE_1_TYPE);
        assertEquals(files.get(0).getId(), "");
    }

    @Test
    public void shouldNotDeleteExcludedFiles() {
        // given
        given(downloader.download(PROJECT_ID)).willReturn(givenScripts(newArrayList(scriptFile1, scriptFile2)));
        given(sourceDir.listFiles()).willReturn(files(file1));
        ignoredFiles = newArrayList(FILE_2_NAME + "." + FILE_2_TYPE.getExtension());
        // when
        uploader.upload(sourceDir, ignoredFiles);
        // then
        verify(downloader).download(PROJECT_ID);
        verify(restTemplate).put(eq(uploadUri), requestCaptor.capture());
        ScriptFiles request = requestCaptor.getValue().getBody();
        List<ScriptFile> files = request.getFiles();
        assertEquals(files.size(), 2);
        assertEquals(files.get(0).getId(), FILE_1_ID);
        assertEquals(files.get(0).getName(), FILE_1_NAME);
        assertEquals(files.get(0).getFileType(), FILE_1_TYPE);
        assertEquals(files.get(0).getSource(), FILE_1_CONTENT);
        assertEquals(files.get(1).getId(), FILE_2_ID);
        assertEquals(files.get(1).getName(), FILE_2_NAME);
        assertEquals(files.get(1).getFileType(), FILE_2_TYPE);
        assertEquals(files.get(1).getSource(), FILE_2_CONTENT);
    }

    @Test
    public void shouldNotOverrideExcludedFileContent() {
        // given
        given(downloader.download(PROJECT_ID)).willReturn(givenScripts(newArrayList(scriptFile1, scriptFile2)));
        given(sourceDir.listFiles()).willReturn(files(file1, file2));
        ignoredFiles = newArrayList(FILE_2_NAME + "." + FILE_2_TYPE.getExtension());
        // when
        uploader.upload(sourceDir, ignoredFiles);
        // then
        verify(scriptFileBuilder).build(file1);
        verify(scriptFileBuilder, never()).build(file2);
    }

    private File[] files(File... files) {
        return newArrayList(files).toArray(new File[files.length]);
    }

    private ScriptFile givenScript(String id, String name, ScriptFileType type, String content) {
        ScriptFile scriptFile = new ScriptFile();
        scriptFile.setId(id);
        scriptFile.setName(name);
        scriptFile.setFileType(type);
        scriptFile.setSource(content);
        return scriptFile;
    }

    private ScriptFiles givenScripts(List<ScriptFile> files) {
        ScriptFiles scriptFiles = new ScriptFiles();
        scriptFiles.setFiles(files);
        return scriptFiles;
    }
}
