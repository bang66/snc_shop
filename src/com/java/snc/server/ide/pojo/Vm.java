package snc.server.ide.pojo;

public class Vm {
    private String uid;
    private String type;
    private String cpu;
    private String mem;
    private String disk ;
    private String paydate;

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String pid) {
        this.uid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMem() {
        return mem;
    }

    @Override
    public String toString() {
        return "Vm{" +
                "uid='" + uid + '\'' +
                ", type='" + type + '\'' +
                ", cpu='" + cpu + '\'' +
                ", mem='" + mem + '\'' +
                ", disk='" + disk + '\'' +
                ", paydate='" + paydate + '\'' +
                '}';
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }
}
