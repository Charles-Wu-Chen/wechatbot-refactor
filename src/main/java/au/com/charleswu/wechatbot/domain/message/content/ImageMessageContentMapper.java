package au.com.charleswu.wechatbot.domain.message.content;

import io.github.wechaty.user.Image;
import io.github.wechaty.user.Message;
import org.springframework.stereotype.Component;

@Component
public class ImageMessageContentMapper implements MessageContentMapper {

    @Override
    public MessageContent getContentFrom(Message wechatyMessage) {
        return ImageMessageContent.builder()
                .content(wechatyMessage.toImage().artwork())
                .build();
    }
}
