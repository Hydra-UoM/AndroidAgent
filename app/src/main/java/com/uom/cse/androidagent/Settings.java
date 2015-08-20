package com.uom.cse.androidagent;

import android.os.Environment;

public class Settings
{
    // Log / application identifier tag.
    public static final String TAG = "A.Benchmark";
    public static final boolean TRACE = false;

    public static int RUNS = 3;

    public static int REST = 100 * 1000;

    // Data / event population
    public static int EVENTS = 100 * 1000;

    // Warm-up procedure (ms)
    public static int WARMUP = 60 * 1000;

    // Determines the actual test runtime (ms), isolated from any warm-up
    public static int RUNTIME = 60 * 1000;

    // No. Of worker threads
    public static int WORKERS = 1;

    // Random seed
    public static final long SEED = 123;

    // Denotes whether or not the data-items should be randomly shuffled
    public static boolean SHUFFLE = true;

    // Determines how many records we should pre-populate in the database.
    public static int DATABASE_POPULATION = 10000;

    // Controls the engine LRU cache
    public static int DATABASE_CACHE = 1000;

    // Defines monitor cycle granularity (ms)
    public static final int MONITORGRANUALITY = 1000;

    // I/O operations. Files / Paths
    public static class IO
    {
        public static final String DIRECTORY = Environment.getExternalStorageDirectory() + "/Asper-Benchmark";
        public static final String TASKS = DIRECTORY + "/Tasks";
        public static final String MEASUREMENTS = DIRECTORY + "/Measurements";
    }
}