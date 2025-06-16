package com.example.xiaomi.config;

import com.example.xiaomi.config.RedisClientConfigProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean("redissonClient")
    public RedissonClient redisson(RedisClientConfigProperties redissonProperties) {
        Config config = new Config();
        // 根据需要可以设定编解码器；https://github.com/redisson/redisson/wiki/4.-%E6%95%B0%E6%8D%AE%E5%BA%8F%E5%88%97%E5%8C%96
        // config.setCodec(new RedisCodec());
        config.useSingleServer()
                .setAddress("redis://" + redissonProperties.getHost() + ":" + redissonProperties.getPort())
//                .setPassword(properties.getPassword())
                .setConnectionPoolSize(redissonProperties.getPoolSize())
                .setConnectionMinimumIdleSize(redissonProperties.getMinIdleSize())
                .setIdleConnectionTimeout(redissonProperties.getIdleTimeout())
                .setConnectTimeout(redissonProperties.getConnectTimeout())
                .setRetryAttempts(redissonProperties.getRetryAttempts())
                .setRetryInterval(redissonProperties.getRetryInterval())
                .setPingConnectionInterval(redissonProperties.getPingInterval())
                .setKeepAlive(redissonProperties.isKeepAlive())
        ;

        return Redisson.create(config);
    }
}
