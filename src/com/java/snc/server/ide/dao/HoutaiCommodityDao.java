package snc.server.ide.dao;

import snc.server.ide.pojo.HoutaiCommodity;
import snc.server.ide.pojo.Vm;

public interface HoutaiCommodityDao {


    public void insertCommodity(HoutaiCommodity h); //插入方法应该在后台 这里只是为了提前存入数据 后台不应该传类 应该直接传字符窜 匹配表
}
