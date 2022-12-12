package org.example;

import lombok.RequiredArgsConstructor;
import org.example.db.MyEntity;
import org.example.db.Repo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class MyService {

    private final Repo repo;

    private final AtomicBoolean needToThrow = new AtomicBoolean();

    @Async
    @Transactional
    public void save() {
        if (needToThrow.get()) {
            needToThrow.set(false);
            throw new RuntimeException("error");
        }
        needToThrow.set(true);
        var e1 = new MyEntity();
        var e2 = new MyEntity();
        repo.save(e1);
        repo.save(e2);
    }
}
