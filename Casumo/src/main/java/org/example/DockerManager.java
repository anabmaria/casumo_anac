package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DockerManager {

    public String runDocker(String command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        System.out.println();
        builder.command(command.split(" "));
        try {
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String containerId = reader.readLine().trim();
            int exitCode = process.waitFor();
            System.out.println("Exited with error code : " + exitCode);
            return containerId;

        } catch (IOException e) {
            e.printStackTrace();
            return ("error encountered");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopDOcker(String command) throws IOException {
        runCommand(command);
    }

    public void removeDOcker(String command) throws IOException {
        runCommand(command);
    }
    public ArrayList<String> runCommand(String command) throws IOException {
            ArrayList<String> output= new ArrayList<String>();
            try {
                Process p = Runtime.getRuntime().exec(command);
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;

                while ((line = in.readLine()) != null) {
                    output.add(line);
                        }
                return output;
            } catch (Exception e) {
                e.printStackTrace();
                return output;
            }
    }

    }


