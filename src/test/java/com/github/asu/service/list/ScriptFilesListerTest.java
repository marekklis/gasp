package com.github.asu.service.list;

import com.github.asu.service.header.HttpHeaderProvider;
import com.github.asu.service.rest.RestTemplateProvider;
import com.github.asu.service.scriptfile.ScriptFiles;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ScriptFilesListerTest {

    @Test(enabled = false)
    public void shouldListProjectFiles() throws MojoExecutionException {
        // given
        String accessToken = "ya29.1.AADtN_UwXi2p4kXAcBlQZJrB9BUyNJQIiECO0lTj4z3g_OdBUYqumG6V6JUwNJtbCMyv2w";
        String projectId = "17C2Ddo_wUSKlqJUqACWsSGSVgbw4aZFod5ggvRiudl57dJF02V5VK2BI";
        RestTemplate restTemplate = new RestTemplateProvider().provide();
        HttpHeaders headers = new HttpHeaderProvider(accessToken).provide();
        ScriptFilesLister lister = new ScriptFilesLister(restTemplate, headers);
        // when
        ScriptFiles files = lister.listFiles(projectId);
        // then
        assertEquals(files.getFiles().size(), 21);
    }
}
