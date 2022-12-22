package org.example.lock;

import org.example.myaop.AspectAction;
import org.example.myaop.AspectActionContext;

public class GlobalLockAction implements AspectAction<GlobalLockAttributes> {

    @Override
    public Object perform(AspectActionContext<GlobalLockAttributes> context) throws Throwable {
        var attributes = context.getAttributes();
        var globalLockService = context.getBean(GlobalLockService.class,
                attributes.getServiceRef(), GlobalLock.DEFAULT_LOCKER_SERVICE);
        try (var ignored = globalLockService.createLock(attributes.getLockerName())) {
            return context.getSourceAction().perform();
        }
    }
}
