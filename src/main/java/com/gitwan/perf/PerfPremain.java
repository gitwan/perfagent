package com.gitwan.perf;

import java.lang.instrument.Instrumentation;

public class PerfPremain {
    public static void premain(String agentOps, Instrumentation instrumentation) {
        if (agentOps == null || agentOps.equals("")) {
            System.out.println("Usage: -javaagent:perfagent.jar=com.gitwan.perf.AppTest,main,java.lang.String@java.lang.String");
            return;
        }
        String[] options = agentOps.split(",");
        if (options.length < 2) {
            System.out.println("Usage: -javaagent:perfagent.jar=com.gitwan.perf.AppTest,main,java.lang.String@java.lang.String");
            return;
        }
        String clz = options[0];
        String mtd = options[1];
        String[] mtdType = null;
        if (options.length == 3) {
            String mtdTypes = options[2];
            mtdType = mtdTypes.split("@");
        }

        instrumentation.addTransformer(new PerfTransformer(clz, mtd, mtdType));
    }
}
