package com.example.feign.fallback;

import com.example.feign.IInheritClient;
import com.example.request.CustomRequest;
import org.springframework.stereotype.Component;

/**
 * @author Fatal
 * @date 2020/6/24 0023 5:39
 */
@Component
public class InheritFallback implements IInheritClient {

    @Override
    public String inherit() {
        return "The inherit() method return fallback value";
    }

    @Override
    public String multiParametersGet(String string, Integer integer, Long longValue, Double doubleValue) {
        return "The multiParametersGet(..) method return fallback value";
    }

    @Override
    public String customGetRequest(CustomRequest customRequest) {
        return "The customGetRequest(CustomRequest customRequest) method return fallback value";
    }

    @Override
    public String customPostRequest(CustomRequest customRequest) {
        return "The customPostRequest(CustomRequest customRequest) method return fallback value";
    }

    @Override
    public String multiParametersPost(Integer integer, CustomRequest customRequest, Long longValue) {
        return "The multiParametersPost(..) method return fallback value";
    }
}
