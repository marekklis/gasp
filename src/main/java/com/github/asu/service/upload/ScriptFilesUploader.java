package com.github.asu.service.upload;

import com.github.asu.service.list.ScriptFilesLister;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class ScriptFilesUploader {

    private RestTemplate restTemplate;
    private ScriptFilesLister lister;

    public ScriptFilesUploader(RestTemplate restTemplate, ScriptFilesLister lister) {
        this.restTemplate = restTemplate;
        this.lister = lister;
    }

    public void upload(File sourceDir) {

    }
}
