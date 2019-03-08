package snc.server.ide.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snc.server.ide.dao.HoutaiCommodityDao;
import snc.server.ide.pojo.HoutaiCommodity;
import snc.server.ide.service.HoutaiCommodityService;

@Service
public class HoutaiCommodityImpl implements HoutaiCommodityService {
    @Autowired
    public HoutaiCommodityDao houtaiCommodityDao;
    @Override
    public void insertCommodity(HoutaiCommodity h){
         houtaiCommodityDao.insertCommodity(h);
    }

}
