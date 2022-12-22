package org.example.myaop;

import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;

@RequiredArgsConstructor
public class FromAnnotationFinder<T extends Attributes, A extends Annotation> implements PointCutsFinder<T> {

    private final Class<A> annotationClass;

    private final Function<A, T> attributesMapper;

    @Override
    public List<PointCut<T>> find(Object target) {
        return Arrays.stream(target.getClass().getDeclaredMethods())
                .map(m -> new AbstractMap.SimpleEntry<>(m, getAnnotation(m, annotationClass)))
                .filter(e -> e.getValue() != null)
                .map(e -> new PointCut<>(e.getKey(), attributesMapper.apply(e.getValue())))
                .collect(Collectors.toList());
    }
}
