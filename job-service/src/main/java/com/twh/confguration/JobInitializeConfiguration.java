package com.twh.confguration;

import com.twh.job.TestJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.calendar.HolidayCalendar;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.EverythingMatcher.allJobs;

@Configuration
@Slf4j
public class JobInitializeConfiguration {

    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    public void init() throws SchedulerException {
        Scheduler schedule = schedulerFactoryBean.getScheduler();
//        if (schedule.isStarted()) {
                    JobDetail job = newJob(TestJob.class)
                .withIdentity("myJob", "group1")
                .usingJobData("jobSays", "Hello World!")
                .usingJobData("myFloatValue", 3.141f)
                .requestRecovery()
                .build();
            Trigger trigger = newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
//                .modifiedByCalendar("myHolidays")
                .build();
                if (!schedule.checkExists(job.getKey()) && schedule.checkExists(trigger.getKey())) {
                    schedule.scheduleJob(job, trigger);
                }

            log.debug("start schedule");
//            schedule.start();
//        }

//        sched.start();

//        HolidayCalendar cal = new HolidayCalendar();
//        cal.addExcludedDate(Date.from(Instant.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()))));
//        cal.addExcludedDate(Date.from(Instant.from(LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()))));
//        sched.deleteCalendar("myHolidays");
//        sched.addCalendar("myHolidays", cal, false, false);

        // define the job and tie it to our HelloJob class
//        JobDetail job = newJob(TestJob.class)
//                .withIdentity("myJob", "group1")
//                .usingJobData("jobSays", "Hello World!")
//                .usingJobData("myFloatValue", 3.141f)
//                .requestRecovery()
//                .build();

        // Trigger the job to run now, and then every 40 seconds
//        Trigger trigger = newTrigger()
//                .withIdentity("myTrigger", "group1")
//                .startNow()
//                .withSchedule(simpleSchedule()
//                        .withIntervalInSeconds(10)
//                        .repeatForever())
//                .modifiedByCalendar("myHolidays")
//                .build();

//        sched.deleteJob(job.getKey());
//        if (sched.checkExists(job.getKey()) && sched.checkExists(trigger.getKey())) {
//            sched.scheduleJob(job, trigger);
//        }

//        sched.getListenerManager().addJobListener(new JobListenerSupport() {
//            @Override
//            public String getName() {
//                return "test";
//            }
//
//            @Override
//            protected Logger getLog() {
//                System.out.println("getLog");
//                return super.getLog();
//            }
//
//            @Override
//            public void jobToBeExecuted(JobExecutionContext context) {
//                System.out.println("jobToBeExecuted");
//                super.jobToBeExecuted(context);
//            }
//
//            @Override
//            public void jobExecutionVetoed(JobExecutionContext context) {
//                System.out.println("jobExecutionVetoed");
//                super.jobExecutionVetoed(context);
//            }
//
//            @Override
//            public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
//                System.out.println("jobWasExecuted");
//                super.jobWasExecuted(context, jobException);
//            }
//        }, allJobs());
    }

}
