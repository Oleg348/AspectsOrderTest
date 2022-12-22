package org.example;

import org.example.async.AsyncActionResultHolder;
import org.example.db.Repo;
import org.example.lock.GlobalLockService;
import org.example.myaop.ProxyCreatorsPostProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableAspectJAutoProxy
@EnableAsync
@EnableTransactionManagement(order = 3)
public class Config implements InitializingBean {

//    @Bean
//    public LogRuntimeExceptionsAspect logRuntimeExceptionsAspect() {
//        return new LogRuntimeExceptionsAspect();
//    }

    @Bean
    public GlobalLockService globalLockService() {
        return new GlobalLockService();
    }

//    @Bean
//    public GlobalLockAspect globalLockAspect() {
//        return new GlobalLockAspect();
//    }

    @Bean
    public AsyncActionResultHolder asyncActionResultHolder() {
        return new AsyncActionResultHolder();
    }

//    @Bean
//    public AsyncActionResultHolderAspect asyncActionResultHolderAspect() {
//        return new AsyncActionResultHolderAspect();
//    }

    @Bean
    public MyService myService(Repo repo) {
        return new MyService(repo);
    }

    @Bean
    public ProxyCreatorsPostProcessor proxyCreatorsPostProcessor() {
        return new ProxyCreatorsPostProcessor();
    }

    @Autowired
    protected AsyncAnnotationBeanPostProcessor asyncAnnotationBeanPostProcessor;

    @Override
    public void afterPropertiesSet() throws Exception {
//        asyncAnnotationBeanPostProcessor.setBeforeExistingAdvisors(false);
    }
}
