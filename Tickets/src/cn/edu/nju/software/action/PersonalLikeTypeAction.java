package cn.edu.nju.software.action;

import cn.edu.nju.software.models.Member;
import cn.edu.nju.software.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class PersonalLikeTypeAction extends BaseAction {

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
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        Map<String, Integer> types = memberService.personalLikeTypeList(member.getEmail());
        request.setAttribute("types", types);
        return "personalLikeType";
    }

}
