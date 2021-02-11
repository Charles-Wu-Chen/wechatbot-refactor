package au.com.charleswu.wechatbot.domain.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String content = "";
    private MessageType messageType;
    //private ChatChannel channel;
    private String messageId = "";


}

