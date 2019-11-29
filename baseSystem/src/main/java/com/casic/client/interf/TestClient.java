package com.casic.client.interf;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product", fallback = TestClient.TestClientFallback.class)
public interface TestClient {

    @PostMapping("/product/listForOrder")
    List<String> listForOrder(@RequestBody String str);

    @Component
    static class TestClientFallback implements TestClient {

        @Override
        public List<String> listForOrder(String str) {
            return null;
        }

    }
}
