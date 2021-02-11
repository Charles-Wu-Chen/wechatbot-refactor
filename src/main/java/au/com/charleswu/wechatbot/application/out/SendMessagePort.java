package au.com.charleswu.wechatbot.application.out;

import au.com.charleswu.wechatbot.domain.message.Message;

public interface SendMessagePort {
    public void sendMessage(Message message);
}
