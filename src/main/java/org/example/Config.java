package org.example;

import org.example.db.Repo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.EnableAsync;


@Configuration
@EnableAspectJAutoProxy
@EnableAsync
public class Config implements InitializingBean {

    @Bean
    public LogRuntimeExceptionsAspect logRuntimeExceptionsAspect() {
        return new LogRuntimeExceptionsAspect();
    }

    @Bean
    public MyService myService(Repo repo) {
        return new MyService(repo);
    }

    @Autowired
    protected AsyncAnnotationBeanPostProcessor asyncAnnotationBeanPostProcessor;

    @Override
    public void afterPropertiesSet() throws Exception {
        asyncAnnotationBeanPostProcessor.setBeforeExistingAdvisors(false);
    }
}
