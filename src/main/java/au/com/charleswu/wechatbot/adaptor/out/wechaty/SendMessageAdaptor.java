package au.com.charleswu.wechatbot.adaptor.out.wechaty;

import au.com.charleswu.wechatbot.application.out.SendMessagePort;
import au.com.charleswu.wechatbot.domain.Room;
import au.com.charleswu.wechatbot.domain.message.PersonMessage;
import au.com.charleswu.wechatbot.domain.message.RoomMessage;
import au.com.charleswu.wechatbot.domain.message.Message;
import au.com.charleswu.wechatbot.task.SysConstant;
import io.github.wechaty.Wechaty;
import io.github.wechaty.schemas.RoomQueryFilter;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Image;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static au.com.charleswu.wechatbot.adaptor.util.CommonWechatyUtil.getWechatyRoom;

@Component
public class SendMessageAdaptor implements SendMessagePort {
    @Override
    public void sendMessage(Message message) {
        Wechaty wechaty = SysConstant.wechatyBot;

        if (message instanceof PersonMessage) {
            Contact to = new Contact(wechaty, ((PersonMessage)message).getTo().getId());
            to.say(message.getContent().getContent());

        } else if (message instanceof RoomMessage) {
            RoomMessage roomMessage = (RoomMessage)message;
            io.github.wechaty.user.Room wechatyRoom = getWechatyRoom(wechaty, roomMessage.getRoom());
            wechatyRoom.say(message.getContent().getContent());
        }
    }


}
