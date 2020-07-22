package Data.Wrapper.Command;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class CommGetCategories {
    public String[] jsonCategories;

    public CommGetCategories() {}
}
