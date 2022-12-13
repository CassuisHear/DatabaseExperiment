package com.whut.dbexperiment.config;

import com.whut.dbexperiment.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 设置静态资源映射路径
     *
     * @param registry 注册器
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        //所有静态资源将映射到类路径下的 static 目录中
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 拓展MVC的消息转换器
     *
     * @param converters 内置的转换器集合
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        log.info("拓展消息转换器...");

        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        //设置对象转换器，底层使用Jackson将Java对象转为Json
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        //将上面的消息转换器添加到MVC框架的转换器集合中，优先级设置为最高
        converters.add(0, messageConverter);
    }
}
