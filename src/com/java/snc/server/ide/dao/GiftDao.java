package snc.server.ide.dao;



import snc.server.ide.pojo.Gift;
import snc.server.ide.pojo.User;

import java.util.List;

public interface GiftDao {
    public void UpdateStock(Gift gift);   //修改库存
    public void Updatept(User user);
}
