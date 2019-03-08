package snc.server.ide.dao;

import snc.server.ide.pojo.A_User;
import snc.server.ide.pojo.Vm;

import java.util.List;
import java.util.Map;

public interface UinfoDao {
    public A_User getAllMessage(String uid);
    public void changeMsg(Map map);
    public void delUinfo(String uid);
    public List<Vm> getUinfoVm(String uid);


}
