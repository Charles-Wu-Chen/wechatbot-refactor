package au.com.charleswu.wechatbot.adaptor.out.wechaty;

import au.com.charleswu.wechatbot.application.out.SendMessagePort;
import au.com.charleswu.wechatbot.domain.message.PersonMessage;
import au.com.charleswu.wechatbot.domain.message.RoomMessage;
import au.com.charleswu.wechatbot.domain.message.Message;
import au.com.charleswu.wechatbot.task.SysConstant;
import io.github.wechaty.Wechaty;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import org.springframework.stereotype.Component;

@Component
public class SendMessageAdaptor implements SendMessagePort {
    @Override
    public void sendMessage(Message message) {
        Wechaty wechaty = SysConstant.wechatyBot;
        if (message instanceof PersonMessage) {
            //TODO might need a mapper for message and contact between wechaty and project entities
            Contact to = new Contact(wechaty, ((PersonMessage)message).getFrom().getId());
            to.say(message.getContent());
        } else if (message instanceof RoomMessage) {
            RoomMessage roomMessage = (RoomMessage)message;
            Room wechatyRoom = wechaty.getRoomManager().load(roomMessage.getRoom().getRoomId());
            wechatyRoom.say(message.getContent());
        }
    }
}
