package Data.Wrapper.Essence;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class PurchProducts {
    public int ID_PurchP;
    public int ID_Product;
    public String Tittle_Product;
    public String Detail_Product;
    public int Quantity;
    public float Price;
    public long Date;

    public PurchProducts() {}
}
