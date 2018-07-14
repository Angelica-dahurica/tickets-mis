package cn.edu.nju.software.service.impl;

import cn.edu.nju.software.dao.*;
import cn.edu.nju.software.models.*;
import cn.edu.nju.software.service.VenueService;
import cn.edu.nju.software.utils.ActivityType;
import cn.edu.nju.software.utils.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueDao venueDao;
    private PlanDao planDao;
    private RecordDao recordDao;
    private PresaleDao presaleDao;
    private ActivityDao activityDao;
    private TicketDao ticketDao;
    private MemberDao memberDao;
    private OrderDao orderDao;

    public VenueDao getVenueDao() {
        return venueDao;
    }

    public void setVenueDao(VenueDao venueDao) {
        this.venueDao = venueDao;
    }

    public PlanDao getPlanDao() {
        return planDao;
    }

    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }

    public RecordDao getRecordDao() {
        return recordDao;
    }

    public void setRecordDao(RecordDao recordDao) {
        this.recordDao = recordDao;
    }

    public PresaleDao getPresaleDao() {
        return presaleDao;
    }

    public void setPresaleDao(PresaleDao presaleDao) {
        this.presaleDao = presaleDao;
    }

    public ActivityDao getActivityDao() {
        return activityDao;
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public TicketDao getTicketDao() {
        return ticketDao;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public MemberDao getMemberDao() {
        return memberDao;
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public int register(Venue venue) throws Exception {
        venue.setVenueid(getNewVid());
        venue.setStatus(0);
        venue.setBala(0);
        venueDao.saveVenue(venue);
        return venue.getVenueid();
    }

    @Override
    public void updateVenue(Venue venue) throws Exception {
        venueDao.updateVenue(venue);
    }

    @Override
    public Venue findVenue(String venueid) throws Exception {
        return venueDao.findVenue(venueid);
    }

    @Override
    public List<Venue> getToCheckVenues() throws Exception {
        List<Venue> venueList = venueDao.getAllVenues();
        List<Venue> venues = new ArrayList<>();
        for(Venue venue: venueList) {
            if (venue.getStatus() != 1) {
                venues.add(venue);
            }
        }
        return venues;
    }

    private int getNewVid() throws Exception{
        while(true){
            int randNum = (int)(Math.random()*9999999)+1;
            String venueid = String.format("%07d", randNum);
            if(venueDao.findVenue(venueid) == null){
                return Integer.parseInt(venueid);
            }
        }
    }

    @Override
    public List<String> getOrderByVenueid(int venueid) throws Exception {
        return planDao.getAllActivityids(String.valueOf(venueid));
    }

    @Override
    public List<Record> getVenueRecords(List<String> aids) throws Exception {
        List<Record> records = new ArrayList<>();
        for(String aid : aids) {
            List<Record> tmp = recordDao.findByAid(aid);
            if(tmp != null) {
                for (Record r : tmp) {
                    records.add(r);
                }
            }
        }
        return records;
    }

    @Override
    public List<Venue> getAllVenues() throws Exception {
        return venueDao.getAllVenues();
    }

    @Override
    public Map<String, Integer> getMemberLikeTimeList(String venueid) throws Exception {
        List<String> aids = planDao.getAllActivityids(venueid);
        List<Presale> presales = getByaids(aids);
        Map<String, Integer> times = new HashMap<>();
        for(Presale presale: presales) {
            Timestamp timestamp = activityDao.findActivity(String.valueOf(presale.getActivityid())).getTime();
            String time = new SimpleDateFormat("HH:mm").format(timestamp);
            if(!times.containsKey(time)) {
                times.put(time, 1);
            } else {
                times.replace(time, times.get(time)+1);
            }
        }
        times = sortMapByKey(times);
        return times;
    }

    @Override
    public Map<String, Double> getSoldOutPercentList(int venueid) throws Exception {
        List<String> aids = planDao.getAllActivityids(String.valueOf(venueid));
        Map<String, Double> soldOutPercents = new HashMap<>();
        int sold_out = 0;
        for(String aid: aids) {
            Activity activity = activityDao.findActivity(aid);
            List<Ticket> tickets = ticketDao.getTickets(aid);
            for(Ticket ticket : tickets) {
                if(ticket.getStatus() == TicketStatus.SOLDOUT.ordinal() ||
                        ticket.getStatus() == TicketStatus.CHECKED.ordinal()) {
                    sold_out++;
                }
            }
            soldOutPercents.put(activity.getName(), sold_out * 1.00 / tickets.size());
        }
        return soldOutPercents;
    }

    @Override
    public Map<String, Double> getSitDownPercentList(int venueid) throws Exception {
        List<String> aids = planDao.getAllActivityids(String.valueOf(venueid));
        Map<String, Double> sitDownPercents = new HashMap<>();
        for(String aid: aids) {
            int sit_down = 0;
            int total = 0;
            Activity activity = activityDao.findActivity(aid);
            List<Ticket> tickets = ticketDao.getTickets(aid);
            for(Ticket ticket : tickets) {
                if(ticket.getStatus() == TicketStatus.CHECKED.ordinal()) {
                    sit_down++;
                    total++;
                } else if(ticket.getStatus() == TicketStatus.SOLDOUT.ordinal()) {
                    total++;
                }
            }
            if(total == 0) {
                sitDownPercents.put(activity.getName(), 0.0);
            } else {
                sitDownPercents.put(activity.getName(), sit_down * 1.00 / total);
            }
        }
        return sitDownPercents;
    }

    @Override
    public Map<String, Integer> getPointMemberList(String venueid) throws Exception {
        Map<String, Integer> pointMembers = new HashMap<>();
        List<String> aids = planDao.getAllActivityids(venueid);
        List<Presale> presales = getByaids(aids);
        for(Presale presale: presales) {
            String name = memberDao.findMember(presale.getEmail()).getName();
            if(!pointMembers.containsKey(name)) {
                pointMembers.put(name, 1);
            } else {
                pointMembers.replace(name, pointMembers.get(name)+1);
            }
        }
        return pointMembers;
    }

    @Override
    public Map<String, Integer> getTypePercentList(int venueid) throws Exception {
        List<Presale> presales = presaleDao.getAll();
        Map<String, Integer> types = new HashMap<>();
        for(Presale presale: presales) {
            if(planDao.findPlan(String.valueOf(presale.getActivityid())).getVenueid() == venueid) {
                String type = ActivityType.values()[activityDao.findActivity(String.valueOf(presale.getActivityid())).getType()].toString();
                if (!types.containsKey(type)) {
                    types.put(type, 1);
                } else {
                    types.replace(type, types.get(type) + 1);
                }
            }
        }
        return types;
    }

    @Override
    public Map<String, Double> getTotalPrice(int venueid, String countType) throws Exception {
        Map<String, Double> total = new HashMap<>();
        if(countType.equals("year")) {
            total = orderDao.getOrderpriceGroupByYear(venueid);
        } else if(countType.equals("month")) {
            total = orderDao.getOrderpriceGroupByMonth(venueid);
        }
        total = sortMapByDoubleKey(total);
        return total;
    }

    @Override
    public Map<String, Double> getAveragePrice(int venueid, String countType) throws Exception {
        Map<String, Double> average = new HashMap<>();
        if(countType.equals("year")) {
            average = orderDao.getOrderaveragepriceGroupByYear(venueid);
        } else if(countType.equals("month")) {
            average = orderDao.getOrderaveragepriceGroupByMonth(venueid);
        }
        average = sortMapByDoubleKey(average);
        return average;
    }

    @Override
    public Map<String, Double> getTotalPrice(String countType) throws Exception {
        Map<String, Double> total = new HashMap<>();
        if(countType.equals("year")) {
            total = orderDao.getOrderpriceGroupByYear();
        } else if(countType.equals("month")) {
            total = orderDao.getOrderpriceGroupByMonth();
        }
        total = sortMapByDoubleKey(total);
        return total;
    }

    @Override
    public Map<String, Double> getAveragePrice(String countType) throws Exception {
        Map<String, Double> average = new HashMap<>();
        if(countType.equals("year")) {
            average = orderDao.getOrderaveragepriceGroupByYear();
        } else if(countType.equals("month")) {
            average = orderDao.getOrderaveragepriceGroupByMonth();
        }
        average = sortMapByDoubleKey(average);
        return average;
    }

    private List<Presale> getByaids(List<String> aids) throws Exception {
        List<Presale> presales = new ArrayList<>();
        List<Presale> presales_all = presaleDao.getAll();
        for(Presale presale: presales_all) {
            if(aids.contains(String.valueOf(presale.getActivityid()))) {
                presales.add(presale);
            }
        }
        return presales;
    }

    private static Map<String, Integer> sortMapByKey(Map<String, Integer> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortMap = new TreeMap<>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }


    private static class MapKeyComparator implements Comparator<String>{
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    private static Map<String, Double> sortMapByDoubleKey(Map<String, Double> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Double> sortMap = new TreeMap<>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

}
