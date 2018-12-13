package url_explorer;

import java.util.concurrent.TimeUnit;

/**
 * Created by Raik Yauheni on 12.12.2018.
 */
public class StopWatch {
    private long startTimeNanos = 0;

    public void start(){
        startTimeNanos = System.nanoTime();
    }

    public long stopAndGetElapsedTimeMillis(){
        return TimeUnit.NANOSECONDS.toMillis(stopAndGetElapsedTime());
    }

    public long stopAndGetElapsedTime(){
        return (System.nanoTime() - startTimeNanos);
    }
}





