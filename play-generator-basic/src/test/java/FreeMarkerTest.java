import cn.hutool.core.date.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/13 17:27
 * @modify
 */
public class FreeMarkerTest {

    @Test
    public void test() throws IOException, TemplateException {
        // 声明配置文件，指定FreeMarker版本号
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        // 指定模版路径
        cfg.setDirectoryForTemplateLoading(new File("src/main/resources/template"));
        // 设置模版文件使用的字符集
        cfg.setDefaultEncoding("UTF-8");
        // 数字格式化
        cfg.setNumberFormat("0.######");
        // 加载指定模版
        Template template = cfg.getTemplate("myweb.html.ftl");

        // 创建数据模型
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("currentYear", DateUtil.year(new Date()));
        List<Map<String, Object>> menuItems = new ArrayList<>();
        Map<String, Object> menuItem1 = new HashMap<>();
        menuItem1.put("url", "https://github.com/chenxin777");
        menuItem1.put("label", "玩物志出品");
        Map<String, Object> menuItem2 = new HashMap<>();
        menuItem2.put("url", "http://codeplay.icu/");
        menuItem2.put("label", "Play OJ");
        menuItems.add(menuItem1);
        menuItems.add(menuItem2);
        dataModel.put("menuItems", menuItems);
        FileWriter out = new FileWriter("myweb.html");
        template.process(dataModel, out);
        out.close();
    }

}
