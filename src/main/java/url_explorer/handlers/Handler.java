package url_explorer.handlers;

import url_explorer.StopWatch;
import url_explorer.URLWorker;
import url_explorer.instruction.Instruction;
import url_explorer.instruction.TypesInstructions;

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
    private StopWatch stopWatch;
    private int countFailed = 0;
    private int countPassed = 0;
    List<Long> elapsedTimes = new ArrayList<>();


    public String execute (Instruction instruction) {
        String[] args = instruction.getArguments();
        String regex = null;
        String logMessage = null;
        long elapsedTime = 0L;
        stopWatch.start();

        switch (instruction.getTypeCommand()) {
            case BEGIN:
                logMessage =  "Logging for file:" + args[0] + "\n";
                break;

            case OPEN:
                urlWorker =  new URLWorker();
                urlConn  =  urlWorker.getConnection(args[0], args[1]);
                htmlDocument = urlWorker.getHtmlPage();

                logMessage =  addMessageToLog(true, TypesInstructions.OPEN, args);
//              нужно дописать негативный сценарий.
                break;

            case CHECK_LINK_PRESENT_BY_HREF:
//              String regex  = "(?:href ?= ?)\".+\"";
                regex =  "href ?= ?\"" + args[0] + "\""; // поиск тега href с содержимым из args[0]
                logMessage =  addMessageToLog(isHtmlPageContainsRegex(regex), TypesInstructions.CHECK_LINK_PRESENT_BY_HREF, args);
                break;

            case CHECK_LINK_PRESENT_BY_NAME:
                regex = "<a.+>.+  " + args[0] + "]</a>";
                logMessage =  addMessageToLog(isHtmlPageContainsRegex(regex), TypesInstructions.CHECK_LINK_PRESENT_BY_NAME, args);
                break;

            case CHECK_PAGE_TITLE:
                regex = "<title>" + args[0] + "</title>";
                logMessage =  addMessageToLog(isHtmlPageContainsRegex(regex), TypesInstructions.CHECK_LINK_PRESENT_BY_NAME, args);
                break;

            case CHECK_PAGE_CONTAINS:
                regex = args[0];
                logMessage =  addMessageToLog(isHtmlPageContainsRegex(regex), TypesInstructions.CHECK_PAGE_CONTAINS, args);
                break;

            case ERROR_READING:
                logMessage =  addMessageToLog(false, TypesInstructions.ERROR_READING, args);
                break;

            case END:
                logMessage = getEndStrings();
                reserHandlerState();
                break;
            default:
//   бросить исключение;
                throw new IllegalStateException("Unsupported instruction");

        }
        return logMessage;
    }

    public boolean isHtmlPageContainsRegex(String regex) throws IllegalStateException {
            if (htmlDocument ==null) throw new  IllegalStateException("There is not any HTML pages for searching");
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(htmlDocument);
            return  matcher.find();
    }

    public String addMessageToLog (boolean isPassed, TypesInstructions type, String ... args) {
        long elapsedTime = stopWatch.stopAndGetElapsedTime();

        StringBuilder sb = new StringBuilder();

        // Create the first character for log message
        if (type == TypesInstructions.ERROR_READING) {
            sb.append("-");
        } else {
            // add to string "+" for succes result or "!" if it is failed
            sb.append(isPassed == true ? "+" : "!");
        }
            sb.append(" ["+type.getDescription() + " ");

        // add to string arguments
        for (int i = 0; i < args.length; i++) {
            sb.append("\"" + args[i] + "\"");
            if (i < args.length - 1) {
                sb.append(" ");
            } else {
                sb.append("]");
            }
        }
        sb.append(" " + elapsedTime + "\n");
        countInstruction(isPassed);

        return sb.toString();
    }

    public void countInstruction(boolean isPassed) {
        if (isPassed) {
            countPassed++;
        } else {
            countFailed++;
        }
    }

    public String getEndStrings() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total tests: " + countFailed+countPassed + "\n")
                .append("Passed/Failed: " + countPassed + "/" + countFailed + "\n")
                .append("Total time: " + getTotalTime()  + "\n")
                .append("Average time: " + getAverageTime()  + "\n");

        return sb.toString();
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

    private void reserHandlerState() {
        countFailed = 0;
        countPassed = 0;
        urlWorker= null;
        urlConn = null;
        String htmlDocument = null;
    }









    public String readHtmlDocument() {
        if (urlConn != null) {
            return urlWorker.getHtmlPage();
        } else return null;
    }








}
