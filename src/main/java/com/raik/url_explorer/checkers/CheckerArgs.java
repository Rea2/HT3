package com.raik.url_explorer.checkers;

import com.raik.url_explorer.exceptions.CannotCreateLogFileException;
import com.raik.url_explorer.exceptions.WrongArgumentsException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Принимает массив аргументов, определяет режим работы программы и проверяет корректность переданных аргументов для
 * данного режима. Режим работы программы определяется по первому агрументу.
 * На данный момент реализована поддержка только одного режима "d". В этом режиме параметры следует
 * передавать следующим образом:
 * d instruction_file1, log_file1.txt, instruction_file2.txt, log_file2.txt, ... instruction_file_n.txt, log_file_n.txt,
 * где instruction_file - файл с инструкциями, log_file - файл для записи результатов выполнения инструкций.
 * Если хотя бы один файл с инструкциями недоступен или не может быть прочитан, работа программы прекращается.
 * Если путь, указывающий хотя бы на один log файл неправильный,  работа программы прекращается.
 * Внимание! При выполнении логирования в существующий текстовой файл производится перезапись файла
 *
 * @author Raik Yauheni
 */
public class CheckerArgs {

    protected String error_message = "";
    protected String mode = "";
    protected String[] supported_modes = {"d"}; //

    public String getError_message() {
        return error_message;
    }

    public String getMode() {
        return mode;
    }

    public boolean checkArgs(String [] args) throws CannotCreateLogFileException, WrongArgumentsException {
        if ( (args == null) || (args.length ==0) ) {
            error_message += "There are not any arguments.\n ";
            return false;
        }

        // Check the first argument
        if (checkFirstArg(args[0], supported_modes) ) {

            // Check for mode "d". You can add some checks for you modes the same way below
            if (mode.equals(supported_modes[0])) {
                return checkMode_d(args);
            } else {
                error_message+= args[0] + ": Unsupported mode. Please enter proper arguments.";
            }
        } else {
            error_message+= "Wrong number of arguments";
            return false;
        }
        return false;
    }

    public boolean checkFirstArg (String firstArg, String ... supported_modes ) {
        boolean result =  Arrays.stream(supported_modes).anyMatch("d"::equals);
        if (result) {
            mode = firstArg;
            return true;
        } else {
            return false;
        }
    }

    public boolean checkMode_d(String[] args) throws CannotCreateLogFileException, WrongArgumentsException {

        // Fast checks arguments
        if (args.length < 3) {
            error_message+= "Too little arguments.\n";
        }
        if ((args.length % 2) == 0) {
            error_message+= "Wrong number of arguments.\n";
        }
        File file = null;
        int i = 1;
        while (i < args.length) {

            // Check every instruction file
            try {
                if (i % 2 == 1) {
                    if (checkInstructionsPath(args[i], "txt") == false) {
                        return false;
                    }
                } else {
                    if (checkLogFilePath(args[i], "txt") == false) {
                        return false;
                    }
                }
            } catch (RuntimeException e) {
                 String message = (args[i] + ": Wrong path or no permissions for ");
                 message+=  i % 2 == 1 ? "reading" : "writing";
                 throw new WrongArgumentsException(message, e);
            }
            i++;
        }
        return true;
    }

    public boolean checkInstructionsPath(String pathToFile, String extension)  {
        Path path = null;
        try {
            path = Paths.get(pathToFile);
        } catch (InvalidPathException e) {
            error_message+= pathToFile + ": Wrong argument";
            return false;
        }

        // Check if file with instruction exists, and is it readable
        if (Files.isReadable(path)) {

            // Check extension
            return isProperFileExtension(pathToFile, extension);
        } else {
            error_message+= pathToFile + ": Instruction file doesn't exist or write access is denied";
            return  false;
        }
    }


    public boolean checkLogFilePath(String pathToFile, String extension) throws CannotCreateLogFileException  {
        Path path = null;
        try {
            path = Paths.get(pathToFile);
        } catch (InvalidPathException e) {
            error_message+= pathToFile + ": Wrong argument";
            return false;
        }
        if (Files.exists(path)) {
            if (Files.isWritable(path)) {
                return isProperFileExtension(pathToFile, extension);
            }
        } else {
            try {
                Files.createFile(path);
                return true;
            } catch (IOException e) {
                throw new CannotCreateLogFileException( pathToFile + " :Can not create log file." +
                        " Perhaps there is not permission for this operation, or wrong path \n", e);
            }
        }
        return false;
    }

    public boolean isProperFileExtension (String pathToFile, String extension) {
        int j = pathToFile.lastIndexOf('.');
        if (pathToFile.substring(j + 1).equalsIgnoreCase(extension)) {
            return true;
        } else {
            error_message+= pathToFile + ": illegal extension of instruction file ." +
                    " The mode supported extensions: " + extension+  "\"\n";
            return false;
        }
    }
}
