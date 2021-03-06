package per.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import per.sc.util.IdWorker;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Disc
 * @Author caozheng
 * @Date: 19/7/4 下午1:42
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("per.sc.mapper")
public class TlApplication {

    public  static void main(String[] args) {
        /**
         * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
         * 解决netty冲突后初始化client时还会抛出异常
         * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
         */
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(TlApplication.class, args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
