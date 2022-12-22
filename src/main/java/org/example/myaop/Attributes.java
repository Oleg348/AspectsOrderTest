package org.example.myaop;

import org.jetbrains.annotations.NotNull;

public interface Attributes extends Comparable<Attributes> {

    int getOrder();

    @Override
    default int compareTo(@NotNull Attributes o) {
        return Integer.compare(getOrder(), o.getOrder());
    }
}
