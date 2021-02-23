package au.com.charleswu.wechatbot.domain.message;


import au.com.charleswu.wechatbot.domain.Contact;
import au.com.charleswu.wechatbot.domain.message.content.MessageContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private MessageContent content;
    private MessageType messageType;
    //private ChatChannel channel;
    private String messageId = "";
    private Contact from;



}

