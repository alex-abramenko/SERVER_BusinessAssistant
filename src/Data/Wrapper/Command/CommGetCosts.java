package Data.Wrapper.Command;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class CommGetCosts {
    public long startDate;
    public long endDate;
    public int ID_Shop;
    public String[] jsonCosts;
}
