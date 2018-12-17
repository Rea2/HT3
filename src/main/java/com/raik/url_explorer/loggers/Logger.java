package com.raik.url_explorer.loggers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * Выполняет запись коллекции строк в файл. Путь к файлу задается в просцессе создания экземпляра данного класса.
 * Для реализации новых режимов работы доступен вариант записи по умолчанию.
 * @author Raik Yauheni    14.12.2018.
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

    public void logging (Collection<String> lines) throws IOException {
        Path file = Paths.get(pathLogFile);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

    public String getPathLogFile() {
        return pathLogFile;
    }
}
