package au.com.charleswu.wechatbot.application.in;


import au.com.charleswu.wechatbot.domain.bot.dingdong.DingDongBot;
import au.com.charleswu.wechatbot.domain.bot.roomsync.RoomSyncBot;
import au.com.charleswu.wechatbot.domain.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ChatbotService {

    @Autowired
    DingDongBot dingDongBot;

    @Autowired
    RoomSyncBot roomSyncBot;

    @Cacheable(value = "incomingMessageCache", keyGenerator = "toStringKeyGenerator")
    public String handleMessage (Message message) {
        if (dingDongBot.isSupported(message)) {
            dingDongBot.handleMessage(message);
        }

        if (roomSyncBot.isSupported(message)) {
            roomSyncBot.handleMessage(message);
        }
        return "";
    }
}
