package au.com.charleswu.wechatbot.adaptor.util;

import io.github.wechaty.Wechaty;
import io.github.wechaty.schemas.RoomQueryFilter;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonWechatyUtil {

    private static Logger logger = LoggerFactory.getLogger(CommonWechatyUtil.class);
    public static String getTopicByRoom(Room room) {
        if (room == null) return "";

        try {
            return room.getTopic().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logger.error(room.getId());
            e.printStackTrace();
        }
        return "";
    }


    public static io.github.wechaty.user.Room getWechatyRoom(Wechaty wechaty, au.com.charleswu.wechatbot.domain.Room room) {
        if (room.getRoomId() != null &&  !room.getRoomId().isEmpty()) {
            return wechaty.getRoomManager().load(room.getRoomId());
        } else if (room.getTopic() != null &&  !room.getTopic().isEmpty()) {
            return getRoomByTopic(wechaty, room.getTopic());
        }
        return null;
    }

    public static io.github.wechaty.user.Room getRoomByTopic(Wechaty wechaty, String topic) {

        RoomQueryFilter roomQueryFilter = new RoomQueryFilter();
        roomQueryFilter.setTopic(topic);

        return wechaty.getRoomManager().find(roomQueryFilter);
    }


    public static Contact loadContact(Contact contact) {

        if (!contact.isReady()) {
            contact.sync();
        }

        logger.debug("is ready : " + contact.isReady());
        logger.debug(String.format("contact ID:%s, contact name:%s", contact.getId(), contact.name()));
        return contact;

    }

}
