package cn.edu.nju.software.action;

import cn.edu.nju.software.models.Venue;
import cn.edu.nju.software.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class SoldOutPercentInVenueAction extends BaseAction {

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
        Venue venue = (Venue) session.getAttribute("venue");
        Map<String, Double> sold_outs = venueService.getSoldOutPercentList(venue.getVenueid());
        request.setAttribute("sold_outs", sold_outs);
        return "soldOutPercentInVenue";
    }

}
