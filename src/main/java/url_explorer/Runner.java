package url_explorer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Raik Yauheni on 11.12.2018.
 */
public class Runner {

    private String[] args = null;
    private boolean isArgsValidated = false;
    public final String FRAME_FOR_ERROR_MESSAGE =
            "--------------------------------------------------------------------------------------------------";
    public final String ILLEGAL_ARGUMENTS = "Please input an even number of arguments: \n" +
             FRAME_FOR_ERROR_MESSAGE + "\n" +
            "The first argument of pair - path to file with instructions.  It has to has \"TXT\" extension." +
            "The second argument of pair - path to log file. If the log file doesn't exist, it will be created. \n" +
            "If it already exists - it will be rewrite\n" +
             FRAME_FOR_ERROR_MESSAGE + "\n";

    public static void main(String[] args) throws IllegalArgumentException {
        new Runner().perform(args);
    }

    public void perform(String[] args) throws IllegalArgumentException {
        isArgsValidated = false;
        // Checking  arguments
        checkArgs(args);

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

            // Check the first arguments of every pair
            if (i%2 == 0) {
                checkFirstArg(paths[i]);
            // Check  the second argument of every pair
            } else {
                checkSecondArg(paths[i]);
            }
        }
        isArgsValidated = true;
    }

    //
    public void checkFirstArg(String arg) throws IllegalArgumentException {
        File file = null;

        // Check existension of  file with instruction
        int j = arg.lastIndexOf('.');
        if (j > 0) {
            if (!arg.substring(j+1).equalsIgnoreCase("txt")) {
                throw new IllegalArgumentException (arg + ": Illegal extension of file with instructions.\n" +
                        ILLEGAL_ARGUMENTS);
            }
        }
        // Check if file with instruction exist
        file = new File(arg);
        if (!file.isFile()) {
            throw new  IllegalArgumentException (arg + ": File with instructions doesn't exist"
                    + ILLEGAL_ARGUMENTS);
        }
    }

    public void checkSecondArg(String arg) throws IllegalArgumentException {
        File file = null;
        checkSecondArg(arg);
        file = new File(arg);
        if (!file.isFile()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                throw new  IllegalArgumentException (arg + "Wrong path. Can't create log file"
                        + ILLEGAL_ARGUMENTS);
            }
        }
    }



}
