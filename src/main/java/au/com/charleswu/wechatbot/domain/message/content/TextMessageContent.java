package au.com.charleswu.wechatbot.domain.message.content;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TextMessageContent implements MessageContent {
    String content;

    @Override
    public String getContent() {
        return content;
    }
}
