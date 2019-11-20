package com.twh.dto.job;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AvailableJobDetail extends SysJobDetail {
    private String cron;
}
