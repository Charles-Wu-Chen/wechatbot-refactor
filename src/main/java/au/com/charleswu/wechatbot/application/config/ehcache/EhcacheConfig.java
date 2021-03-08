package au.com.charleswu.wechatbot.application.config.ehcache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

@Configuration
@EnableCaching
public class EhcacheConfig {
    @Bean
    public KeyGenerator toStringKeyGenerator() {
        return (Object target, Method method, Object... params) ->
                method.getName() + "_" + Arrays.toString(params);
    }
}
