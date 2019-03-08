package snc.server.ide.dao;

/**
 * Created by jac on 18-11-17.
 */
public interface UserInfoDao {
    public String getUUID(String aid);
    public String getCM(String uuid);
    public String getDockerID(String uuid);
    public String getDebugPort(String uuid);
    public int setDebugPort(String uuid);
    public int getStatus(String aid);
}
