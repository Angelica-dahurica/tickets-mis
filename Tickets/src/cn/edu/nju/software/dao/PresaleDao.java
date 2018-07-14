package cn.edu.nju.software.dao;

import cn.edu.nju.software.models.Presale;

import java.util.List;

public interface PresaleDao {

    public void save(Presale presale) throws Exception;

    public List<Presale> find(int orderid) throws Exception;

    public List<String> getAidsByemail(String email) throws Exception;

    public List<Presale> getPresalesByEmail(String email) throws Exception;

    public List<Presale> getAll() throws Exception;

    public List<String> getALLAids() throws Exception;
}
