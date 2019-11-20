package com.twh.annotation;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @see com.twh.confguration.AutowiringSpringBeanJobFactory#createJobInstance(TriggerFiredBundle)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Job {
    String name();

    String detail() default "";

}
