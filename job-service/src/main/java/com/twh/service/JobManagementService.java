package com.twh.service;

import com.twh.annotation.Job;
import com.twh.dto.job.AvailableJobDetail;
import com.twh.dto.job.SysJobDetail;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

/**
 * job管理
 */
@Service
@Slf4j
public class JobManagementService implements ApplicationContextAware {
    private static String DEFAULT_GROUP = "default";

    private SchedulerFactoryBean schedulerFactoryBean;
    private Scheduler scheduler;

    // readonly
    private List<org.quartz.Job> sysJobs;

    @Autowired
    public JobManagementService(SchedulerFactoryBean schedulerFactoryBean) {
        scheduler = schedulerFactoryBean.getScheduler();
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.findJob();
    }

    private void findJob() {
        // 从ioc容器查找所有标注了@Job注解的bean
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Job.class);
        List<org.quartz.Job> sysJobs = new ArrayList<>();
        for (Object value : beansWithAnnotation.values()) {
            if (value instanceof org.quartz.Job) {
                sysJobs.add((org.quartz.Job)value);
            }
        }

        this.sysJobs = List.copyOf(sysJobs);
    }

    /**
     * 获取系统所有Job
     * @return
     */
    public List<SysJobDetail> allSysJobDetails() {
        // 从ioc容器查找所有标注了@Job注解的bean
        return sysJobs.stream().map((bean) -> {
            SysJobDetail jobDetail = new SysJobDetail();
            jobDetail.setClassName(bean.getClass().getName());
            Job jobAnnotation = bean.getClass().getAnnotation(Job.class);
            jobDetail.setJobName(jobAnnotation.name());
            jobDetail.setDetail(jobAnnotation.detail());

            return jobDetail;
        }).collect(toList());
    }

    /**
     * 获取quartz job
     * @return
     * @throws SchedulerException
     */
    public List<AvailableJobDetail> allAvailableJobDetails() throws SchedulerException {
        Set<JobKey> jobKeys = scheduler.getJobKeys(groupEquals(DEFAULT_GROUP));
        // TODO
        List<AvailableJobDetail> availableJobDetails = new ArrayList<>(jobKeys.size());
        for (JobKey jobKey : jobKeys) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            AvailableJobDetail availableJobDetail = new AvailableJobDetail();
            availableJobDetail.setJobName(jobKey.getName());
            availableJobDetail.setClassName(jobDetail.getJobClass().getName());
            availableJobDetail.setDetail(jobDetail.getDescription());
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggersOfJob) {
                if (trigger instanceof CronTrigger) {
                    // TODO
                    availableJobDetail.setCron(((CronTrigger)trigger).getCronExpression());
                }
            }
            availableJobDetails.add(availableJobDetail);
        }

        return availableJobDetails;
    }

    public void save(AvailableJobDetail detail) throws SchedulerException {
        JobKey jobKey = new JobKey(detail.getJobName(), DEFAULT_GROUP);
        Trigger trigger = newTrigger()
                .withIdentity(detail.getJobName(), DEFAULT_GROUP)
                .withSchedule(cronSchedule(detail.getCron()))
                .forJob(detail.getJobName(), DEFAULT_GROUP)
                .build();

        if (scheduler.checkExists(jobKey)) {
            // 忽略更新Job，只更新Trigger
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
            if (triggersOfJob.size() > 0) {

                // 之前有多个Trigger，全部取消
                if (triggersOfJob.size() != 1) {
                    for (Trigger oldTrigger : triggersOfJob) {
                        log.debug("unscheduleJob {}", oldTrigger.getKey());
                        scheduler.unscheduleJob(oldTrigger.getKey());
                    }
                    scheduler.scheduleJob(scheduler.getJobDetail(jobKey), trigger);
                } else {
                    // 重新开始调度
                    log.debug("rescheduleJob {} => {}", triggersOfJob.get(0).getKey(), trigger);
                    scheduler.rescheduleJob(triggersOfJob.get(0).getKey(), trigger);
                }
            }
        } else {
            // insert
            Class<? extends org.quartz.Job> jobClass = null;
            for (org.quartz.Job value : sysJobs) {
                if (value.getClass().getName().equals(detail.getClassName())) {
                    jobClass = value.getClass();
                }
            }

            if (jobClass == null) {
                // TODO
            }
            JobDetail job = newJob(jobClass)
                    .withIdentity(detail.getJobName(), DEFAULT_GROUP)
                    .withDescription(detail.getDetail())
                    .requestRecovery()
                    .build();
            log.debug("scheduleJob {} => {}", job, trigger);
            scheduler.scheduleJob(job, trigger);
        }
    }

    public void delete(List<JobKey> jobKeys) throws SchedulerException {
        // 暂停job 删除trigger
        pause(jobKeys);
        scheduler.deleteJobs(jobKeys);
    }

    /**
     * 删除指定Job
     * @param jobKeys
     */
    public void delete(String ...jobKeys) throws SchedulerException {
        List<JobKey> keys = Arrays.stream(jobKeys).map(k -> new JobKey(k, DEFAULT_GROUP)).collect(toList());
        delete(keys);
    }

    /**
     * 删除所有Job
     */
    public void deleteAll() throws SchedulerException {
        Set<JobKey> jobKeys = scheduler.getJobKeys(groupEquals(DEFAULT_GROUP));
        delete(List.copyOf(jobKeys));
    }

    public void pause(List<JobKey> jobKeys) throws SchedulerException {
        for (JobKey jobKey : jobKeys) {
            scheduler.pauseJob(jobKey);
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggersOfJob) {
                // TODO 没找到删除trigger的方法
                scheduler.pauseTrigger(trigger.getKey());
            }
        }
    }

    public void pause(String[] jobKeys) throws SchedulerException {
        List<JobKey> keys = Arrays.stream(jobKeys).map(k -> new JobKey(k, DEFAULT_GROUP)).collect(toList());
        pause(keys);
    }

    public void pauseAll() throws SchedulerException {
        scheduler.pauseAll();
    }

    public void resume(List<JobKey> jobKeys) throws SchedulerException {
        for (JobKey jobKey : jobKeys) {
            scheduler.resumeJob(jobKey);
        }
    }

    public void resume(String[] jobKeys) throws SchedulerException {
        List<JobKey> keys = Arrays.stream(jobKeys).map(k -> new JobKey(k, DEFAULT_GROUP)).collect(toList());
        resume(keys);
    }

    public void resumeAll() throws SchedulerException {
        scheduler.resumeAll();
    }
}
