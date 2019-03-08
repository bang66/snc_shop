package snc.server.ide.pojo;

public class Commodity {
    private String c_sid;               //礼物id
    private String c_name;
    private double c_price;
    private double marketprice;      //市场价
    private String c_brand;
    private String c_image;
    private int c_type;
    private String c_spuid;              //是否是同一个商品只是属性不同
    private int c_sold;                //销量

    public String getC_sid() {
        return c_sid;
    }

    public void setC_sid(String c_sid) {
        this.c_sid = c_sid;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public double getC_price() {
        return c_price;
    }

    public void setC_price(double c_price) {
        this.c_price = c_price;
    }

    public double getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(double marketprice) {
        this.marketprice = marketprice;
    }

    public String getC_brand() {
        return c_brand;
    }

    public void setC_brand(String c_brand) {
        this.c_brand = c_brand;
    }

    public String getC_image() {
        return c_image;
    }

    public void setC_image(String c_image) {
        this.c_image = c_image;
    }

    public int getC_type() {
        return c_type;
    }

    public void setC_type(int c_type) {
        this.c_type = c_type;
    }

    public String getC_spuid() {
        return c_spuid;
    }

    public void setC_spuid(String c_spuid) {
        this.c_spuid = c_spuid;
    }

    public int getC_sold() {
        return c_sold;
    }

    public void setC_sold(int c_sold) {
        this.c_sold = c_sold;
    }
}
