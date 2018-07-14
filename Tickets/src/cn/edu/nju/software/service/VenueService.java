package cn.edu.nju.software.service;

import cn.edu.nju.software.models.Record;
import cn.edu.nju.software.models.Venue;

import java.util.List;
import java.util.Map;

public interface VenueService {

    public int register(Venue venue) throws Exception;

    public void updateVenue(Venue venue) throws Exception;

    public Venue findVenue(String venueid) throws Exception;

    public List<Venue> getToCheckVenues() throws Exception;

    public List<String> getOrderByVenueid(int venueid) throws Exception;

    public List<Record> getVenueRecords(List<String> aids) throws Exception;

    public List<Venue> getAllVenues() throws Exception;

    public Map<String, Integer> getMemberLikeTimeList(String venueid) throws Exception;

    public Map<String, Double> getSoldOutPercentList(int venueid) throws Exception;

    public Map<String, Double> getSitDownPercentList(int venueid) throws Exception;

    public Map<String,Integer> getPointMemberList(String venueid) throws Exception;

    public Map<String,Integer> getTypePercentList(int venueid) throws Exception;

    public Map<String,Double> getTotalPrice(int venue, String countType) throws Exception;

    public Map<String,Double> getAveragePrice(int venueid, String countType) throws Exception;

    public Map<String,Double> getTotalPrice(String countType) throws Exception;

    public Map<String,Double> getAveragePrice(String countType) throws Exception;
}
