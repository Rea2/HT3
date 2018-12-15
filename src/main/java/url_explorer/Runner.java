package url_explorer;

import url_explorer.exceptions.NotAvailableDocumentException;
import url_explorer.exceptions.WrongInstructionException;
import url_explorer.handlers.Handler;
import url_explorer.instruction.Instruction;
import url_explorer.instruction.InstructionsReader;
import url_explorer.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raik Yauheni on 11.12.2018.
 */
public class Runner {
    private List<String> logMessages = new ArrayList<>();
    private boolean isArgsValidated = false;

    public final static String TAB = "   ";
    public final static String FRAME_FOR_ERROR_MESSAGE =
            "--------------------------------------------------------------------------------------------------";
    public final String ILLEGAL_ARGUMENTS = "Please input an even number of arguments: \n" +
             FRAME_FOR_ERROR_MESSAGE + "\n" +
            "The first argument of pair - path to file with instructions.  It has to has \"TXT\" extension." +
            "The second argument of pair - path to log file. If the log file doesn't exist, it will be created. \n" +
            "If it already exists - it will be rewrite\n" +
             FRAME_FOR_ERROR_MESSAGE + "\n";

    public static void main(String[] args){
        try {
            new Runner().perform(args);
        } catch (IOException | IllegalArgumentException | WrongInstructionException | NotAvailableDocumentException e) {
            System.out.println(Runner.TAB + "Dear user. Error happened while program was running ");
            System.out.println( e.getMessage());
            System.out.println("Please, try again");
        }
    }

    public void perform(String[] args) throws IllegalArgumentException,
            IOException, WrongInstructionException, NotAvailableDocumentException {
        isArgsValidated = false;
        checkArgs(args);
        if(!isArgsValidated) {
            throw new IllegalArgumentException("Illegal arguments. Unknown error.");
        }

        InstructionsReader instructionsReader = new InstructionsReader();
        Logger logger = null;
        for (int i = 0 ; i < args.length; i+=2) {
            Handler handler = new Handler();

            if (instructionsReader.addInstructionsToDequeFromFile(args[i])) {
                Instruction inst = null;
                logger = new Logger(args[i+1]);

                while (instructionsReader.hasNextInstruction()) {
                    inst = instructionsReader.nextInstruction();
                    String message = handler.execute(inst);
                    logMessages.add (message);
                }
                try {
                    logger.logging(logMessages);
                } catch (IOException e) {
                   throw new IOException("Can not to write data into log file. " +
                           "Perhaps, either threare are not permissions for this operation, " +
                           "or file is used by another application ");
                }
            }
        }
    }

    public void checkArgs(String [] paths) throws IllegalArgumentException {
        // Fast checks number of arguments
        if (paths == null) {
            throw new IllegalArgumentException("There were not any arguments.\n " + ILLEGAL_ARGUMENTS);
        }
        if (paths.length < 2) {
            throw new IllegalArgumentException("Too little arguments.\n" + ILLEGAL_ARGUMENTS);
        }
        if ((paths.length % 2) != 0){
            throw new IllegalArgumentException("Wrong number of arguments.\n" + ILLEGAL_ARGUMENTS);
        }
        // Check arguments
        File file = null;
        for (int i = 0 ; i < paths.length; i++) {

            // Check the first arguments of every pair.
            if (i%2 == 0) {
                checkInstructionsPath(paths[i]);
            // Check  the second argument of every pair.
            } else {
                checkLogFilePath(paths[i]);
            }
        }
        isArgsValidated = true;
    }

    public void checkInstructionsPath(String arg) throws IllegalArgumentException {
        Path path = null;

        // Check extension of file with instructions.
        int j = arg.lastIndexOf('.');
        if (j > 0) {
            if (!arg.substring(j+1).equalsIgnoreCase("txt")) {
                throw new IllegalArgumentException (arg + ": Illegal extension of file with instructions.\n" +
                        ILLEGAL_ARGUMENTS);
            }
        }
        // Check if file with instruction exists.
        path = Paths.get(arg);
        if (!Files.isReadable(path)) {
            throw new  IllegalArgumentException (arg + ": File with instructions doesn't exist or write access denied "
                    + ILLEGAL_ARGUMENTS);
        }
    }

    public void checkLogFilePath(String arg) throws IllegalArgumentException, SecurityException {
        File file = null;
        file = new File(arg);
        if (file.isFile()) {
                file.canWrite();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new  IllegalArgumentException (arg + " - wrong path. Can not create log file\n");
            }
        }
    }
}
