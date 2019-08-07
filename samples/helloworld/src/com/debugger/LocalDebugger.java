package com.debugger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.amazon.ask.helloworld.HelloWorldStreamHandler;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class LocalDebugger {
    private static final String PORT_NUMBER = "portNumber";
    private static final String TIME_OUT = "timeOut";
    private static int timeOutinMilliseconds = 60000;
    private static int port;

    public static void main(String[] args) throws InterruptedException, IOException, SecurityException, ParseException {
        HelloWorldStreamHandler skillInvoker = new HelloWorldStreamHandler();
        CommandLine cmd = ParseCommandLineArgs(args);
        port = Integer.parseInt(cmd.getOptionValue(PORT_NUMBER));
        if(port<1 || port>65535){
            throw new IllegalArgumentException("port out of range:" + port);
        }
        if (cmd.getOptionValue(TIME_OUT) != null && !cmd.getOptionValue(TIME_OUT).isEmpty()) {
            timeOutinMilliseconds = Integer.parseInt(cmd.getOptionValue(TIME_OUT));
        }

        ServerSocket serverSocket = CreateDebuggerSocket(port, timeOutinMilliseconds);
        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String payload = extractPayload(br);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                skillInvoker.handleRequest(IOUtils.toInputStream(payload.toString(), "UTF-8"), baos, null);
                JSONObject jsonResponse = new JSONObject(baos.toString("UTF-8"));
                out.write("HTTP/1.1 200 OK\r\n" + "Content-Type: application/json;charset=UTF-8\r\n" + "Content-Length: "
                        + jsonResponse.toString().length() + "\r\n\r\n" + jsonResponse.toString());
                out.flush();
                out.close();
            }
        } catch (SocketTimeoutException e) {
            serverSocket.close();
        }
    }

    private static CommandLine ParseCommandLineArgs(String[] args) throws ParseException {
        Options options = new Options();

        Option portNumber = new Option("p", PORT_NUMBER, true, "port number for local debuging session");
        portNumber.setRequired(true);
        options.addOption(portNumber);

        Option debugTimeout = new Option("t", TIME_OUT, true, "time in milliseconds for which the socket connection is allowed to be inactive");
        debugTimeout.setRequired(false);
        options.addOption(debugTimeout);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(Thread.currentThread().getStackTrace()[1].getClassName(), options);
            throw e;
        }
        return cmd;
    }

    private static ServerSocket CreateDebuggerSocket(int portNumber, int socketTimeOut) throws IOException, SocketException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        serverSocket.setReuseAddress(true);
        serverSocket.setSoTimeout(socketTimeOut);
        return serverSocket;
    }

    private static String extractPayload(BufferedReader br) throws IOException {
        // code to parse through header data
        while ((br.readLine()).length() != 0) {
        }

        // code to read the post payload data
        StringBuilder payload = new StringBuilder();
        while (br.ready()) {
            payload.append((char) br.read());
        }
        return payload.toString();
    }
}