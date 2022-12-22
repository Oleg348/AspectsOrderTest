package org.example.myaop;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JDKDynamicProxy<T extends Attributes> implements InvocationHandler, BeanFactoryAware {

    private final Object target;

    private final Map<String, T> pointCuts;

    private final AspectAction<T> aspectAction;

    private BeanFactory beanFactory;

    public JDKDynamicProxy(Object target, List<PointCut<T>> pointCuts, AspectAction<T> aspectAction) {
        this.target = target;
        this.pointCuts = pointCuts.stream()
                .collect(Collectors.toMap(pc -> getFullMethodName(pc.getMethod()), PointCut::getAttributes));
        this.aspectAction = aspectAction;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var interceptInfo = pointCuts.get(getFullMethodName(method));
        if (interceptInfo == null) {
            return method.invoke(target, args);
        }
        var ctx = new AspectActionContext<>(args, interceptInfo, beanFactory,
                () -> method.invoke(target, args));
        return aspectAction.perform(ctx);
    }

    private String getFullMethodName(Method method) {
        return method.getName();
    }
}
