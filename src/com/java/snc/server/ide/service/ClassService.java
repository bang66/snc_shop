package snc.server.ide.service;

import snc.server.ide.pojo.User;

public interface ClassService {
    public String getClspt(String cls_id);
    public String getCM(String uuid);
    public void Updatemoney(User user);
    public  void Updateclass(User user);

}
