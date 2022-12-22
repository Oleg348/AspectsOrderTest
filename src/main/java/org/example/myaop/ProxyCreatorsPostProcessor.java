package org.example.myaop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.async.WithAsyncResult;
import org.example.async.WithAsyncResultAction;
import org.example.async.WithAsyncResultAttributes;
import org.example.lock.GlobalLock;
import org.example.lock.GlobalLockAction;
import org.example.lock.GlobalLockAttributes;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ProxyCreatorsPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private final Collection<Aspect<?>> aspects = List.of(
            new Aspect<>(
                    new FromAnnotationFinder<>(GlobalLock.class, GlobalLockAttributes::new),
                    new GlobalLockAction()
            ),
            new Aspect<>(
                    new FromAnnotationFinder<>(WithAsyncResult.class, WithAsyncResultAttributes::new),
                    new WithAsyncResultAction()
            )
    );

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        for (var aspect : aspects) {
            aspect.setBeanFactory(beanFactory);
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        var aspectsByMethod = new HashMap<Method, List<AspectPointCut>>();
        var pointCutsByAspectOrder = new HashMap<AspectOrder, List<PointCut>>();
        for (var aspect : aspects) {
            var aspectPointCuts = aspect.findPointCuts(bean);
            for (var pointCut : aspectPointCuts) {
                var aspectPointCut = new AspectPointCut(aspect, pointCut);
                var method = pointCut.getMethod();
                aspectsByMethod.computeIfAbsent(method, k -> new ArrayList<>())
                        .add(aspectPointCut);
                var aspectOrder = new AspectOrder(aspect, pointCut.getAttributes().getOrder());
                pointCutsByAspectOrder.computeIfAbsent(aspectOrder, k -> new ArrayList<>())
                        .add(pointCut);
            }
        }
        var proxiedObject = bean;
        for (var methodAspectEntry : aspectsByMethod.entrySet()) {
            var apcs = methodAspectEntry.getValue();
            apcs.sort(Comparator.comparing((AspectPointCut apc) -> apc.getPointCut().getAttributes().getOrder()).reversed());
            for (var aspectPointCut : apcs) {
                var aspect = aspectPointCut.getAspect();
                var order = aspectPointCut.getPointCut().getAttributes().getOrder();
                var aspectOrder = new AspectOrder(aspect, order);
                var pointCuts = pointCutsByAspectOrder.remove(aspectOrder);
                if (pointCuts != null) {
                    proxiedObject = aspect.createProxy(proxiedObject, pointCuts);
                }
            }
        }
        return proxiedObject;
    }

    @Getter
    @RequiredArgsConstructor
    private static final class AspectWithOrderedPointCuts {

        private final Aspect relatedAspect;

        private final Map<Integer, List<PointCut>> pointCutsByOrder;
    }

    @Getter
    @RequiredArgsConstructor
    private static final class AspectPointCut {

        private final Aspect aspect;

        private final PointCut pointCut;
    }

    @Getter
    @RequiredArgsConstructor
    private static final class AspectOrder {

        private final Aspect aspect;

        private final int order;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof AspectOrder))
                return false;
            var other = ((AspectOrder)obj);
            return aspect.equals(other.aspect) && order == other.order;
        }

        @Override
        public int hashCode() {
            return aspect.hashCode() ^ order;
        }
    }
}
