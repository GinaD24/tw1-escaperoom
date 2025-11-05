package com.tallerwebi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@EnableWebMvc
@Configuration
@ComponentScan({"com.tallerwebi.presentacion", "com.tallerwebi.dominio", "com.tallerwebi.infraestructura"})
public class SpringWebConfig implements WebMvcConfigurer {

    private static final String UPLOAD_DIR_CONFIG = "C:/uploads/imagenes_perfil/";

    // Spring + Thymeleaf need this
    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();

        // 5 MB = 5 * 1024 * 1024 bytes
        long maxUploadSize = 5 * 1024 * 1024;

        // Establece el tamaño máximo permitido para cualquier archivo subido (en bytes)
        multipartResolver.setMaxUploadSize(maxUploadSize);

        // Establece el tamaño máximo permitido para toda la solicitud multipart/form-data (en bytes)
        multipartResolver.setMaxInMemorySize(1048576); // 1MB

        return multipartResolver;
    }


    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/resources/core/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/resources/core/js/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");

        registry.addResourceHandler("/img/uploads/**")
                .addResourceLocations("file:/" + UPLOAD_DIR_CONFIG);

        // 2. Mapeo GENERAL para el resto de imágenes estáticas (corregido - ¡sin duplicados!)
        registry.addResourceHandler("/img/**").addResourceLocations("/resources/core/img/");
    }

    // https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html
    // Spring + Thymeleaf
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        // SpringResourceTemplateResolver automatically integrates with Spring's own
        // resource resolution infrastructure, which is highly recommended.
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/thymeleaf/");
        templateResolver.setSuffix(".html");
        // HTML is the default value, added here for the sake of clarity.
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    // Spring + Thymeleaf
    @Bean
    public SpringTemplateEngine templateEngine() {
        // SpringTemplateEngine automatically applies SpringStandardDialect and
        // enables Spring's own MessageSource message resolution mechanisms.
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
        // speed up execution in most scenarios, but might be incompatible
        // with specific cases when expressions in one template are reused
        // across different data types, so this flag is "false" by default
        // for safer backwards compatibility.
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
    // Spring + Thymeleaf
    // Configure Thymeleaf View Resolver
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }


    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/**/login",
                        "/**/validar-login",
                        "/**/irARegistro",
                        "/**/registrarme",
                        "/**/compra/confirmacion",
                        "/**/webhook/mercado-pago",
                        "/css/**",
                        "/js/**",
                        "/img/**"
                );
    }

}