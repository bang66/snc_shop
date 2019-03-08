package snc.server.ide.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snc.server.ide.dao.GiftDao;
import snc.server.ide.pojo.Gift;
import snc.server.ide.pojo.User;
import snc.server.ide.service.GiftService;
@Service
public class GiftServiceImp implements GiftService {
    @Autowired
    private GiftDao giftDao;

    @Override
    public void UpdateStock(Gift gift) {
        System.out.println(gift.getG_id()+"   "+gift.getG_stock());
        giftDao.UpdateStock(gift);
    }

    @Override
    public void Updatept(User user) {
        System.out.println(user.getUuid()+"<<<<<<=============>>>>>>"+user.getPt());
        giftDao.Updatept(user);
    }
}
