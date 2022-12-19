package org.example.async;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AsyncActionResultHolder {

    private final Map<String, AsyncActionResult> results = Collections.synchronizedMap(new HashMap<>());

    public void run(Runnable action, String uid) {
        results.put(uid, AsyncActionResult.IN_PROGRESS);
        log.info("action with uid {} started executing", uid);
        AsyncActionResult actionResult;
        try {
            action.run();
            actionResult = AsyncActionResult.SUCCESS;
        }
        catch (RuntimeException re) {
            actionResult = AsyncActionResult.ERROR;
        }
        results.put(uid, actionResult);
        log.info("result of action with uid {} is {}", uid, actionResult);
    }

    public AsyncActionResult getResult(String uid) {
        var result = results.get(uid);
        if (result == null) {
            throw new IllegalArgumentException("there is no actions with uid: " + uid);
        }
        return result;
    }
}
