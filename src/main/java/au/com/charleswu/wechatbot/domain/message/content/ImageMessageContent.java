package au.com.charleswu.wechatbot.domain.message.content;

import io.github.wechaty.filebox.FileBox;
import io.github.wechaty.user.Image;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
public class ImageMessageContent implements MessageContent {

    FileBox content;

    @Override
    public FileBox getContent() {
        return content;
    }
}
