package com.powershop.config;


import com.powershop.jop.ItemJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    //job：做什么事
    @Bean
    public MethodInvokingJobDetailFactoryBean 
           			methodInvokingJobDetailFactoryBean(ItemJob itemJob) {
        MethodInvokingJobDetailFactoryBean JobDetailFactoryBean = 
            							new MethodInvokingJobDetailFactoryBean();
        JobDetailFactoryBean.setTargetObject(itemJob);
        JobDetailFactoryBean.setTargetMethod("scanLocalMessage");
        return JobDetailFactoryBean;
    }

    //trigger：什么时候做
    @Bean//trigger（job）
    public CronTriggerFactoryBean cronTriggerFactoryBean(
            				MethodInvokingJobDetailFactoryBean JobDetailFactoryBean) {
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        triggerFactoryBean.setCronExpression("*/2 * * * * ?");
        triggerFactoryBean.setJobDetail(JobDetailFactoryBean.getObject());
        return triggerFactoryBean;
    }

    //scheduled：什么时候做什么事
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
        								CronTriggerFactoryBean triggerFactoryBean) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(triggerFactoryBean.getObject());
        return schedulerFactoryBean;
    }
}