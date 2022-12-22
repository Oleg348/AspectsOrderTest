package org.example.async;

import org.example.myaop.AspectAction;
import org.example.myaop.AspectActionContext;

public class WithAsyncResultAction implements AspectAction<WithAsyncResultAttributes> {

    @Override
    public Object perform(AspectActionContext<WithAsyncResultAttributes> context) {
        var uid = (String)context.getMethodArgs()[0];
        var asyncResultHolderService = context.getBean(AsyncActionResultHolder.class,
                context.getAttributes().getServiceRef(), WithAsyncResult.DEFAULT_SERVICE);
        asyncResultHolderService.run(() -> {
            try {
                context.getSourceAction().perform();
            } catch (RuntimeException re) {
                throw re;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }, uid);
        return null;
    }
}
