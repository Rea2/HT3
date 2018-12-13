package url_explorer.handlers;

import url_explorer.StopWatch;
import url_explorer.URLWorker;
import url_explorer.instruction.Instruction;
import url_explorer.instruction.TypesInstructions;

import java.net.URLConnection;
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
    private List<String> logMessages;
    private StopWatch stopWatch = new StopWatch();

    public void perform(Instruction instruction) {
        String[] args = instruction.getArguments();
        String regex = null;
        String logMessage = null;
        long elapsedTime = 0L;

        stopWatch.start();
        switch (instruction.getTypeCommand()) {

            case BEGIN:
                break;

            case OPEN:
                urlWorker =  new URLWorker();
                urlConn  =  urlWorker.getConnection(args[0], args[1]);
                htmlDocument = urlWorker.getHtmlPage();

                addMessageToLog(true, TypesInstructions.OPEN, args);
//              нужно дописать негативный сценарий.
                break;

            case CHECK_LINK_PRESENT_BY_HREF:
//              String regex  = "(?:href ?= ?)\".+\"";
                regex =  "href ?= ?\"" + args[0] + "\""; // поиск тега href с содержимым из args[0]
                addMessageToLog(isHtmlPageContainsRegex(regex), TypesInstructions.CHECK_LINK_PRESENT_BY_HREF, args);
                break;

            case CHECK_LINK_PRESENT_BY_NAME:
                regex = "<a.+>.+  " + args[0] + "]</a>";
                addMessageToLog(isHtmlPageContainsRegex(regex), TypesInstructions.CHECK_LINK_PRESENT_BY_NAME, args);
                break;

            case CHECK_PAGE_TITLE:
                regex = "<title>" + args[0] + "</title>";
                addMessageToLog(isHtmlPageContainsRegex(regex), TypesInstructions.CHECK_LINK_PRESENT_BY_NAME, args);
                break;

            case CHECK_PAGE_CONTAINS:
                regex = args[0];
                addMessageToLog(isHtmlPageContainsRegex(regex), TypesInstructions.CHECK_PAGE_CONTAINS, args);

            case ERROR_READING:
                break;
                
            case END:
                break;


            default:
                break;
        }
    }


    public boolean isHtmlPageContainsRegex(String regex) throws IllegalStateException {
            if (htmlDocument ==null) throw new  IllegalStateException("There is not any HTML pages for searching");
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(htmlDocument);
            return  matcher.find();
    }

    public boolean addMessageToLog (boolean isSuccess, TypesInstructions type, String ... args) {
        long elapsedTime = stopWatch.stopAndGetElapsedTime();
        StringBuilder sb = new StringBuilder();

        // add to string "+" for succes result or "!" if it is failed
        sb.append(isSuccess == true ? "+" : "!")
                .append(" [")
                .append(type.getDescription() + " ");

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
        return logMessages.add(sb.toString());
    }


    public String readHtmlDocument() {
        if (urlConn != null) {
            return urlWorker.getHtmlPage();
        } else return null;
    }








}
