package ru.danil42russia.agent;

import java.lang.instrument.Instrumentation;

public class GoLandAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        Logger.initLogger();
        Logger.debug("---- Start GoLand Agent ----");

        boolean javassistIsLoaded = javassistIsLoaded();
        if (!javassistIsLoaded) {
            Logger.debug("Javassist is not loaded");
            return;
        }

        inst.addTransformer(new GoSdkUtilClassTransformer());
    }

    private static boolean javassistIsLoaded() {
        try {
            Class.forName("javassist.ClassPool");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
