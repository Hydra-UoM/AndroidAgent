package eventAdapters;

import com.uom.cse.androidagent.Event;
import com.uom.cse.androidagent.Variable;
import com.uom.cse.androidagent.info.RAMUsageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirushan on 8/6/2015.
 */
public class RAMUsageInfoEventAdapter extends Event {
    public static String EVENT_NAME = "RamUsageInfo";

    public RAMUsageInfoEventAdapter(RAMUsageInfo ramUsageInfo) {
        super(EVENT_NAME, getVariables(ramUsageInfo));
    }


    public static Event getSampleEvent(){
        Event sampleEvent;

        RAMUsageInfo info = new RAMUsageInfo();
        info.setProcessType("type");
        info.setPid(0);
        info.setPrivateMemoryUsage(0);
        info.setSharedMemoryUsage(0);
        info.setApplicationLabel("label");
        info.setEventType("event");
        info.setPackageName("name");

        List<Variable> variables = getVariables(info);

        sampleEvent = new Event(EVENT_NAME, variables);

        return sampleEvent;
    }

    private static List<Variable> getVariables(RAMUsageInfo info){
        List<Variable> variables = new ArrayList<Variable>();

        Variable eventType = new Variable(RAMUsageInfo.VARIABLE_EVENT_TYPE, "RAM");
        variables.add(eventType);

        Variable applicationLabel = new Variable(RAMUsageInfo.VARIABLE_APPLICATION_LABEL, info.getApplicationLabel());
        variables.add(applicationLabel);

        Variable processType = new Variable(RAMUsageInfo.VARIABLE_PROCESS_TYPE, info.getProcessType());
        variables.add(processType);

        Variable packageName = new Variable(RAMUsageInfo.VARIABLE_PACKAGE_NAME, info.getPackageName());
        variables.add(packageName);

        Variable privateMemoryUsage = new Variable(RAMUsageInfo.VARIABLE_PRIVATE_MEMORY_USAGE, info.getPrivateMemoryUsage());
        variables.add(privateMemoryUsage);

        Variable sharedMemoryUsage = new Variable(RAMUsageInfo.VARIABLE_SHARED_MEMORY_USAGE, info.getSharedMemoryUsage());
        variables.add(sharedMemoryUsage);

        Variable pid = new Variable(RAMUsageInfo.VARIABLE_PID, info.getPid());
        variables.add(pid);


        return variables;
    }
}
