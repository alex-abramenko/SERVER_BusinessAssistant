package Data.Wrapper.Command;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class CommGetUserInfo {
    public String login;
    public int id;

    public CommGetUserInfo() {}
}
