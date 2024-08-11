package org.lzm.autoConfiguer;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;


@Configuration //声明配置类
@EnableConfigurationProperties(ServerProperties.class) //使serverProperties生效
public class ServerAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(ServerAutoConfiguration.class);

    //声明一个bean
    @Bean
    @ConditionalOnClass(HttpServer.class)
    public HttpServer httpServer(ServerProperties serverProperties) throws IOException {
        //创建HttpServer对象并启动
        HttpServer server = HttpServer.create(new InetSocketAddress(serverProperties.getPort()), 0);
        server.start();
        logger.info("[httpServer][启动服务器成功，端口为:{}]", serverProperties.getPort());

        //返回
        return server;
    }


}
