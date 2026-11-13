package com.vt.cms.config;

import lombok.Data;

@Data
public class JobConfig {
    private Long id;
    private String jobName;
    private Integer delayValue;
    private String delayUnit;
    private Boolean enabled;
}
