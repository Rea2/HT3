package com.raik.url_explorer.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author Raik Yauheni
 */
public class StopWatch {

    private long startTimeNanos = -1L;

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





