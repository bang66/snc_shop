package snc.server.ide.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snc.server.ide.dao.UserInfoDao;
import snc.server.ide.service.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public String getUUID(String aid) {
        return userInfoDao.getUUID(aid);
    }

    @Override
    public String getCM(String uuid) {
        return userInfoDao.getCM(uuid);
    }

    @Override
    public String getDockerID(String uuid) {
        return userInfoDao.getDockerID(uuid);
    }

    @Override
    public String getDebugPort(String uuid) {
        return userInfoDao.getDebugPort(uuid);
    }

    @Override
    public int setDebugPort(String uuid) {
        return userInfoDao.setDebugPort(uuid);
    }

    @Override
    public int getStatus(String aid) {
        return userInfoDao.getStatus(aid);
    }
}
