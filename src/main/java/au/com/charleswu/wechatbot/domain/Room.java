package au.com.charleswu.wechatbot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Room {
    private String roomId = "";
    private String topic = "";
//    private List<Contact> roomMember;
}
