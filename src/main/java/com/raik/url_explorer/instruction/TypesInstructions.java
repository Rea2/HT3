package com.raik.url_explorer.instruction;


/**
 * @author Raik Yauheni
 */

import com.raik.url_explorer.handlers.Handler;

/**
 * Данный класс соджержит все допустимы в приложении типы инструкций.
 * Тип интсрукции определяет набор дейтсвий, которые должен придеринять обрботчик {@link Handler}
 * при ее получении
 */

public enum TypesInstructions {

    // Working instructions
    OPEN("open"),
    CHECK_LINK_PRESENT_BY_HREF("checkLinkPresentByHref"),
    CHECK_LINK_PRESENT_BY_NAME("checkLinkPresentByName"),
    CHECK_PAGE_TITLE("checkPageTitle"),
    CHECK_PAGE_CONTAINS("checkPageContains"),
    // ... you can add new types of working instructions here

    // Service instructions
    ERROR_READING("error_reading"),
    BEGIN("begin"),
    END("end");
    // ... you can add new types of service instructions here

    private String abbreviation;

    private TypesInstructions(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }


}
