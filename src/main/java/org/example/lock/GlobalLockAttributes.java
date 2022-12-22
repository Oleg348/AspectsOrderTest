package org.example.lock;

import lombok.RequiredArgsConstructor;
import org.example.myaop.Attributes;

@RequiredArgsConstructor
public class GlobalLockAttributes implements Attributes {

    private final GlobalLock annotation;

    @Override
    public int getOrder() {
        return annotation.order();
    }

    public String getLockerName() {
        return annotation.lockerName();
    }

    public String getServiceRef() {
        return annotation.serviceRef();
    }
}
