package cn.edu.nju.software.action;

import cn.edu.nju.software.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PriceInVenueAction extends BaseAction {

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
        return "priceInVenue";
    }

}
