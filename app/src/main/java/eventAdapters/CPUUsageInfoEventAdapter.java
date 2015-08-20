package eventAdapters;

import com.uom.cse.androidagent.Event;
import com.uom.cse.androidagent.Variable;
import com.uom.cse.androidagent.info.CPUUsageInfo;
import com.uom.cse.androidagent.info.RAMUsageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirushan on 8/6/2015.
 */
public class CPUUsageInfoEventAdapter extends Event {
    public static String EVENT_NAME = "CPUUsageInfo";

    public CPUUsageInfoEventAdapter(CPUUsageInfo cpuUsageInfo) {
        super(EVENT_NAME, getVariables(cpuUsageInfo));
    }

    public static Event getSampleEvent(){
        Event sampleEvent;

        CPUUsageInfo info = new CPUUsageInfo();
        info.setProcessType("type");
        info.setPid(0);
        info.setApplicationLabel("label");
        info.setEventType("event");
        info.setPackageName("name");
        info.setCpuUsage(0);

        List<Variable> variables = getVariables(info);

        sampleEvent = new Event(EVENT_NAME, variables);

        return sampleEvent;
    }

    private static List<Variable> getVariables(CPUUsageInfo info){
        List<Variable> variables = new ArrayList<Variable>();

        Variable eventType = new Variable(RAMUsageInfo.VARIABLE_EVENT_TYPE, "CPU");
        variables.add(eventType);

        Variable applicationLabel = new Variable(RAMUsageInfo.VARIABLE_APPLICATION_LABEL, info.getApplicationLabel());
        variables.add(applicationLabel);

        Variable processType = new Variable(RAMUsageInfo.VARIABLE_PROCESS_TYPE, info.getProcessType());
        variables.add(processType);

        Variable packageName = new Variable(RAMUsageInfo.VARIABLE_PACKAGE_NAME, info.getPackageName());
        variables.add(packageName);

        Variable CPUUsage = new Variable(CPUUsageInfo.VARIABLE_CPU_USAGE, info.getCpuUsage());
        variables.add(CPUUsage);

        Variable pid = new Variable(RAMUsageInfo.VARIABLE_PID, info.getPid());
        variables.add(pid);


        return variables;
    }
}
