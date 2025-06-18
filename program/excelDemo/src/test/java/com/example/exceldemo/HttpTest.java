package com.example.exceldemo;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HttpTest {
    @Test
    public void httpGet() {
        String url = "http://localhost:9999/id";

        // 创建HttpClient实例
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            int loopCount = 10;

            for (int i = 0; i < loopCount; i++) {
                HttpGet request = new HttpGet(url);

                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        String result = EntityUtils.toString(entity, "UTF-8");
                        System.out.println("Received ID: " + result);
                    } else {
                        System.out.println("Request failed, status code: " + statusCode);
                    }

                    // 可选：每次间隔 1 秒
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.err.println("Error during request: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}