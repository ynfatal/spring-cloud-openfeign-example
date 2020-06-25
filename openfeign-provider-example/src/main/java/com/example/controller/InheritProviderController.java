package com.example.controller;

import com.example.feign.IInheritClient;
import com.example.request.CustomRequest;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 OpenFeign 的继承特性
 * @author Fatal
 * @date 2020/6/24 0024 5:45
 */
@RestController
public class InheritProviderController implements IInheritClient {

    @Override
    public String inherit() {
        return "Inherit Characteristic.";
    }

    @Override
    public String multiParametersGet(String string, Integer integer, Long longValue, Double doubleValue) {
        return String.format("string: %s, integer: %s, longValue: %s, doubleValue: %s",
                string, integer, longValue, doubleValue);
    }

    @Override
    public String customGetRequest(CustomRequest customRequest) {
        return String.format("customRequest: %s", customRequest);
    }

    @Override
    public String customPostRequest(CustomRequest customRequest) {
        return String.format("customRequest: %s", customRequest);
    }

    @Override
    public String multiParametersPost(Integer integer, CustomRequest customRequest, Long longValue) {
        return String.format("integer: %s, longValue: %s, customRequest: %s", integer, longValue, customRequest);
    }

}
