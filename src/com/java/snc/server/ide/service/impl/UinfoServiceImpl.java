package snc.server.ide.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snc.server.ide.dao.UinfoDao;
import snc.server.ide.pojo.A_User;
import snc.server.ide.pojo.Vm;
import snc.server.ide.service.UinfoService;

import java.util.List;
import java.util.Map;
@Service
public class UinfoServiceImpl implements UinfoService {

    @Autowired
    UinfoDao uinfoDao;

    @Override
    public A_User getAllMessage(String uid){
        return uinfoDao.getAllMessage(uid);
    }

    @Override
    public void changeMsg(Map map) {
        uinfoDao.changeMsg(map);
    }

    @Override
    public void delUinfo(String uid) {
        uinfoDao.delUinfo(uid);
    }

    @Override
    public List<Vm> getUinfoVm(String uid) {
        return uinfoDao.getUinfoVm(uid);
    }
}
