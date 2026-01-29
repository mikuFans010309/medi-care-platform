package com.mediacare.user.config;

//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//这个配置会自动生成 /swagger-ui.html 页面，展示所有接口。

@Configuration //告诉 Spring 这是一个配置类，会被自动扫描加载
public class SwaggerConfig {
    /*
    * OpenAPI	是 OpenAPI 3.0 规范的 Java 实体，代表整个 API 文档结构
      Info	    描述 API 的基本信息（标题、版本、描述）
    * */
    @Bean  //定义一个名为 openAPI 的 Bean，供 Spring 管理
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("MediCare 用户服务 API")
                        .version("v1.0")
                        .description("用户注册、登录、信息管理"));
    }
}
