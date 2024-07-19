package com.chenxin.maker.generator;

import java.io.*;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/18 11:40
 * @modify
 */
public class JarGenerator {

    public static void doGenerate(String projectDir) throws InterruptedException, IOException {
        // 调用Process类执行Maven打包命令,win用mvn.cmd
        String mavenCommand = "mvn clean package -DskipTests=true";
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("mvn命令执行结束，退出码：" + exitCode);
    }

}
