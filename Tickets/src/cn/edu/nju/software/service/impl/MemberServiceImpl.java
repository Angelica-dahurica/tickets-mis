package cn.edu.nju.software.service.impl;

import cn.edu.nju.software.dao.*;
import cn.edu.nju.software.models.*;
import cn.edu.nju.software.service.MemberService;
import cn.edu.nju.software.utils.ActivityType;
import cn.edu.nju.software.utils.OrderStatus;
import cn.edu.nju.software.utils.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberDao memberDao;
    private OrderDao orderDao;
    private TicketOwnDao ticketOwnDao;
    private TicketDao ticketDao;
    private SeatDao seatDao;
    private PresaleDao presaleDao;
    private BookDao bookDao;
    private RecordDao recordDao;
    private ManagerDao managerDao;
    private CouponDao couponDao;
    private PlanDao planDao;
    private VenueDao venueDao;
    private ActivityDao activityDao;

    public CouponDao getCouponDao() {
        return couponDao;
    }

    public void setCouponDao(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public MemberDao getMemberDao() {
        return memberDao;
    }

    public ManagerDao getManagerDao() {
        return managerDao;
    }

    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
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

    public TicketOwnDao getTicketOwnDao() {
        return ticketOwnDao;
    }

    public void setTicketOwnDao(TicketOwnDao ticketOwnDao) {
        this.ticketOwnDao = ticketOwnDao;
    }

    public TicketDao getTicketDao() {
        return ticketDao;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public SeatDao getSeatDao() {
        return seatDao;
    }

    public void setSeatDao(SeatDao seatDao) {
        this.seatDao = seatDao;
    }

    public PresaleDao getPresaleDao() {
        return presaleDao;
    }

    public void setPresaleDao(PresaleDao presaleDao) {
        this.presaleDao = presaleDao;
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public RecordDao getRecordDao() {
        return recordDao;
    }

    public void setRecordDao(RecordDao recordDao) {
        this.recordDao = recordDao;
    }

    public PlanDao getPlanDao() {
        return planDao;
    }

    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }

    public VenueDao getVenueDao() {
        return venueDao;
    }

    public void setVenueDao(VenueDao venueDao) {
        this.venueDao = venueDao;
    }

    public ActivityDao getActivityDao() {
        return activityDao;
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    @Override
    public Member checkMemberLogin(String email, String password) throws Exception {
        Member member = memberDao.findMember(email);
        if(member == null){
            return null;
        }else if(member.getPassword().equals(password)){
            return member;
        }
        return null;
    }

    @Override
    public void register(Member member) throws Exception {
        memberDao.saveMember(member);
    }

    @Override
    public void updateMember(Member member) throws Exception {
        memberDao.updateMember(member);
    }

    @Override
    public void cancelQualification(String email) throws Exception {
        Member member = memberDao.findMember(email);
        member.setActive(0);
        memberDao.updateMember(member);
    }

    @Override
    public Member findMember(String email) throws Exception {
        return memberDao.findMember(email);
    }

    @Override
    public int submitOrder(Member member) throws Exception {
        Order order = new Order();
        order.setOrderid(getNewOid());
        order.setBooktime(new Timestamp(Calendar.getInstance().getTime().getTime()));
        order.setStatus(OrderStatus.PAYING.ordinal());
        orderDao.saveOrder(order);

        Book book = new Book();
        book.setEmail(member.getEmail());
        book.setOrderid(order.getOrderid());
        bookDao.saveBook(book);

        return order.getOrderid();
    }

    @Override
    public void prebookTickets(String email, int activityid, int orderid,
                               String[] price, String[] quantity, String[] type) throws Exception {
        Presale presale = new Presale();
        presale.setEmail(email);
        presale.setActivityid(activityid);
        presale.setOrderid(orderid);
        for(int i = 0; i < price.length; i++) {
            presale.setPrice(price[i]);
            presale.setQuantity(quantity[i]);
            presale.setType(String.valueOf(type[i].equals("内场") ? 0 : 1));
            presaleDao.save(presale);
        }
    }

    @Override
    public void updatePaidBook(String orderid) throws Exception {
        Order order = orderDao.findOrder(String.valueOf(orderid));
        order.setStatus(OrderStatus.PAID.ordinal());
        orderDao.update(order);
    }

    @Override
    public void updatePaidBook(String orderid, List tickets) throws Exception {
        Order order = orderDao.findOrder(String.valueOf(orderid));
        order.setStatus(OrderStatus.PAID.ordinal());
        orderDao.update(order);

        for(Object o : tickets) {
            Ticket t = (Ticket) o;
            t.setLocktime(null);
            t.setLockperson(null);
            t.setStatus(TicketStatus.SOLDOUT.ordinal());
            ticketDao.updateTicket(t);

            TicketOwn ticketOwn = new TicketOwn();
            ticketOwn.setOrderid(order.getOrderid());
            ticketOwn.setTicketid(t.getTicketid());
            ticketOwnDao.save(ticketOwn);
        }
    }

    @Override
    public void updateQuitBook(String orderid) throws Exception {
        List<TicketOwn> ticketOwns = ticketOwnDao.findByOid(orderid);
        if(ticketOwns!= null && ticketOwns.size()!=0) {
            for(TicketOwn ticketOwn : ticketOwns) {
                ticketOwnDao.delete(ticketOwn);
                Ticket ticket = ticketDao.findTicket(String.valueOf(ticketOwn.getTicketid()));
                ticket.setStatus(TicketStatus.ONSELLING.ordinal());
                ticketDao.updateTicket(ticket);
            }
        }

        Order order = orderDao.findOrder(String.valueOf(orderid));
        order.setStatus(OrderStatus.QUIT.ordinal());
        orderDao.update(order);
    }

    @Override
    public Manager getManager() throws Exception {
        return managerDao.getManager();
    }

    @Override
    public List<Member> getAllMembers() throws Exception {
        return memberDao.getAllMembers();
    }

    @Override
    public void updateManager(Manager manager) throws Exception {
        managerDao.update(manager);
    }

    @Override
    public void saveCoupon(String email) throws Exception {
        Coupon cou = couponDao.find(email);
        if(cou == null) {
            Coupon coupon = new Coupon();
            coupon.setEmail(email);
            coupon.setPrice(10);
            coupon.setQuantity(1);
            couponDao.save(coupon);
        } else {
            cou.setQuantity(cou.getQuantity() + 1);
            couponDao.update(cou);
        }
    }

    @Override
    public Map<String, Integer> personalLikeCityList(String email) throws Exception {
        List<String> aids = presaleDao.getAidsByemail(email);
        Map<String, Integer> cities = new HashMap<>();
        for(String aid: aids) {
            String city = venueDao.findVenue(String.valueOf(planDao.findPlan(aid).getVenueid())).getCity();
            if(!cities.containsKey(city)) {
                cities.put(city, 1);
            } else {
                cities.replace(city, cities.get(city)+1);
            }
        }
        return cities;
    }

    @Override
    public Map<String, Integer> personalLikeVenueList(String email) throws Exception{
        List<String> aids = presaleDao.getAidsByemail(email);
        Map<String, Integer> venues = new HashMap<>();
        for(String aid: aids) {
            String venuename = venueDao.findVenue(String.valueOf(planDao.findPlan(aid).getVenueid())).getPlace();
            if(!venues.containsKey(venuename)) {
                venues.put(venuename, 1);
            } else {
                venues.replace(venuename, venues.get(venuename)+1);
            }
        }
        return venues;
    }

    @Override
    public Map<String, Integer> personalLikeTimeList(String email) throws Exception {
        List<String> aids = presaleDao.getAidsByemail(email);
        Map<String, Integer> times = new HashMap<>();
        for(String aid: aids) {
            Timestamp timestamp = activityDao.findActivity(aid).getTime();
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
    public Map<String, Integer> priceRangeAcceptedList(String email) throws Exception {
        List<Presale> presales = presaleDao.getPresalesByEmail(email);
        Map<String, Integer> prices = new HashMap<>();
        for(Presale presale: presales) {
            if(!prices.containsKey(presale.getPrice())) {
                prices.put(presale.getPrice(), Integer.valueOf(presale.getQuantity()));
            } else {
                prices.replace(presale.getPrice(), prices.get(presale.getPrice())+Integer.parseInt(presale.getQuantity()));
            }
        }
        prices = sortMapByKey(prices);
        return prices;
    }

    @Override
    public Map<String, Integer> personalLikeTypeList(String email) throws Exception {
        List<String> aids = presaleDao.getAidsByemail(email);
        Map<String, Integer> types = new HashMap<>();
        for(String aid: aids) {
            String type = ActivityType.values()[activityDao.findActivity(aid).getType()].toString();
            if(!types.containsKey(type)) {
                types.put(type, 1);
            } else {
                types.replace(type, types.get(type)+1);
            }
        }
        return types;
    }

    @Override
    public Map<Timestamp, Double> personalPayPercentList(String email) throws Exception {
        List<Presale> presales = presaleDao.getPresalesByEmail(email);
        Map<Timestamp, Double> payPercents = new HashMap<>();
        int total = 0;
        int paid = 0;   //QUIT不算付款的
        for(Presale presale: presales) {
            Order order = orderDao.findOrder(String.valueOf(presale.getOrderid()));
            if(order.getStatus() == OrderStatus.PAID.ordinal()) {
                paid++;
            }
            total++;
            Double paypercent = paid * 1.00 /total;
            payPercents.put(order.getBooktime(), paypercent);
        }
        payPercents = sortMapTimestampByKey(payPercents);
        return payPercents;
    }

    @Override
    public Map<Timestamp, Double> personalDonePercentList(String email) throws Exception {
        List<Presale> presales = presaleDao.getPresalesByEmail(email);
        Map<Timestamp, Double> donePercents = new HashMap<>();
        int total = 0;
        int checked = 0;
        for(Presale presale: presales) {
            Order order = orderDao.findOrder(String.valueOf(presale.getOrderid()));
            List<TicketOwn> ticketOwns = ticketOwnDao.findByOid(String.valueOf(order.getOrderid()));
            for(TicketOwn ticketOwn : ticketOwns) {
                if (ticketDao.findTicket(String.valueOf(ticketOwn.getTicketid())).getStatus()
                        == TicketStatus.CHECKED.ordinal()) {
                    checked++;
                }
            }
            total+=Integer.parseInt(presale.getQuantity());
            Double donePercent = checked * 1.00 /total;
            donePercents.put(order.getBooktime(), donePercent);
        }
        donePercents = sortMapTimestampByKey(donePercents);
        return donePercents;
    }

    @Override
    public Map<String, Integer> getMemberLikeVenueList() throws Exception {
        List<Presale> presales = presaleDao.getAll();
        Map<String, Integer> venues = new HashMap<>();
        for(Presale presale: presales) {
            String venuename = venueDao.findVenue(String.valueOf(planDao.findPlan(String.valueOf(presale.getActivityid())).getVenueid())).getPlace();
            if(!venues.containsKey(venuename)) {
                venues.put(venuename, 1);
            } else {
                venues.replace(venuename, venues.get(venuename)+1);
            }
        }
        return venues;
    }

    @Override
    public Map<String, Integer> getMemberLikeCityList() throws Exception {
        List<Presale> presales = presaleDao.getAll();
        Map<String, Integer> cities = new HashMap<>();
        for(Presale presale: presales) {
            String city = venueDao.findVenue(String.valueOf(planDao.findPlan(String.valueOf(presale.getActivityid())).getVenueid())).getCity();
            if(!cities.containsKey(city)) {
                cities.put(city, 1);
            } else {
                cities.replace(city, cities.get(city)+1);
            }
        }
        return cities;
    }

    @Override
    public Map<String, Integer> getMemberLikeTimeList() throws Exception {
        List<Presale> presales = presaleDao.getAll();
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
    public List<Order> getOrderByEmail(String email) throws Exception {
        List<Book> books = bookDao.getAllBookList(email);
        List<Order> orders = new ArrayList<>();
        for(Book book: books) {
            orders.add(orderDao.findOrder(String.valueOf(book.getOrderid())));
        }
        return orders;
    }

    @Override
    public List<Record> getMemberRecords(List<Order> orders) throws Exception {
        List<Record> records = new ArrayList<>();

        for(Order o : orders) {
            List<Record> tmp = recordDao.find(o.getOrderid());
            if(tmp != null) {
                for (Record r : tmp) {
                    records.add(r);
                }
            }
        }
        return records;
    }

    @Override
    public List<Presale> getPresaleByOid(int orderid) throws Exception {
        return presaleDao.find(orderid);
    }

    private int getNewOid() throws Exception{
        while(true){
            int randNum = (int)(Math.random()*9999999)+1;
            String orderid = String.format("%09d", randNum);
            if(orderDao.findOrder(orderid) == null){
                return Integer.parseInt(orderid);
            }
        }
    }

    private int getNewTid() throws Exception{
        while(true){
            int randNum = (int)(Math.random()*9999999)+1;
            String ticketid = String.format("%09d", randNum);
            if(ticketDao.findTicket(ticketid) == null){
                return Integer.parseInt(ticketid);
            }
        }
    }

    private void balance(List<Seat> seats, String price) throws Exception {
        for(Seat seat: seats){
            if (seat.getPrice() == Double.parseDouble(price)) {
                seat.setNum(seat.getNum()-1);
                seatDao.update(seat);
            }
        }
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

    private static Map<Timestamp, Double> sortMapTimestampByKey(Map<Timestamp, Double> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<Timestamp, Double> sortMap = new TreeMap<>(
                new MapKeyTimestampComparator());
        sortMap.putAll(map);
        return sortMap;
    }


    private static class MapKeyTimestampComparator implements Comparator<Timestamp>{
        @Override
        public int compare(Timestamp str1, Timestamp str2) {
            return str1.compareTo(str2);
        }
    }

}
