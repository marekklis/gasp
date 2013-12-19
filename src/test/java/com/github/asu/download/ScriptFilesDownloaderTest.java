package com.github.asu.download;

import com.github.asu.header.HttpHeadersProvider;
import com.github.asu.rest.RestTemplateProvider;
import com.github.asu.scriptfile.ScriptFiles;
import org.apache.maven.plugin.MojoExecutionException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import testng.MockitoTestNGListener;

import java.net.URI;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

@Listeners({MockitoTestNGListener.class})
public class ScriptFilesDownloaderTest {

    @InjectMocks
    private ScriptFilesDownloader downloader;
    @Mock
    private RestTemplateProvider restTemplateProvider;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private HttpHeadersProvider headerProvider;
    @Mock
    private HttpHeaders headers;
    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private static final String PROJECT_ID = "project_id";
    private static final ScriptFiles SCRIPT_FILES = new ScriptFiles();
    private URI downloadUri;

    @BeforeMethod
    public void setUp() throws Exception {
        given(restTemplateProvider.provide()).willReturn(restTemplate);
        given(headerProvider.provide()).willReturn(headers);
        downloadUri = new URI("https://script.google.com/feeds/download/export?id=" + PROJECT_ID + "&format=json");
    }

    @Test
    public void shouldCallDownloadDriveAPI() throws MojoExecutionException {
        // given
        given(restTemplate.exchange(eq(downloadUri), eq(HttpMethod.GET), any(HttpEntity.class), eq(ScriptFiles.class))).willReturn(responseWithBody(SCRIPT_FILES));
        // when
        ScriptFiles files = downloader.download(PROJECT_ID);
        // then
        verify(restTemplate).exchange(eq(downloadUri), eq(HttpMethod.GET), requestCaptor.capture(), eq(ScriptFiles.class));
        assertEquals(requestCaptor.getValue().getHeaders(), headers);
        assertEquals(files, SCRIPT_FILES);
    }

    private ResponseEntity<ScriptFiles> responseWithBody(ScriptFiles scriptFiles) {
        return new ResponseEntity<ScriptFiles>(scriptFiles, HttpStatus.OK);
    }
}
