package url_explorer.exceptions;

/**
 * Created by Raik Yauheni on 15.12.2018.
 */
public class NotAvailableDocumentException extends Exception {

    public NotAvailableDocumentException() {
        super();
    }

    public NotAvailableDocumentException(Throwable cause) {
        super(cause);
    }

    public NotAvailableDocumentException(String message) {
        super(message);
    }

    public NotAvailableDocumentException(String message, Throwable cause) {
        super(message, cause);
    }

}
