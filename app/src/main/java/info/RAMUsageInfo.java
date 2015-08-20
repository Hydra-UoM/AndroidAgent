package info;

/**
 * Created by Nirushan on 7/22/2015.
 */
public class RAMUsageInfo extends UsageInfo {
    public static String VARIABLE_PRIVATE_MEMORY_USAGE = "privateMemoryUsage";
    public static String VARIABLE_SHARED_MEMORY_USAGE = "sharedMemoryUsage";

    private int privateMemoryUsage;
    private int sharedMemoryUsage;


    public int getPrivateMemoryUsage() {
        return privateMemoryUsage;
    }

    public void setPrivateMemoryUsage(int privateMemoryUsage) {
        this.privateMemoryUsage = privateMemoryUsage;
    }

    public int getSharedMemoryUsage() {
        return sharedMemoryUsage;
    }

    public void setSharedMemoryUsage(int sharedMemoryUsage) {
        this.sharedMemoryUsage = sharedMemoryUsage;
    }
}
