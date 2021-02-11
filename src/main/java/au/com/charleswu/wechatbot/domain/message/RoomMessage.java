package au.com.charleswu.wechatbot.domain.message;

import au.com.charleswu.wechatbot.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RoomMessage extends Message {
//    private final ChatChannel channel = ChatChannel.ROOM;
    private Room room;
}
