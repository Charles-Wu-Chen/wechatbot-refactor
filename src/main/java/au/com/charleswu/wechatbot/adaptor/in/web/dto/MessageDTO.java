package au.com.charleswu.wechatbot.adaptor.in.web.dto;

import au.com.charleswu.wechatbot.domain.message.MessageChannel;
import au.com.charleswu.wechatbot.domain.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private MessageType messageType;
    private MessageChannel messageChannel;
    private String toID;
    private String content;
}
