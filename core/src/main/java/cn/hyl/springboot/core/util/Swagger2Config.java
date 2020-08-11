package cn.hyl.springboot.core.util;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Config {

    //默认版本的接口api-docs分组
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //指定要扫描的包的路径
                .apis(RequestHandlerSelectors.basePackage("cn.hyl.springboot.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    //app1.0.0版本对外接口
    @Bean
    public Docket vApp100(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(ApiVersions.API_100)
                .select()
                .apis(input -> {
                    Optional<ApiVersion> apiVersionOptional = input.findAnnotation(ApiVersion.class);
                    if (apiVersionOptional.isPresent()) {
                        ApiVersion apiVersion = apiVersionOptional.get();
                        if (apiVersion != null && Arrays.asList(apiVersion.group()).contains(ApiVersions.API_100)) {
                            return true;
                        }
                    }
                    return false;
                })//controller路径
                .paths(PathSelectors.any())
                .build();
    }

    //构建api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("测试使用Swagger")
                //创建人
                .contact(new Contact("DHEE", "www.baidu.com", "vhukze@qq.com"))
                //版本号
                .version("1.1")
                //描述
                .description("管理系统")
                .build();
    }
}
