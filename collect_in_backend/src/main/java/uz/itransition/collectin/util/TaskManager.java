package uz.itransition.collectin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class TaskManager {
    private final static Logger log = LoggerFactory.getLogger(TaskManager.class);

    private Map<Path, ScheduledFuture> futures = new HashMap<>();

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private static final TimeUnit UNITS = TimeUnit.SECONDS;

    public void scheduleForDeletion(Path path, long delay) {
        ScheduledFuture future = executor.schedule(() -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }, delay, UNITS);

//        futures.put(path, future);
    }

    public void onFileAccess(Path path) {
        ScheduledFuture future = futures.get(path);
        if (future != null) {

            boolean result = future.cancel(false);
            if (result) {
                // reschedule the task
                futures.remove(path);
                scheduleForDeletion(path, future.getDelay(UNITS));
            } else {
                // too late, task was already running
            }
        }
    }
}
