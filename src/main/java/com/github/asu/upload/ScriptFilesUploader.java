package com.github.asu.upload;

import com.github.asu.download.ScriptFilesDownloader;
import com.github.asu.header.HttpHeadersProvider;
import com.github.asu.rest.RestTemplateProvider;
import com.github.asu.scriptfile.ScriptFile;
import com.github.asu.scriptfile.ScriptFileBuilder;
import com.github.asu.scriptfile.ScriptFileType;
import com.github.asu.scriptfile.ScriptFiles;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ScriptFilesUploader {

    private RestTemplateProvider restTemplateProvider;
    private ScriptFilesDownloader downloader;
    private ScriptFileBuilder scriptFileBuilder;
    private final String projectId;
    private final HttpHeadersProvider headersProvider;

    public ScriptFilesUploader(RestTemplateProvider restTemplateProvider, HttpHeadersProvider headersProvider,
                               ScriptFilesDownloader downloader, String projectId,
                               ScriptFileBuilder scriptFileBuilder) {
        this.restTemplateProvider = restTemplateProvider;
        this.downloader = downloader;
        this.projectId = projectId;
        this.headersProvider = headersProvider;
        this.scriptFileBuilder = scriptFileBuilder;
    }

    public void upload(File sourceDir) {
        ScriptFiles scriptFiles = downloader.download(projectId);
        List<ScriptFile> toUpload = new ArrayList<ScriptFile>();
        if (sourceDir.isDirectory()) {
            for (File file : sourceDir.listFiles()) {
                ScriptFile scriptFile = scriptFileBuilder.build(file);
                scriptFile.setId(findId(scriptFiles, scriptFile.getName(), scriptFile.getFileType()));
                toUpload.add(scriptFile);
            }
        }
        ScriptFiles request = new ScriptFiles();
        request.setFiles(toUpload);
        HttpEntity<ScriptFiles> requestEntity = new HttpEntity<ScriptFiles>(request, headersProvider.provide());
        restTemplateProvider.provide().exchange(uploadPath(projectId), HttpMethod.PUT, requestEntity, String.class);
    }

    private URI uploadPath(String projectId) {
        try {
            return new URI("https://www.googleapis.com/upload/drive/v2/files/" + projectId);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private String findId(ScriptFiles scriptFiles, String name, ScriptFileType type) {
        for (ScriptFile file : scriptFiles.getFiles()) {
            if (file.getName().equals(name) && file.getFileType().equals(type)) {
                return file.getId();
            }
        }
        return "";
    }
}
