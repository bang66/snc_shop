package snc.server.ide.service;

public interface UserInfoService {
    public String getUUID(String aid);
    public String getCM(String uuid);
    public String getDockerID(String uuid);
    public String getDebugPort(String uuid);
    public int setDebugPort(String uuid);
    public int getStatus(String aid);
}
