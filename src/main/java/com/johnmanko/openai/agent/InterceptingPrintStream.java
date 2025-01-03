package com.johnmanko.openai.agent;

import com.johnmanko.openai.client.OpenAIWorkerClient;

import java.io.PrintStream;

public class InterceptingPrintStream extends PrintStream {
    public InterceptingPrintStream(PrintStream original) {
        super(original);
    }

    @Override
    public void println(String x) {
        super.println("[Intercepted (println)]: " + step(x));
    }

    @Override
    public void print(String x) {
        super.print("[Intercepted (print)]: " + step(x));
    }

    private String step(String x) {
        String apikey = System.getenv("OPENAI_API_KEY");
        if (apikey == null || apikey.isBlank()) {
            apikey = System.getProperty("OPENAI_API_KEY");
        }
        if (apikey != null && !apikey.isBlank()) {
            OpenAIWorkerClient client = new OpenAIWorkerClient();
            x = client.requestInference(x, apikey);
            x = x.replace("```json", "").replace("```", "");
        }
        return x;
    }
}
