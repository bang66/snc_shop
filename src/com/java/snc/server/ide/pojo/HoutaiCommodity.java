package snc.server.ide.pojo;

public class HoutaiCommodity {

    private String c_sid;               //礼物id
    private String c_name;
    private double c_price;
    private double marketprice;      //市场价
    private String c_brand;
    private String c_image;
    private int c_type;
    private String c_spuid;              //是否是同一个商品只是属性不同
    private int c_sold;                //销量

    private String c_col;            //商品颜色
    private String c_mod;            //型号
    private String c_size;              //大小

    private String c_details;
    private int c_stock;               //库存
    public String getC_spuid() {
        return c_spuid;
    }

    public void setC_spuid(String c_spuid) {
        this.c_spuid = c_spuid;
    }
    public double getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(double marketprice) {
        this.marketprice = marketprice;
    }

    public int getC_sold() {
        return c_sold;
    }

    public void setC_sold(int c_sold) {
        this.c_sold = c_sold;
    }

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

    public String getC_col() {
        return c_col;
    }

    public void setC_col(String c_col) {
        this.c_col = c_col;
    }

    public String getC_mod() {
        return c_mod;
    }

    public void setC_mod(String c_mod) {
        this.c_mod = c_mod;
    }

    public String getC_size() {
        return c_size;
    }

    public void setC_size(String c_size) {
        this.c_size = c_size;
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

    public String getC_details() {
        return c_details;
    }

    public void setC_details(String c_details) {
        this.c_details = c_details;
    }

    public int getC_stock() {
        return c_stock;
    }

    public void setC_stock(int c_stock) {
        this.c_stock = c_stock;
    }
}
