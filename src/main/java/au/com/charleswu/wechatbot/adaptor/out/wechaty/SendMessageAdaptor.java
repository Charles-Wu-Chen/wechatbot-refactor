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

@Component
public class SendMessageAdaptor implements SendMessagePort {
    @Override
    public void sendMessage(Message message) {
        Wechaty wechaty = SysConstant.wechatyBot;
        if (message instanceof PersonMessage) {
            //TODO might need a mapper for message and contact between wechaty and project entities
            Contact to = new Contact(wechaty, ((PersonMessage)message).getTo().getId());
            to.say(message.getContent());
            io.github.wechaty.user.Message wechatMessage =
                    new io.github.wechaty.user.Message(wechaty, message.getMessageId());
            Image image = wechatMessage.toImage();
            to.say(image.artwork());
            //TODO domain.image extends wechaty.image
        } else if (message instanceof RoomMessage) {
            RoomMessage roomMessage = (RoomMessage)message;
            io.github.wechaty.user.Room wechatyRoom = getWechatyRoom(wechaty, roomMessage.getRoom());
            wechatyRoom.say(message.getContent());
        }
    }

    private io.github.wechaty.user.Room getWechatyRoom(Wechaty wechaty, Room room) {
        if (room.getRoomId() != null &&  !room.getRoomId().isEmpty()) {
            return wechaty.getRoomManager().load(room.getRoomId());
        } else if (room.getTopic() != null &&  !room.getTopic().isEmpty()) {
            return getRoomByTopic(wechaty, room.getTopic());
        }
        return null;
    }

    private static io.github.wechaty.user.Room getRoomByTopic(Wechaty wechaty, String topic) {

        RoomQueryFilter roomQueryFilter = new RoomQueryFilter();
        roomQueryFilter.setTopic(topic);

        return wechaty.getRoomManager().find(roomQueryFilter);
    }

}
