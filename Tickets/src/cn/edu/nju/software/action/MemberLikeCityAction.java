package cn.edu.nju.software.action;

import cn.edu.nju.software.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class MemberLikeCityAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private MemberService memberService;

    public MemberService getMemberService() {
        return memberService;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public String execute() throws Exception {
        Map<String, Integer> cities = memberService.getMemberLikeCityList();
        request.setAttribute("cities", cities);
        return "memberLikeCity";
    }

}
