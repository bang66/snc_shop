package snc.server.ide.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snc.server.ide.dao.VmDao;
import snc.server.ide.pojo.User;
import snc.server.ide.pojo.Vm;
import snc.server.ide.service.VmService;
@Service
public class VmServiceImpl implements VmService {

    @Autowired
    private VmDao vmDao;

    @Override
    public String getAccount(String pid) {
        return vmDao.getAccount(pid);
    }

    @Override
    public void updateAccount(User user) {
        vmDao.updateAccount(user);
    }

    @Override
    public void insertMessage(Vm vm) {
        vmDao.insertMessage(vm);
    }
}
