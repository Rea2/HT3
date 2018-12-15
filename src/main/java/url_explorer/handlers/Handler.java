package url_explorer.handlers;

import url_explorer.StopWatch;
import url_explorer.URLWorker;
import url_explorer.exceptions.NotAvailableDocumentException;
import url_explorer.exceptions.WrongInstructionException;
import url_explorer.instruction.Instruction;
import url_explorer.instruction.TypesInstructions;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Raik Yauheni on 12.12.2018.
 */
public class Handler {
    private URLWorker  urlWorker= null;
    private URLConnection urlConn = null;
    private String htmlDocument = null;
    private StopWatch stopWatch = new StopWatch();
    private final String DELIMITER =  "----------------------------------------\n";

    // Variables for generate summary
    private int countFailed = 0;
    private int countPassed = 0;
    List<Long> elapsedTimes = new ArrayList<>();

    public String execute (Instruction instruction) throws WrongInstructionException, NotAvailableDocumentException {
        String[] args = instruction.getArguments();
        String regex = null;
        String logMessage = null;
        long elapsedTime = 0L;
        stopWatch.start();
        switch (instruction.getType()) {

            case BEGIN:
                logMessage =  "Logging for file: " + args[0];
                break;

            case OPEN:
                urlWorker =  new URLWorker();
                boolean isPassed = false;
                try {
                    urlConn  =  urlWorker.getConnection(args[0], args[1]);
                    htmlDocument = urlWorker.getHtmlPage();
                    isPassed = true;
                } catch (IOException e) {
                    isPassed = false;
                } finally {
                    logMessage = createLogMessageForInstruction (isPassed, TypesInstructions.OPEN, args);
                }
                break;

            case CHECK_LINK_PRESENT_BY_HREF:
                regex =  "href ?= ?\"" + args[0] + "\""; // поиск тега href с содержимым из args[0]
                logMessage = createLogMessageForInstruction(isHtmlPageContainsRegex(regex),
                        TypesInstructions.CHECK_LINK_PRESENT_BY_HREF, args);
                break;

            case CHECK_LINK_PRESENT_BY_NAME:
                regex = "<a.+>.+" + args[0] + "<\\/a>";
                logMessage =  createLogMessageForInstruction(isHtmlPageContainsRegex(regex),
                        TypesInstructions.CHECK_LINK_PRESENT_BY_NAME, args);
                break;

            case CHECK_PAGE_TITLE:
                regex = "<title>" + args[0] + "</title>";
                logMessage =  createLogMessageForInstruction(isHtmlPageContainsRegex(regex),
                        TypesInstructions.CHECK_LINK_PRESENT_BY_NAME, args);
                break;

            case CHECK_PAGE_CONTAINS:
                regex = args[0];
                logMessage =  createLogMessageForInstruction(isHtmlPageContainsRegex(regex),
                        TypesInstructions.CHECK_PAGE_CONTAINS, args);
                break;

            case ERROR_READING:
                logMessage =  createLogMessageForInstruction(false,
                        TypesInstructions.ERROR_READING, args);
                break;

            case END:
                logMessage = createLogMessageFromEndInstruction();
                resetHandlerState();
                break;

            default:
                throw new WrongInstructionException("Unsupported instruction");
        }
        System.out.println(logMessage);
        return logMessage;
    }

    public boolean isHtmlPageContainsRegex(String regex) throws NotAvailableDocumentException {
            if (htmlDocument ==null) throw new NotAvailableDocumentException("There is not any HTML pages for searching");
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(htmlDocument);
            return  matcher.find();
    }

    public String createLogMessageForInstruction (boolean isPassed, TypesInstructions type, String ... args) {

        long elapsedTime = stopWatch.stopAndGetElapsedTimeMillis();
        elapsedTimes.add(elapsedTime);
        countInstructions(isPassed);
        String logMessage = null;

        if (type !=  TypesInstructions.ERROR_READING) {
            logMessage = createLogMessageForWorkInstruction(isPassed, type, args);
        } else {
            logMessage = createLogMessageForErrorInstruction(args[0]);
        }
        logMessage += " " + convertMillsToSeconds(elapsedTime);

        return logMessage;

    }
     public String createLogMessageForErrorInstruction(String line){
        StringBuilder sb = new StringBuilder();
        sb.append("- [")
            .append(line.substring(1, line.length()-1))
            .append("]");
        return sb.toString();
    }

    public String createLogMessageForWorkInstruction(boolean isPassed, TypesInstructions type, String ... args) {
        StringBuilder sb = new StringBuilder();

        //Create the first character for log message Add to string "+" for passed test or "!" if it  failed
        sb.append(isPassed == true ? "+" : "!");
        sb.append(" ["+type.getAbbreviation() + " ");

        // add to string arguments
        for (int i = 0; i < args.length; i++) {
            sb.append("\"" + args[i] + "\"");
            if (i < args.length - 1) {
                sb.append(" ");
            } else {
                sb.append("]");
            }
        }
        return sb.toString();
    }

    private String convertMillsToSeconds (long time) {
        return String.format("%d.%03d", time/1000, time % 1000);
    }

    public String createLogMessageFromEndInstruction() {
        StringBuilder sb = new StringBuilder();
        int totalTestsAmount = countFailed + countPassed;
        sb.append(DELIMITER);
        sb.append("Total tests: " + totalTestsAmount + "\n")
                .append("Passed/Failed: " + countPassed + "/" + countFailed + "\n")
                .append("Total time: " + convertMillsToSeconds(getTotalTime())  + "\n")
                .append("Average time: " +  convertMillsToSeconds(getAverageTime())+ "\n")
                .append(DELIMITER);
        return sb.toString();
    }

    public void countInstructions(boolean isPassed) {
        if (isPassed) {
            countPassed++;
        } else {
            countFailed++;
        }
    }

    private long getTotalTime() {
        long sum = 0L;
        for (long lo : elapsedTimes)
            sum = sum + lo;
        return sum;
    }


    private long getAverageTime() {
       return getTotalTime()/ elapsedTimes.size();
    }

    private void resetHandlerState() {
        countFailed = 0;
        countPassed = 0;
        urlWorker= null;
        urlConn = null;
        String htmlDocument = null;
    }

}
