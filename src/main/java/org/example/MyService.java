package org.example;

import lombok.RequiredArgsConstructor;
import org.example.async.WithAsyncResult;
import org.example.db.MyEntity;
import org.example.db.Repo;
import org.example.lock.GlobalLock;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class MyService implements IMyService {

    private final Repo repo;

    private final AtomicBoolean needToThrow = new AtomicBoolean();

    protected MyService() {
        this(null);
    }

    @Override
//    @Async
    @WithAsyncResult(order = 1, serviceRef = "asyncActionResultHolder")
    @GlobalLock(order = 2, lockerName = "save_locker", serviceRef = "globalLockService")
//    @Transactional
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

    @Override
    @GlobalLock(order = 1, lockerName = "save_locker", serviceRef = "globalLockService")
    @WithAsyncResult(order = 2, serviceRef = "asyncActionResultHolder")
    public void save1(String uid) {
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

    @Override
    @WithAsyncResult(order = 1, serviceRef = "asyncActionResultHolder")
    @GlobalLock(order = 2, lockerName = "save_locker", serviceRef = "globalLockService")
    public void save3(String uid) {
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
