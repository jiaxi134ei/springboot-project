package com.biaojiaf.springbootproject.scheduled;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class TestQuartz extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("quartz task " + new Date() + "quartz。。。");
    }
}

@Configuration
class QuartzConfig  {
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(TestQuartz.class).withIdentity("testQuartz").storeDurably().build();
    }
    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(10)
                .repeatForever();

        return TriggerBuilder.newTrigger().forJob(jobDetail())
                .withIdentity("testQuartz")
                .withSchedule(simpleScheduleBuilder).build();
    }
}