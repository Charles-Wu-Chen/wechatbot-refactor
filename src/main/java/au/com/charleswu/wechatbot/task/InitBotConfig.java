package au.com.charleswu.wechatbot.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("!test")
public class InitBotConfig implements CommandLineRunner {

    @Value("${wechat.token}")
    private String wechatyToken;

    @Autowired
    BotThread botThread;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(String... args) throws Exception {
        logger.info("initbot 初始化机器人");
        botThread.setWechatToken(wechatyToken);
        botThread.run();
    }

}
