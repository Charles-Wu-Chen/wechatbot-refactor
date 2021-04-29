package au.com.charleswu.wechatbot.domain.message.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TextMessageContent implements MessageContent {
    String content;

    @Override
    public String getContent() {
        return content;
    }
}
