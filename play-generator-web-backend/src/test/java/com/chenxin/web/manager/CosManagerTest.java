package com.chenxin.web.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest(properties = "spring.profiles.active=local")
class CosManagerTest {

    @Resource
    private CosManager cosManager;

    @Test
    void delObject() {
        cosManager.delObject("/generator_picture/1820771993056395266/FEkg3IPJ-logo.png");
    }

    @Test
    void delObjects() {
        cosManager.delObjects(Arrays.asList("test/IMG_1637.jpg", "test/JJ20-1.jpeg"));
    }

    @Test
    void delDir() throws Exception {
        cosManager.delDir("/test/");
    }
}