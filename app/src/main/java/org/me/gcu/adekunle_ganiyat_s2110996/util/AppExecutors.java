package org.me.gcu.adekunle_ganiyat_s2110996.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private static AppExecutors instance;
    private final ExecutorService diskIO;

    private AppExecutors(ExecutorService diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance() {
        if (instance == null) {
            synchronized (AppExecutors.class) {
                if (instance == null) {
                    instance = new AppExecutors(Executors.newFixedThreadPool(THREAD_COUNT));
                }
            }
        }
        return instance;
    }

    public ExecutorService diskIO() {
        return diskIO;
    }
}
