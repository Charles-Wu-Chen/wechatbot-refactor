package au.com.charleswu.wechatbot.domain.bot.dingdong;

import au.com.charleswu.wechatbot.application.out.SendMessagePort;
import au.com.charleswu.wechatbot.domain.Contact;
import au.com.charleswu.wechatbot.domain.Room;
import au.com.charleswu.wechatbot.domain.message.*;
import au.com.charleswu.wechatbot.domain.message.content.MessageContent;
import au.com.charleswu.wechatbot.domain.message.content.TextMessageContent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class DingDongBotTest {

    private SendMessagePort sendMessagePort  = Mockito.mock(SendMessagePort.class);


    private DingDongBot dingDongBot;

    @BeforeEach
    void initUseCase() {
        dingDongBot = new DingDongBot(sendMessagePort);
    }

    @Test
    void shallReplyDongInRoomMessage() {

        MessageContent content = TextMessageContent.builder()
                .content("Ding")
                .build();

        Message incomingMessage = RoomMessage.builder()
                .messageType(MessageType.Text)
                .room(new Room("roomId", "topic"))
                .content(content)
                .build();
        dingDongBot.handleMessage(incomingMessage);


        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(sendMessagePort).sendMessage(messageCaptor.capture());
        assertEquals("Dong", messageCaptor.getValue().getContent().getContent().toString());
        Assertions.assertTrue(messageCaptor.getValue() instanceof RoomMessage);
        assertEquals("roomId", ((RoomMessage)messageCaptor.getValue()).getRoom().getRoomId());

    }

    @Test
    void shallReplyDongInPersonMessage() {

        MessageContent content = TextMessageContent.builder()
                .content("Ding")
                .build();

        Message incomingMessage = PersonMessage.builder()
                .messageType(MessageType.Text)
                .from(new Contact("contactID", "name"))
                .content(content)
                .build();
        dingDongBot.handleMessage(incomingMessage);


        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(sendMessagePort).sendMessage(messageCaptor.capture());
        assertEquals("Dong", messageCaptor.getValue().getContent().getContent().toString());
        Assertions.assertTrue(messageCaptor.getValue() instanceof PersonMessage);
        assertEquals("contactID", ((PersonMessage)messageCaptor.getValue()).getTo().getId());

    }

    @Test
    void isSupported() {
    }
}
