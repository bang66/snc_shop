package snc.server.ide.pojo;

/**
 * Created by jac on 18-11-16.
 */
public class UserInfo {
    private String ide_id; //ide id
    private String dockerid; // docker 容器id
    private String port; //端口号
    private String user_id; //对应的uuid

    public String getIde_id() {
        return ide_id;
    }

    public void setIde_id(String ide_id) {
        this.ide_id = ide_id;
    }

    public String getDockerid() {
        return dockerid;
    }

    public void setDockerid(String dockerid) {
        this.dockerid = dockerid;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
