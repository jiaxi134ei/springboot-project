package com.biaojiaf.springbootproject.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * spring task 定时任务
 */
@Slf4j
@Component
public class ScheduledService {
    @Scheduled(cron = "0/5 * * * * *")
    public void scheduled() {
        System.out.println("================> 使用 CRON ：" + System.currentTimeMillis());
    }


    @Scheduled(fixedRate = 5000)
    public void scheduled1() {
        System.out.println("=====>>>>>使用fixedRate{} :" + System.currentTimeMillis());
    }

    @Scheduled(fixedDelay = 5000)
    public void scheduled2() {
        System.out.println("=====>>>>>fixedDelay{} :"+ System.currentTimeMillis());
    }
}
