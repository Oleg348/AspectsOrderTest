package org.example.myaop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@Getter
@RequiredArgsConstructor
public class PointCut<T extends Attributes> {

    private final Method method;

    private final T attributes;
}
