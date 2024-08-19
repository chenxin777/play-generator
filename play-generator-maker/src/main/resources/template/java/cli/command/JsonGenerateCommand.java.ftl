package ${basePackage}.cli.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * 读取json文件生成代码
 */
@Command(name = "json-generate", description = "读取json文件生成代码", mixinStandardHelpOptions = true)
@Data
public class JsonGenerateCommand implements Runnable {

    @Option(names = {"-f", "--file"}, description = "json 文件路径", interactive = true, arity = "0..1", echo = true)
    private String filePath;

    @Override
    public void run() {
        // 读取json文件
        String jsonStr = FileUtil.readUtf8String(filePath);
        DataModel dataModel = JSONUtil.toBean(jsonStr, DataModel.class);
        try {
            MainGenerator.doGenerator(dataModel);
        } catch (Exception e) {
            System.out.println("代码生成失败" + e);
        }
    }
}
