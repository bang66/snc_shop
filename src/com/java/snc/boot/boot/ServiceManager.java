package snc.boot.boot;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jac on 18-11-16.
 */
public class ServiceManager {
    private static ClassPathXmlApplicationContext context = null;
    public static void init() {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public static <T> Object getService(String id) {
        return context.getBean(id);
    }
}
