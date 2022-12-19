package org.example.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GlobalLockService {

     public AutoCloseable createLock(String lockerName) {
          return new GlobalLockerByName(lockerName);
     }

     @Slf4j
     private static final class GlobalLockerByName implements AutoCloseable {

          private static final Map<String, ReentrantLock> lockers = Collections.synchronizedMap(new HashMap<>());

          private final String lockerName;

          private final Lock lock;


          private GlobalLockerByName(String lockerName) {
               this.lockerName = lockerName;
               lock = lockers.computeIfAbsent(lockerName, key -> new ReentrantLock());
               lock.lock();
               log.info("Locker with name {} is locked", lockerName);
          }

          @Override
          public synchronized void close() {
               log.info(lockerName + " is unlocked");
               lock.unlock();
          }
     }
}
