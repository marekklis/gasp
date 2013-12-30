package gasp.scriptfile;

import java.util.List;

public class ScriptFiles {

    private List<ScriptFile> files;

    public List<ScriptFile> getFiles() {
        return files;
    }

    public void setFiles(List<ScriptFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "ScriptFiles{" +
                "files=" + files +
                '}';
    }
}
