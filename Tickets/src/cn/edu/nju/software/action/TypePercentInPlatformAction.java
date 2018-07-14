package cn.edu.nju.software.action;

import cn.edu.nju.software.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class TypePercentInPlatformAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ActivityService activityService;

    public ActivityService getActivityService() {
        return activityService;
    }

    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public String execute() throws Exception {
        Map<String, Integer> type_percents = activityService.getTypePercentList();
        request.setAttribute("type_percents", type_percents);
        return "typePercentInPlatform";
    }

}
