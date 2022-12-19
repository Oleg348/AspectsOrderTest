package org.example;

import lombok.RequiredArgsConstructor;
import org.example.async.WithAsyncResult;
import org.example.db.MyEntity;
import org.example.db.Repo;
import org.example.lock.GlobalLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class MyService {

    private final Repo repo;

    private final AtomicBoolean needToThrow = new AtomicBoolean();

    @Async
    @WithAsyncResult(serviceRef = "asyncActionResultHolder")
    @GlobalLock(lockerName = "save_locker", serviceRef = "globalLockService")
    @Transactional
    public void save(String uid) {
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
