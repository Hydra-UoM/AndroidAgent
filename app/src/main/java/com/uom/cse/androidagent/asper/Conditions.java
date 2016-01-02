package com.uom.cse.androidagent.asper;


import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Collection of Application
 * conditions that serve as a guideline
 * for execution sequence.
 */

public class Conditions
{
    /**
     * General lock
     */
    private static Lock lock = new ReentrantLock();

    /**
     * Represents wether or not the
     * global event-repository is populated
     */
    public static Condition repositoryIsFilled;

    /**
     * A barrier that must be met by {n} workers
     * to conclude that all are ready to start measurements
     */
    public static CyclicBarrier testCanStart;

    /**
     * Represents wether or not a test is finnished
     */
    public static boolean testIsFinished;

    /**
     * Reset procedure for all conditions.
     */
    public static void reset()
    {
        repositoryIsFilled = lock.newCondition();
        testCanStart = new CyclicBarrier(Settings.WORKERS + 1);
        testIsFinished = false;
    }

}
