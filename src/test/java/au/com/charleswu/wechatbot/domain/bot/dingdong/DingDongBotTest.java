package au.com.charleswu.wechatbot.domain.bot.dingdong;

import au.com.charleswu.wechatbot.application.out.SendMessagePort;
import au.com.charleswu.wechatbot.domain.Contact;
import au.com.charleswu.wechatbot.domain.Room;
import au.com.charleswu.wechatbot.domain.message.PersonMessage;
import au.com.charleswu.wechatbot.domain.message.RoomMessage;
import au.com.charleswu.wechatbot.domain.message.Message;
import au.com.charleswu.wechatbot.domain.message.MessageType;
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
        Message incomingMessage = RoomMessage.builder()
                .messageType(MessageType.Text)
                .room(new Room("roomId", "topic"))
                .content("DING")
                .build();
        dingDongBot.handleMessage(incomingMessage);


        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(sendMessagePort).sendMessage(messageCaptor.capture());
        assertEquals("Dong", messageCaptor.getValue().getContent());
        Assertions.assertTrue(messageCaptor.getValue() instanceof RoomMessage);
        assertEquals("roomId", ((RoomMessage)messageCaptor.getValue()).getRoom().getRoomId());

    }

    @Test
    void shallReplyDongInPersonMessage() {
        Message incomingMessage = PersonMessage.builder()
                .messageType(MessageType.Text)
                .from(new Contact("contactID"))
                .content("DING")
                .build();
        dingDongBot.handleMessage(incomingMessage);


        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(sendMessagePort).sendMessage(messageCaptor.capture());
        assertEquals("Dong", messageCaptor.getValue().getContent());
        Assertions.assertTrue(messageCaptor.getValue() instanceof PersonMessage);
        assertEquals("contactID", ((PersonMessage)messageCaptor.getValue()).getTo().getId());

    }

    @Test
    void isSupported() {
    }
}
