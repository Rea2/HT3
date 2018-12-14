package url_explorer.logging;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Raik Yauheni on 14.12.2018.
 */
public class Logger {

    public final String DEFAULT_LOG_FILE = "target/logs/log.txt";
    private String pathLogFile;

    public Logger() {
        pathLogFile = DEFAULT_LOG_FILE;
    }

    public Logger(String pathLogFile) {
        this.pathLogFile = pathLogFile;
    }

    public void logging (List<String> lines) throws IOException {
        Path file = Paths.get(pathLogFile);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

    public String getPathLogFile() {
        return pathLogFile;
    }
}
