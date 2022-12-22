package org.example.myaop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Proxy;
import java.util.List;

@RequiredArgsConstructor
public class Aspect<T extends Attributes> implements BeanFactoryAware {

    private final PointCutsFinder<T> pointCutsFinder;

    private final AspectAction<T> aspectAction;

    private BeanFactory beanFactory;

    public List<PointCut<T>> findPointCuts(Object target) {
        return pointCutsFinder.find(target);
    }

    public Object createProxy(Object target, List<PointCut<T>> pointCuts) {
        var methodInterceptor = new JDKDynamicProxy<>(target, pointCuts, aspectAction);
        methodInterceptor.setBeanFactory(beanFactory);
//        methodInterceptor.setBeanFactory(beanFactory);
//        var enhancer = new Enhancer();
//        enhancer.setSuperclass(target.getClass());
//        enhancer.setCallback(methodInterceptor);
//        return enhancer.create();
        return Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), target.getClass().getInterfaces(), methodInterceptor);
//        enhancer.create(target.getClass(), methodInterceptor);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
