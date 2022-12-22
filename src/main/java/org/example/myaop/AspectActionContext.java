package org.example.myaop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;

import java.util.Objects;

import static org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils.qualifiedBeanOfType;

@Getter
@RequiredArgsConstructor
public class AspectActionContext<T extends Attributes> {

    private final Object[] methodArgs;

    private final T attributes;

    private final BeanFactory beanFactory;

    private final SourceAction sourceAction;

    public <U> U getBean(Class<U> clazz, String beanQualifier, String defaultBeanName) {
        if (Objects.equals(defaultBeanName, beanQualifier)) {
            return beanFactory.getBean(clazz);
        }
        else {
            return qualifiedBeanOfType(beanFactory, clazz, beanQualifier);
        }
    }
}
