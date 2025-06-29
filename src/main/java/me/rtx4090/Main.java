package me.rtx4090;

import java.io.*;
import java.util.Scanner;

public class Main {

    private static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("The Shell started. Type 'exit' to quit.");
        while (true) {
            System.out.print(currentDirectory.getAbsolutePath() + " $ ");
            String command = scanner.nextLine();

            if (command.trim().equals("exit")) {
                System.out.println("Exiting the Shell.");
                break;
            }

            // handle "cd" command manually
            if (command.startsWith("cd ")) {
                String path = command.substring(3).trim();
                File newDir = new File(currentDirectory, path);
                try {
                    if (newDir.exists() && newDir.isDirectory()) {
                        currentDirectory = newDir.getCanonicalFile();
                    } else {
                        System.out.println("cd: no such directory: " + path);
                    }
                } catch (IOException e) {
                    System.out.println("cd: error resolving path");
                }
                continue;
            }


            executeCommand(command);
        }
        scanner.close();
    }

    private static void executeCommand(String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
            builder.directory(currentDirectory);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("*Error*: exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
