package snc.server.ide.dao;

import org.apache.ibatis.annotations.Param;
import snc.server.ide.pojo.User;

public interface ClassDao {
    public String getClspt(String cls_id);
    public String getCM(String uuid);
    public void Updatemoney(User user);
    public  void Updateclass(User user);
//    public void Updatemoney(@Param(value = "cls_pt") String  cls_pt, @Param(value = "uid")String uid);
//    public  void Updateclass(@Param(value = "course")String course, @Param(value = "uid")String uid);
}
