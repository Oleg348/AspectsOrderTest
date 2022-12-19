package org.example.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.Ordered;

import static org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils.qualifiedBeanOfType;

@Aspect
public class GlobalLockAspect implements BeanFactoryAware, Ordered {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Around("@annotation(globalLockAnnotation)")
    public Object doWithGlobalLock(ProceedingJoinPoint joinPoint, GlobalLock globalLockAnnotation) throws Throwable {
        var globalLockService = getLockService(globalLockAnnotation.serviceRef());
        try (var ignored = globalLockService.createLock(globalLockAnnotation.lockerName())) {
            return joinPoint.proceed();
        }
    }

    private GlobalLockService getLockService(String beanQualifier) {
        if (GlobalLock.DEFAULT_LOCKER_SERVICE.equals(beanQualifier)) {
            return beanFactory.getBean(GlobalLockService.class);
        }
        else {
            return qualifiedBeanOfType(beanFactory, GlobalLockService.class, beanQualifier);
        }
    }
}
