package au.com.charleswu.wechatbot.domain.bot.dingdong;

import au.com.charleswu.wechatbot.application.out.SendMessagePort;
import au.com.charleswu.wechatbot.domain.message.*;
import au.com.charleswu.wechatbot.domain.bot.ChatBot;
import au.com.charleswu.wechatbot.domain.message.content.MessageContent;
import au.com.charleswu.wechatbot.domain.message.content.TextMessageContent;
import org.springframework.stereotype.Component;

@Component
public class DingDongBot implements ChatBot {
    private String keyword = "ding";

    private final SendMessagePort sendMessagePort;

    public DingDongBot(SendMessagePort sendMessagePort) {
        this.sendMessagePort = sendMessagePort;
    }

    @Override
    public void handleMessage(Message incomingMessage) {

        MessageContent content = TextMessageContent.builder()
                .content("Dong")
                .build();

        Message outgoingMessage = new Message();
        if (incomingMessage instanceof RoomMessage) {
            RoomMessage incomingRoomMessage = (RoomMessage) incomingMessage;

            outgoingMessage = RoomMessage.builder()
                    .content(content)
                    .messageType(MessageType.Text)
                    .room(incomingRoomMessage.getRoom())
                    .build();
        } else if (incomingMessage instanceof PersonMessage) {
            PersonMessage incomingPersonMessage = (PersonMessage) incomingMessage;
            outgoingMessage = PersonMessage.builder()
                    .content(content)
                    .messageId(incomingMessage.getMessageId())
                    .messageType(MessageType.Text)
                    .to(incomingPersonMessage.getFrom())
                    .build();

        }
        sendMessagePort.sendMessage(outgoingMessage);

    }

    @Override
    public String getUsage() {
        return "ding<大小写不限>";
    }

    @Override
    public boolean isSupported(Message message) {
        return message.getContent().getContent().toString().toLowerCase().equals(keyword);
    }
}
