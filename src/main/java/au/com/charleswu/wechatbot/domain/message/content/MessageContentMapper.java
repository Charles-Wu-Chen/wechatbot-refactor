package au.com.charleswu.wechatbot.domain.message.content;

import io.github.wechaty.user.Message;

public interface MessageContentMapper {
    MessageContent getContentFrom(Message wechatyMessage);
}
