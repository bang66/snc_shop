package snc.server.ide.dao;



import snc.server.ide.pojo.User;
import snc.server.ide.pojo.Vm;

import java.io.StringReader;

public interface VmDao {
    public String getAccount(String pid);
    public void updateAccount(User user);
    public void insertMessage(Vm vm);
}
