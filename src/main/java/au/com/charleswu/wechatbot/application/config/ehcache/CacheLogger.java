package au.com.charleswu.wechatbot.application.config.ehcache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheLogger implements CacheEventListener<Object, Object> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        logger.info("Key: {} | EventType: {} | Old value: {} | New value: {}",
                cacheEvent.getKey(), cacheEvent.getType(), cacheEvent.getOldValue(),
                cacheEvent.getNewValue());
    }

}
