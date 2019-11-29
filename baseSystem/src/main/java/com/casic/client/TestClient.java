package com.casic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "client", fallback = TestClient.TestClientFallback.class)
public interface TestClient {

    @PostMapping("/client/client")
    String hello2(@RequestBody String str);

    @Component
    static class TestClientFallback implements TestClient {

        @Override
        public String hello2(String str) {
            return null;
        }

    }
}
