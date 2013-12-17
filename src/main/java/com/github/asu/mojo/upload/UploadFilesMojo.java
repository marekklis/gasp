package com.github.asu.mojo.upload;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "upload")
public class UploadFilesMojo extends AbstractMojo {

    @Parameter(property = "scriptsDir", required = true)
    private File scriptsDir;

    public void execute() throws MojoExecutionException {
        File f = scriptsDir;

        if (f.exists()) {
            getLog().info("------- files to upload ------");
            for (File file : f.listFiles()) {
                if (file.isFile()) {
                    getLog().info("file: " + file.getName());
                } else if (file.isDirectory()) {
                    getLog().info("dir : " + file.getName());
                }
            }
            getLog().info("--------------------------");
        } else {
            getLog().error("script files folder not exists!!");
        }
    }
}
