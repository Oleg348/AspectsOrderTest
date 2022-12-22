package org.example.myaop;

public interface AspectAction<T extends Attributes> {

    Object perform(AspectActionContext<T> context) throws Throwable;
}
