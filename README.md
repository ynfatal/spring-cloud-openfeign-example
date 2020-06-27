# spring-cloud-openfeign-example
使用 openfeign 的继承特性，发现了一个问题。
以本 Demo 的接口为例：
`com.example.controller.InheritProviderController.inherit()` 方法上没有继承到 `@GetMapping`，
但是它为什么还能作为接口被访问？其实这是 `SpringMVC` 的功能。
接下来写个 Demo 测试下，随便写个接口，能访问就行。
然后找到这个方法 `org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.detectHandlerMethods`，
打个断点，该方法主要用来指定 `Controller` 的 `HandlerMethod` 并将它们注册，注册使用的是 
`org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.registerHandlerMethod`。
启动项目。。。
方法按照下面的执行顺序：
1. `org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.detectHandlerMethods`
2. `org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping.getMappingForMethod`
3. `org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping.createRequestMappingInfo(java.lang.reflect.AnnotatedElement)`
4. `org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation`
5. `org.springframework.core.annotation.AnnotatedElementUtils.findAnnotations`
6. `org.springframework.core.annotation.MergedAnnotations.from(java.lang.reflect.AnnotatedElement, org.springframework.core.annotation.MergedAnnotations.SearchStrategy, org.springframework.core.annotation.RepeatableContainers)`
7. `org.springframework.core.annotation.TypeMappedAnnotations.from(java.lang.reflect.AnnotatedElement, org.springframework.core.annotation.MergedAnnotations.SearchStrategy, org.springframework.core.annotation.RepeatableContainers, org.springframework.core.annotation.AnnotationFilter)`
在第`5`点这个方法停下来，看看参数，其中 `SearchStrategy` 就是搜索策略。
具体位置：`org.springframework.core.annotation.MergedAnnotations.SearchStrategy`
看看实现这个功能的策略（其它省略掉了）：`SearchStrategy.TYPE_HIERARCHY`
```java
enum SearchStrategy {
    /**
     * Perform a full search of the entire type hierarchy, including
     * superclasses and implemented interfaces. Superclass annotations do
     * not need to be meta-annotated with {@link Inherited @Inherited}.
     * 翻译为：
     * 执行完整搜索整个类层次结构，包括超类和实现的接口。 超注释不需要是元注解为@Inherited 。
     */
    TYPE_HIERARCHY
}
```
使用 `类型层次` 策略，扫描到了 `com.example.controller.InheritProviderController` 实现的父接口
`com.example.feign.IInheritClient` 上的 `Mapping` 注解。
`FeignClient` 接口中修饰参数的注解估计也是通过类似的方式找到的。
在深入的话等以后再研究吧，现在找到原因就行。

整理笔记时请参考[Spring Cloud OpenFeign](https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.2.2.RELEASE/reference/html/#netflix-feign-starter)
