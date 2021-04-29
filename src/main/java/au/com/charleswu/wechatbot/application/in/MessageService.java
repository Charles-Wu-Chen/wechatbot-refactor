package au.com.charleswu.wechatbot.application.in;


import au.com.charleswu.wechatbot.adaptor.in.web.dto.MessageDTO;
import au.com.charleswu.wechatbot.application.out.SendMessagePort;
import au.com.charleswu.wechatbot.domain.Contact;
import au.com.charleswu.wechatbot.domain.bot.dingdong.DingDongBot;
import au.com.charleswu.wechatbot.domain.bot.roomsync.RoomSyncBot;
import au.com.charleswu.wechatbot.domain.message.Message;
import au.com.charleswu.wechatbot.domain.message.MessageType;
import au.com.charleswu.wechatbot.domain.message.PersonMessage;
import au.com.charleswu.wechatbot.domain.message.content.MessageContent;
import au.com.charleswu.wechatbot.domain.message.content.TextMessageContent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class MessageService {


    private final SendMessagePort sendMessagePort;

    public MessageService(SendMessagePort sendMessagePort) {
        this.sendMessagePort = sendMessagePort;
    }


    public void sendMessage (MessageDTO messageDTO) {
        Contact to = new Contact(messageDTO.getToID(), null, null);
        PersonMessage personMessage = PersonMessage.builder()
                .to(to)
                .messageType(MessageType.Text)
                .content(new TextMessageContent(messageDTO.getContent()))
                .build();

        sendMessagePort.sendMessage(personMessage);
    }
}
