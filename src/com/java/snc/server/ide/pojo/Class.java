package snc.server.ide.pojo;

public class Class {
    private String cls_id;
    private int cls_pt;
    private String cls_json;

    public String getCls_json() {
        return cls_json;
    }

    public void setCls_json(String cls_json) {
        this.cls_json = cls_json;
    }

    public Class(String cid, int cls_pt) {
        this.cls_id = cid;
        this.cls_pt = cls_pt;
    }

    public String getCls_id() {
        return cls_id;
    }

    public void setCls_id(String cls_id) {
        this.cls_id = cls_id;
    }

    public int getCls_pt() {
        return cls_pt;
    }

    public void setCls_pt(int cls_pt) {
        this.cls_pt = cls_pt;
    }
}
