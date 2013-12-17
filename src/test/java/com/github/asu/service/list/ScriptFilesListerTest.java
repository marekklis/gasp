package com.github.asu.service.list;

import com.github.asu.service.header.HttpHeaderProvider;
import com.github.asu.service.rest.RestTemplateProvider;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ScriptFilesListerTest {

    @Test
    public void shouldListProjectFiles() throws MojoExecutionException {
        // given
        String accessToken = "ya29.1.AADtN_WdoWXGe3pc08BJH6_H_ItR_9PqLXCPWbHX_4uVo_E1qfULauECtCq9QA67iUMICQ";
        String projectId = "1g502drF5fuEA4TKhgrziybO02iTuuvfHXb3UeurCLbCChbpYOBjwfwbz";
        RestTemplate restTemplate = new RestTemplateProvider().provide();
        HttpHeaders headers = new HttpHeaderProvider(accessToken).provide();
        ScriptFilesLister lister = new ScriptFilesLister(restTemplate, headers);
        // when
        ScriptFiles files = lister.listFiles(projectId);
        // then
        assertEquals(files.getFiles().size(), 21);
    }
}
