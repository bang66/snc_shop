package snc.server.ide.pojo;

import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class User {

    private String orderid;
    private String ADD;
    private String uuid;  //用户id
    private String pt;   //用户积分
    private boolean res;
    private String data;
    private String cid;//用户课时id
    private String cls_pt;//用户学时数
    private String YG;//用户已购课程

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCls_pt() {
        return cls_pt;
    }

    public void setCls_pt(String cls_pt) {
        this.cls_pt = cls_pt;
    }

    public String getYG() {
        return YG;
    }

    public void setYG(String YG) {
        this.YG = YG;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getADD() {
        return ADD;
    }

    public void setADD(String ADD) {
        this.ADD = ADD;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        String orderId = newDate + result;
        return orderId;
    }

    public String getDate(){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dd = df.format(date);
        return dd;
    }


    public String getjson(){
        String json;
        ArrayList<User> userArrayList = new ArrayList<>();
        User user= new User();
        user.getDate();
        user.getOrderId();
        user.setRes(true);
        userArrayList.add(user);
        json = JSON.toJSONString(userArrayList);
        System.out.println(json);
        return json;
    }
}
