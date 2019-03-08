package snc.server.ide.service;

import snc.server.ide.pojo.User;
import snc.server.ide.pojo.Vm;

public interface VmService {
    public String getAccount(String pid);
    public void updateAccount(User user);
    public void insertMessage(Vm vm);
}
