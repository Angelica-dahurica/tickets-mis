package cn.edu.nju.software.dao;

import cn.edu.nju.software.models.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    public void saveOrder(Order order)throws Exception;

    public Order findOrder(String oid) throws Exception;

    public List<Order> getOrdersUnpaid() throws Exception;

    public void update(Order order) throws Exception;

    public Map<String, Double> getOrderpriceGroupByYear(int venueid) throws Exception;

    public Map<String,Double> getOrderpriceGroupByMonth(int venueid) throws Exception;

    public Map<String,Double> getOrderaveragepriceGroupByYear(int venueid) throws Exception;

    public Map<String,Double> getOrderaveragepriceGroupByMonth(int venueid) throws Exception;

    public Map<String,Double> getOrderpriceGroupByYear() throws Exception;

    public Map<String,Double> getOrderpriceGroupByMonth() throws Exception;

    public Map<String,Double> getOrderaveragepriceGroupByYear() throws Exception;

    public Map<String,Double> getOrderaveragepriceGroupByMonth() throws Exception;
}
