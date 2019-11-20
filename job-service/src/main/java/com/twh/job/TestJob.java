package com.twh.job;

import com.twh.annotation.Job;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Job(name = "test")
@Slf4j
public class TestJob extends QuartzJobBean {

    private int num;

    @Autowired
    DataSource dataSource;

    private String jobSays;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Hello!  HelloJob is executing. " + jobSays + " " + LocalDateTime.now() + " " + num);
    }

    public void setJobSays(String jobSays) {
        this.jobSays = jobSays;
    }
}
