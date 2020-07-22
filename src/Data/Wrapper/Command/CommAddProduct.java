package Data.Wrapper.Command;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class CommAddProduct {
    public int ID_Shop;
    public String Tittle;
    public String Detail;
    public float Price;

    public CommAddProduct() {}
}
