package com.amazon.ask.helloworld;
import java.io.IOException;

import static spark.Spark.*;

public class LocalDebugger {
    public static void main(String[] args) throws InterruptedException, IOException {
        port(8080);
        post("/", (req, res) -> HelloWorldStreamHandler.getSkill());
        boolean isWindows = System.getProperty("os.name")
  .toLowerCase().startsWith("windows");
        String homeDirectory = System.getProperty("user.home");
        Process process;
        if (isWindows) {
            process = Runtime.getRuntime()
            .exec(String.format("cmd.exe /c ngrok http 8080", homeDirectory));
        } else {
            process = Runtime.getRuntime()
            .exec(String.format("sh -c ngrok http 8080", homeDirectory));
        }
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }
}