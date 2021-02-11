package au.com.charleswu.wechatbot.application.service;


import au.com.charleswu.wechatbot.domain.bot.dingdong.DingDongBot;
import au.com.charleswu.wechatbot.domain.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatbotService {

    @Autowired
    DingDongBot dingDongBot;

    public void handleMessage (Message message) {
        if (dingDongBot.isSupported(message)) {
            dingDongBot.handleMessage(message);
        }

    }
}
