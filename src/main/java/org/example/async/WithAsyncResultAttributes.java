package org.example.async;

import lombok.RequiredArgsConstructor;
import org.example.myaop.Attributes;

@RequiredArgsConstructor
public class WithAsyncResultAttributes implements Attributes {

    private final WithAsyncResult annotation;

    @Override
    public int getOrder() {
        return annotation.order();
    }

    public String getServiceRef() {
        return annotation.serviceRef();
    }
}
