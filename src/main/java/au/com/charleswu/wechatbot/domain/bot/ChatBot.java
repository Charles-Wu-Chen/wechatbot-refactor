package au.com.charleswu.wechatbot.domain.bot;

import au.com.charleswu.wechatbot.domain.message.Message;

public interface ChatBot {
    void handleMessage(Message message);
    String getUsage();
    boolean isSupported(Message message);
}
