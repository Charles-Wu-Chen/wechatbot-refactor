package au.com.charleswu.wechatbot.domain.message;

import au.com.charleswu.wechatbot.domain.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper=true)
public class PersonMessage extends Message {
//    private final ChatChannel channel = ChatChannel.PERSONAL;

    private Contact to;
}
