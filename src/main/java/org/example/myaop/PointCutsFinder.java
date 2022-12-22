package org.example.myaop;

import java.util.List;

public interface PointCutsFinder<T extends Attributes> {

    List<PointCut<T>> find(Object target);
}
