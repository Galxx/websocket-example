package com.websocket.config;

import com.websocket.domain.Product;
import com.websocket.dto.ProductDto;
import com.websocket.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ProductMapper mapper = ProductMapper.MAPPER;

    private static Product product;

    {
        product = new Product(1L,"Tea");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Autowired
    private SimpMessagingTemplate template;

    @Scheduled(fixedRate = 5000L)
    private void sendItem() {
        ProductDto dto = mapper.fromProduct(product);
        template.convertAndSend("/topic", dto);
    }

}
