package com.chenxin.maker.generator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/19 12:44
 * @modify
 */
public class GitGenerator {

    public static void doGenerate(String targetPath) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("git", "init");
        builder.directory(new File(targetPath));
        Process process = builder.start();
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("Git命令执行结束，退出码：" + exitCode);
        // 生成默认.gitignore文件
        Path path = Paths.get(targetPath);
        Path gitignorePath = path.resolve(".gitignore");
        Files.createFile(gitignorePath);
        Files.write(gitignorePath, ("target" + "\n" + ".source").getBytes(StandardCharsets.UTF_8));
        System.out.println(".gitignore生成结束");
    }
}
