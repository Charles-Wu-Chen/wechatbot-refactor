package au.com.charleswu.wechatbot.domain.message.content;

import io.github.wechaty.user.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class TextMessageContentMapper implements MessageContentMapper {

    @Override
    public MessageContent getContentFrom(Message wechatyMessage) {
        return TextMessageContent.builder()
                .content(wechatyMessage.content())
                .build();
    }
}
