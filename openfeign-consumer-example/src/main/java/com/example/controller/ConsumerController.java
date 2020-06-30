package com.example.controller;

import com.example.config.FeignConfiguration;
import com.example.config.FooConfiguration;
import com.example.feign.IAnotherClient;
import com.example.feign.IInheritClient;
import com.example.feign.ISimpleClient;
import com.example.request.CustomRequest;
import feign.Retryer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @implNote
 * 看下一个神奇的类，称为命名上下文工厂，全类名为：org.springframework.cloud.context.named.NamedContextFactory
 * 找到该类后，鼠标移到该类，ctrl + alt 后点击，看看它的子类，以目前引的依赖来看，它包括以下三个：
 * - org.springframework.cloud.openfeign.FeignContext
 * - org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory
 * - org.springframework.cloud.netflix.ribbon.SpringClientFactory
 * 这三个类都继承了 NamedContextFactory，所以它们都是以同一套规范实现的上下文。不过目前我测出来就感觉优先级有点问题，估计是我没找到正确的姿势。
 * 当然咯，它肯定不止三个子类，只是该项目只依赖了 openfeign，所以只能搜索到三个。
 * @author Fatal
 * @date 2020/6/23 0023 21:48
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ConsumerController {

    private ISimpleClient simpleClient;
    private IAnotherClient anotherClient;
    private IInheritClient inheritClient;
    private FeignContext feignContext;
    private ApplicationContext applicationContext;

    @GetMapping("/hello")
    public String hello() {
        return simpleClient.hello();
    }

    @GetMapping("/another")
    public String another() {
        return anotherClient.another();
    }

    /**
     * @apiNote
     * 1. org.springframework.cloud.openfeign.FeignContext
     * 该 Bean 在 FeignAutoConfiguration 中被创建，参考方法 org.springframework.cloud.openfeign.FeignAutoConfiguration#feignContext()
     * 析：创建 Feign 实例的工厂，它会为每个 FeignClient 创建一个 Spring ApplicationContext（AnnotationConfigApplicationContext），
     * 并从中提取它需要的 Bean 。
     * 每个 FeignClient 绑定一个 AnnotationConfigApplicationContext，用于存储自己的最后获得的配置 Bean。
     * 所有的 FeignClient 和对应的 AnnotationConfigApplicationContext 作为键值对存储在 ConcurrentHashMap 中。
     * AnnotationConfigApplicationContext 是 Spring 容器，但并非我们平常谈的那个，平常那个用来托管的是 AnnotationConfigServletWebServerApplicationContext。
     * AnnotationConfigApplicationContext 调用了方法 org.springframework.context.support.GenericApplicationContext#setParent(org.springframework.context.ApplicationContext)
     * 设置了 AnnotationConfigServletWebServerApplicationContext，并将它的 BeanFactory 初始为 parent 的 BeanFactory，后边内部的 Bean 都是通过 parent 的 BeanFactory 俩创建的。
     * 2. org.springframework.cloud.context.named.NamedContextFactory#getContextNames()
     * 获得当前 FeignContext 中所有的 FeignClient 的名称。（它将作为键从而拿到 FeignClient 的 AnnotationConfigApplicationContext）
     * 3. org.springframework.cloud.context.named.NamedContextFactory#getInstance(java.lang.String, java.lang.Class)
     * 参数一：指定我们想拿的 FeignClient 的 name；参数二：想从容器中拿的 Bean 的 Class 对象。
     * @return
     */
    @GetMapping("/feign_context")
    public FeignContext feignContext() {
        log.info("contextNames = [{}]", feignContext.getContextNames());
        log.info("ISimpleClient's configuration bean for Retryer is [{}]", feignContext.getInstance("ISimpleClient", Retryer.class));
        log.info("ISimpleClient's configuration bean is [{}]", feignContext.getInstance("ISimpleClient", FeignClientsConfiguration.class));
        log.info("IAnotherClient's configuration bean for Retryer is [{}](Customized)", feignContext.getInstance("IAnotherClient", Retryer.class));
        log.info("IAnotherClient's configuration bean is [{}](Customized)", feignContext.getInstance("IAnotherClient", FooConfiguration.class));
        log.info("IInheritClient's configuration bean for Retryer is [{}]", feignContext.getInstance("IInheritClient", Retryer.class));
        // IInheritClient 没有自定义的局部 Feign 配置 FooConfiguration，所以它使用的是全局默认的 Feign 配置，故在 FeignContext 找不到属于他的 FooConfiguration，所以这里结果为 null
        log.info("IInheritClient's FooConfiguration bean is [{}]", feignContext.getInstance("IInheritClient", FooConfiguration.class));
        log.info("IInheritClient's FeignClientsConfiguration bean is [{}]", feignContext.getInstance("IInheritClient", FeignClientsConfiguration.class));
        log.info("IInheritClient's FeignConfiguration bean is [{}]", feignContext.getInstance("IInheritClient", FeignConfiguration.class));
        return feignContext;
    }

    @GetMapping("/inherit")
    public String inherit() {
        return inheritClient.inherit();
    }

    @GetMapping("/multi_parameters")
    public String multiParametersGet() {
        return inheritClient.multiParametersGet("multiParametersGet", 18, 22L, 104.5D);
    }

    @GetMapping("/custom_request")
    public String customGetRequest() {
        return inheritClient.customGetRequest(new CustomRequest("customGetRequest", 22, 120L, 104.5D));
    }

    @PostMapping("/custom_request")
    public String customPostRequest() {
        return inheritClient.customPostRequest(new CustomRequest("customPostRequest", 22, 120L, 104.5D));
    }

    @PostMapping("/multi_parameters")
    public String multiParametersPost() {
        return inheritClient.multiParametersPost(16, new CustomRequest("multiParametersPost", 18, 22L, 104.5D), 33L);
    }

}
