package com.biaojiaf.springbootproject.scheduled;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestScheduledExecutorService {

    public static void main(String[] args) {

        ScheduledExecutorService sec = Executors.newSingleThreadScheduledExecutor();

        TimerTask ts = new TimerTask() {
            @Override
            public void run() {
                System.out.println("task run : " + new Date());
            }
        };

        sec.scheduleAtFixedRate(ts,0,1, TimeUnit.SECONDS);
    }
}
