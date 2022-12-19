package org.example.async;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.Ordered;

import static org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils.qualifiedBeanOfType;

@Aspect
public class AsyncActionResultHolderAspect implements BeanFactoryAware, Ordered {

    private BeanFactory beanFactory;

    //execution(* void (String)) &&
    @Around("args(String) &&@annotation(withAsyncResult)")
    public void runAndSaveResult(ProceedingJoinPoint proceedingJoinPoint, WithAsyncResult withAsyncResult) {
        var uid = (String)proceedingJoinPoint.getArgs()[0];
        var asyncResultHolderService = getAsyncResultHolderService(withAsyncResult.serviceRef());
        asyncResultHolderService.run(() -> {
            try {
                proceedingJoinPoint.proceed();
            }
            catch (RuntimeException re) {
                throw re;
            }
            catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }, uid);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private AsyncActionResultHolder getAsyncResultHolderService(String beanQualifier) {
        if (WithAsyncResult.DEFAULT_SERVICE.equals(beanQualifier)) {
            return beanFactory.getBean(AsyncActionResultHolder.class);
        }
        else {
            return qualifiedBeanOfType(beanFactory, AsyncActionResultHolder.class, beanQualifier);
        }
    }
}
