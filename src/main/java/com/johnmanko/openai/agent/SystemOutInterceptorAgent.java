package com.johnmanko.openai.agent;

import java.lang.instrument.Instrumentation;

public class SystemOutInterceptorAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Agent loaded. Intercepting System.out.");
        // Replace System.out with a custom PrintStream
        System.setOut(new InterceptingPrintStream(System.out));
    }
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("Agent loaded. Intercepting System.out!");
        // Replace System.out with a custom PrintStream
        System.setOut(new InterceptingPrintStream(System.out));
    }
}
