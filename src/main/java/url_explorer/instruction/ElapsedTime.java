package url_explorer.instruction;

import java.io.IOException;

/**
 * Created by Raik Yauheni on 12.12.2018.
 */
public interface ElapsedTime  {
    Long elapsedTime = null;
    void measureTime() throws IOException;
}
