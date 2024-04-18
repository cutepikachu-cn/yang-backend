package cn.cutepikachu.yangtuyunju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication
public class YnagtuyunjuApplication {

    public static void main(String[] args) {
        SpringApplication.run(YnagtuyunjuApplication.class, args);
    }

}
