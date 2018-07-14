package cn.edu.nju.software.action;

import cn.edu.nju.software.models.Venue;
import cn.edu.nju.software.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class TypePercentInVenueAction extends BaseAction {

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
        Map<String, Integer> type_percents = venueService.getTypePercentList(venue.getVenueid());
        request.setAttribute("type_percents", type_percents);
        return "typePercentInVenue";
    }

}
