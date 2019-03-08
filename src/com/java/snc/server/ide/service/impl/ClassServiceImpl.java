package snc.server.ide.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import snc.boot.boot.ServiceManager;
import snc.server.ide.dao.ClassDao;
import snc.server.ide.pojo.Class;
import snc.server.ide.pojo.User;
import snc.server.ide.service.ClassService;
@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassDao classDao;

    @Override
    public String getClspt(String cls_id) {
        System.out.println(cls_id);
        return classDao.getClspt(cls_id);
    }

    @Override
    public String getCM(String uuid) {
        System.out.println(uuid);
        return classDao.getCM(uuid);
    }

    @Override
    public void Updatemoney(User user) {
        System.out.println(user.getUuid());
        classDao.Updatemoney(user);


    }

    @Override
    public void Updateclass(User user) {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//        classDao = context.getBean(ClassDao.class);
//        classDao = (ClassDao) ServiceManager.getService(String.valueOf(Class.class));
        classDao.Updateclass(user);
    }
}
