package cn.edu.nju.software.action;

import cn.edu.nju.software.models.Venue;
import cn.edu.nju.software.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by txin15 on 2018/7/2.
 */
public class GetPriceAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private VenueService venueService;

    public VenueService getVenueService() {
        return venueService;
    }

    public void setVenueService(VenueService venueService) {
        this.venueService = venueService;
    }

    @Override
    public String execute() throws Exception {
        HttpSession session = request.getSession();
        String countType = request.getParameter("countType");

        Venue venue = (Venue) session.getAttribute("venue");
        Map<String, Double> total;
        Map<String, Double> average;
        if(venue == null) {
            total = venueService.getTotalPrice(countType);
            average = venueService.getAveragePrice(countType);
        } else {
            total = venueService.getTotalPrice(venue.getVenueid(), countType);
            average = venueService.getAveragePrice(venue.getVenueid(), countType);
        }
        request.setAttribute("total", total);
        request.setAttribute("average", average);
        return "getPrice";
    }

}
