package cn.edu.nju.software.dao.impl;

import cn.edu.nju.software.dao.BaseDao;
import cn.edu.nju.software.dao.OrderDao;
import cn.edu.nju.software.models.Order;
import cn.edu.nju.software.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl implements OrderDao{

    @Autowired
    private BaseDao baseDao;

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public void saveOrder(Order order) throws Exception {
        baseDao.save(order);
    }

    @Override
    public Order findOrder(String oid) throws Exception {
        return (Order) baseDao.load(Order.class, Integer.parseInt(oid));
    }

    @Override
    public List<Order> getOrdersUnpaid() throws Exception {
        List list = baseDao.getAllList(Order.class);
        List<Order> orders = new ArrayList<>();
        for(Object o: list) {
            Order order = (Order) o;
            if(order.getStatus() == OrderStatus.PAYING.ordinal())
                orders.add(order);
        }
        return orders;
    }

    @Override
    public void update(Order order) throws Exception {
        baseDao.update(order);
    }

    @Override
    public Map<String, Double> getOrderpriceGroupByYear(int venueid) throws Exception {
        List list = baseDao.query("select year(o.booktime), sum(r.price) from cn.edu.nju.software.models.Order as o," +
                " cn.edu.nju.software.models.Record as r," +
                " cn.edu.nju.software.models.Plan as p where p.venueid = " + venueid + " and" +
                " p.activityid = r.activityid and r.orderid = o.orderid group by year(o.booktime)");
        Map<String, Double> total = new HashMap<>();
        for(Object o: list) {
            Object[] objs = (Object[])o;
            total.put(objs[0].toString(), -(Double) objs[1]);
        }
        return total;
    }

    @Override
    public Map<String, Double> getOrderpriceGroupByMonth(int venueid) throws Exception {
        List list = baseDao.query("select year(o.booktime), month(o.booktime), sum(r.price) from cn.edu.nju.software.models.Order as o," +
                " cn.edu.nju.software.models.Record as r," +
                " cn.edu.nju.software.models.Plan as p where p.venueid = " + venueid + " and" +
                " p.activityid = r.activityid and r.orderid = o.orderid group by year(o.booktime), month(o.booktime)");
        Map<String, Double> total = new HashMap<>();
        for(Object o: list) {
            Object[] objs = (Object[])o;
            total.put(objs[0].toString()+"年"+objs[1].toString()+"月", -(Double)objs[2]);
        }
        return total;
    }

    @Override
    public Map<String, Double> getOrderaveragepriceGroupByYear(int venueid) throws Exception {
        List list = baseDao.query("select year(o.booktime), avg(r.price) from cn.edu.nju.software.models.Order as o," +
                " cn.edu.nju.software.models.Record as r," +
                " cn.edu.nju.software.models.Plan as p where p.venueid = " + venueid + " and" +
                " p.activityid = r.activityid and r.orderid = o.orderid group by year(o.booktime)");
        Map<String, Double> average = new HashMap<>();
        for(Object o: list) {
            Object[] objs = (Object[])o;
            average.put(objs[0].toString(), -(Double) objs[1]);
        }
        return average;
    }

    @Override
    public Map<String, Double> getOrderaveragepriceGroupByMonth(int venueid) throws Exception {
        List list = baseDao.query("select year(o.booktime), month(o.booktime), avg(r.price) from cn.edu.nju.software.models.Order as o," +
                " cn.edu.nju.software.models.Record as r," +
                " cn.edu.nju.software.models.Plan as p where p.venueid = " + venueid + " and" +
                " p.activityid = r.activityid and r.orderid = o.orderid group by year(o.booktime), month(o.booktime)");
        Map<String, Double> average = new HashMap<>();
        for(Object o: list) {
            Object[] objs = (Object[])o;
            average.put(objs[0].toString()+"年"+objs[1].toString()+"月", -(Double)objs[2]);
        }
        return average;
    }

    @Override
    public Map<String, Double> getOrderpriceGroupByYear() throws Exception {
        List list = baseDao.query("select year(o.booktime), sum(r.price) from cn.edu.nju.software.models.Order as o," +
                " cn.edu.nju.software.models.Record as r where" +
                " r.orderid = o.orderid group by year(o.booktime)");
        Map<String, Double> total = new HashMap<>();
        for(Object o: list) {
            Object[] objs = (Object[])o;
            total.put(objs[0].toString(), -(Double) objs[1]);
        }
        return total;
    }

    @Override
    public Map<String, Double> getOrderpriceGroupByMonth() throws Exception {
        List list = baseDao.query("select year(o.booktime), month(o.booktime), sum(r.price) from cn.edu.nju.software.models.Order as o," +
                " cn.edu.nju.software.models.Record as r where" +
                " r.orderid = o.orderid group by year(o.booktime), month(o.booktime)");
        Map<String, Double> total = new HashMap<>();
        for(Object o: list) {
            Object[] objs = (Object[])o;
            total.put(objs[0].toString()+"年"+objs[1].toString()+"月", -(Double)objs[2]);
        }
        return total;
    }

    @Override
    public Map<String, Double> getOrderaveragepriceGroupByYear() throws Exception {
        List list = baseDao.query("select year(o.booktime), avg(r.price) from cn.edu.nju.software.models.Order as o," +
                " cn.edu.nju.software.models.Record as r where" +
                " r.orderid = o.orderid group by year(o.booktime)");
        Map<String, Double> average = new HashMap<>();
        for(Object o: list) {
            Object[] objs = (Object[])o;
            average.put(objs[0].toString(), -(Double) objs[1]);
        }
        return average;
    }

    @Override
    public Map<String, Double> getOrderaveragepriceGroupByMonth() throws Exception {
        List list = baseDao.query("select year(o.booktime), month(o.booktime), avg(r.price) from cn.edu.nju.software.models.Order as o," +
                " cn.edu.nju.software.models.Record as r where" +
                " r.orderid = o.orderid group by year(o.booktime), month(o.booktime)");
        Map<String, Double> average = new HashMap<>();
        for(Object o: list) {
            Object[] objs = (Object[])o;
            average.put(objs[0].toString()+"年"+objs[1].toString()+"月", -(Double)objs[2]);
        }
        return average;
    }

}
