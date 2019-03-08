package snc.server.ide.service.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import snc.boot.redis.GetJedis;
import snc.boot.util.FinalTable;
import snc.boot.util.common.Router;
import snc.server.ide.pojo.User;
import snc.server.ide.service.VmService;
import snc.server.ide.pojo.Vm;
import snc.server.ide.pojo.VmPay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyVm {
    private static Logger log=Logger.getLogger(BuyVm.class);
    private VmService vmService;
    Jedis jedis =  GetJedis.getJedis();
    User user=new User();

    //构造
    public BuyVm(VmService vmService){
        this.vmService=vmService;
    }

    //获取时间戳
    public  String getDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        return date;
    }



    //计算云主机的价格
    public int calculatePrice(Vm vm){
        log.info("价格为100学时");
        return 100;

    }

    //查询用户的剩余学时
    public int getMoney(String pid){
        System.out.println("uid------------>"+pid);
        int account;
        if (jedis.hexists(pid,"CM")){
            account=Integer.parseInt(jedis.hget(pid,"CM"));
            log.info("redis 已记录该用户学时-------->pid："+pid+"   account:"+account);
        }
        else {
        System.out.println("vm-------"+vmService);
            account=Integer.parseInt(vmService.getAccount(pid));
            jedis.hset(pid,"m",String.valueOf(account));
            log.info("已从db中查到余额并存入redis-------->pid:"+pid+"  account:"+account);
        }
        return account;
    }

    //购买云主机
    public String buy(Vm vm){
        String pid= vm.getUid();
        int account = getMoney(pid);
        int price=calculatePrice(vm);
        int currentAccount;
        String json="";
        VmPay vmPay=new VmPay();
        if (account>=price){
            user.setUuid(pid);
            log.info("用户当前可用余额大于价格---------->uid:"+pid);
            currentAccount=account-price;
            user.setCls_pt(String.valueOf(currentAccount));
            jedis.hset(pid,"CM",String.valueOf(currentAccount));
            vmService.updateAccount(user);
            vmPay.setState(true);
            log.info("vm购买成功------------>uid:"+pid+"  currentAccount:"+currentAccount);
            String payDate=getDate();
            vm.setPaydate(payDate);
            if ((vm.getType()).equals("vm1")){
                vm.setCpu("1");
                vm.setDisk("1");
                vm.setMem("1");
                vmService.insertMessage(vm);
                log.info(pid+"用户购买的vm信息已存");
            }else {
                vmService.insertMessage(vm);
                log.info(pid+"用户购买的vm信息已存");
            }
            json=JSON.toJSONString(vmPay);
            return  json;
        }
        else {
            log.info("用户购买vm失败-------->uid："+pid);
            vmPay.setState(false);
            json=JSON.toJSONString(vmPay);
            return  json;
        }
    }

    //接受前端的请求，并实例化为vm对象
    public Vm getMessage(FullHttpRequest fhr){
//        Vm vm = JSON.parseObject(json,Vm.class);
        Vm vm=new Vm();
        JSONObject o = Router.getMessage(fhr);

        System.out.println(o);
        String uid = o.getString(FinalTable.UUID);

        String type = o.getString(FinalTable.VM_TYPE);
        String cpu = o.getString(FinalTable.VM_CPU);
        String mem = o.getString(FinalTable.VM_MEM);
        String disk = o.getString(FinalTable.VM_DISK);

        vm.setUid(uid);
        vm.setType(type);
        vm.setCpu(cpu);
        vm.setMem(mem);
        vm.setDisk(disk);

        return vm;
    }


//    public static void main(String[] args) {
//        String json="{\"pid\":\"111\",\"type\":\"vm1\",\"cpu\":\"2\",\"mem\":\"2g\",\"disk\":\"20g\"}";
//        BuyVm buyVm =new BuyVm();
//        Vm vm=buyVm.getMessage(json);
//        System.out.println(vm.getCpu()+"  "+vm.getDisk()+"  "+vm.getMem()+"  "+vm.getType()+"  "+vm.getPid());
//    }
}
