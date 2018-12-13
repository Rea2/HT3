package url_explorer.instruction;

/**
 * Created by Raik Yauheni on 11.12.2018.
 */
public enum TypesInstructions {

    // Working instructions
    OPEN                       ("open"),
    CHECK_LINK_PRESENT_BY_HREF ("checkLinkPresentByHref"),
    CHECK_LINK_PRESENT_BY_NAME ("checkLinkPresentByName"),
    CHECK_PAGE_TITLE           ("checkPageTitle"),
    CHECK_PAGE_CONTAINS        ("checkPageContains"),
    // ... you can add new types of working instructions here

    // Service instructions
    ERROR_READING              ("error_reading"),
    BEGIN                      ("begin"),
    END                        ("end");
    // ... you can add new types of service instructions here
    private String description;

    private TypesInstructions(String description) {

    }
    public String getDescription() {
        return description;
    }
}
