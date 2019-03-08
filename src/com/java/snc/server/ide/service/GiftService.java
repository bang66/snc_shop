package snc.server.ide.service;

import snc.server.ide.pojo.Gift;
import snc.server.ide.pojo.User;

public interface GiftService {
    public void UpdateStock(Gift gift);   //修改库存
    public void Updatept(User user);
}
