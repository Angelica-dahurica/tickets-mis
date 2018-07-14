package cn.edu.nju.software.dao.impl;

import cn.edu.nju.software.dao.BaseDao;
import cn.edu.nju.software.dao.PresaleDao;
import cn.edu.nju.software.models.Presale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PresaleDaoImpl implements PresaleDao {

    @Autowired
    private BaseDao baseDao;

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public void save(Presale presale) throws Exception {
        baseDao.save(presale);
    }

    @Override
    public List<Presale> find(int orderid) throws Exception {
        List list = baseDao.query("from cn.edu.nju.software.models.Presale as p where p.orderid = " + orderid);
        List<Presale> presales = new ArrayList<>();
        for(Object o: list) {
            presales.add((Presale) o);
        }
        return presales;
    }

    @Override
    public List<String> getAidsByemail(String email) throws Exception {
        List list = baseDao.query("from cn.edu.nju.software.models.Presale as p where p.email = '" + email + "'");
        List<String> aids = new ArrayList<>();
        for(Object o: list) {
            aids.add(String.valueOf(((Presale) o).getActivityid()));
        }
        return aids;
    }

    @Override
    public List<Presale> getPresalesByEmail(String email) throws Exception {
        List list = baseDao.query("select p.email, p.orderid, p.activityid, p.type, p.quantity, p.price from cn.edu.nju.software.models.Presale as p, cn.edu.nju.software.models.Order as o where p.email = '" + email + "' and o.orderid = p.orderid ORDER BY o.booktime ASC");
        List<Presale> presales = new ArrayList<>();
        for(Object o: list) {
            Object[] objs = (Object[])o;
            Presale presale = new Presale();
            presale.setEmail((String) objs[0]);
            presale.setOrderid((Integer) objs[1]);
            presale.setActivityid((Integer) objs[2]);
            presale.setType((String) objs[3]);
            presale.setQuantity((String) objs[4]);
            presale.setPrice((String) objs[5]);
            presales.add(presale);
        }
        return presales;
    }

    @Override
    public List<Presale> getAll() throws Exception {
        List list = baseDao.getAllList(Presale.class);
        List<Presale> presales = new ArrayList<>();
        for(Object o: list) {
            presales.add((Presale) o);
        }
        return presales;
    }

    @Override
    public List<String> getALLAids() throws Exception {
        List list = baseDao.getAllList(Presale.class);
        List<String> aids = new ArrayList<>();
        for(Object o: list) {
            aids.add(String.valueOf(((Presale) o).getActivityid()));
        }
        return aids;
    }

}
