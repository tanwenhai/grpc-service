package com.twh.web.controller;

import com.twh.dto.job.AvailableJobDetail;
import com.twh.dto.job.SysJobDetail;
import com.twh.service.JobManagementService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController()
@RequestMapping("/job")
public class JobController {
    @Autowired
    private JobManagementService jobManagementService;

    /**
     * 所有job
     * @return
     */
    @GetMapping("/sys")
    public List<SysJobDetail> allSysJob() {
        return jobManagementService.allSysJobDetails();
    }

    @GetMapping("/available")
    public List<AvailableJobDetail> allAvailableJob() throws SchedulerException {
        return jobManagementService.allAvailableJobDetails();
    }

    @PutMapping
    public ResponseEntity save(@RequestBody AvailableJobDetail detail) throws SchedulerException {
        jobManagementService.save(detail);
        return ResponseEntity.ok().body(Collections.singletonMap("msg", "ok"));
    }

    @DeleteMapping("{jobKeys}")
    public ResponseEntity delete(@PathVariable String jobKeys) throws SchedulerException {
        if (Objects.equals(jobKeys, "_all")) {
            jobManagementService.deleteAll();
        } else {
            jobManagementService.delete(jobKeys.split(","));
        }
        return ResponseEntity.ok().body(Collections.singletonMap("msg", "ok"));
    }

    @GetMapping("/pause/{jobKeys}")
    public ResponseEntity pause(@PathVariable String jobKeys) throws SchedulerException {
        if (Objects.equals(jobKeys, "_all")) {
            jobManagementService.pauseAll();
        } else {
            jobManagementService.pause(jobKeys.split(","));
        }
        return ResponseEntity.ok().body(Collections.singletonMap("msg", "ok"));
    }

    @GetMapping("/resume/{jobKeys}")
    public ResponseEntity resume(@PathVariable String jobKeys) throws SchedulerException {
        if (Objects.equals(jobKeys, "_all")) {
            jobManagementService.resumeAll();
        } else {
            jobManagementService.resume(jobKeys.split(","));
        }
        return ResponseEntity.ok().body(Collections.singletonMap("msg", "ok"));
    }
}
