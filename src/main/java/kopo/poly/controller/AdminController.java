package kopo.poly.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kopo.poly.dto.BusinessDTO;
import kopo.poly.dto.ReviewDTO;
import kopo.poly.service.AdminService;
import kopo.poly.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("value = /Admin")
@RequiredArgsConstructor
@RestController
@Slf4j

public class AdminController {
    private static Map<Object, Object> controllerMap = new HashMap<Object, Object>();
    HttpServletRequest request;
    Model model;
    HttpSession session;

    private final AdminService adminService; //
    private final ReviewService reviewService;

    @ModelAttribute
    void init(HttpServletRequest request, Model model) {
        this.request = request; // 요청
        this.model = model;
        this.session = request.getSession(); // 세션
    }

    @RequestMapping("adminLogin") // 관리자 로그인 페이지
    public String adminLogin() {
        return "/common/adminLogin";
    } // adminLogin()

    @RequestMapping("adminLoginPro")
    public String adminLoginPro(String email, String password) {
        String msg = "등록된 관리자 계정이 아닙니다.";

        try {
            Boolean check = adminService.adminLogin(email, password);
            if (check) {
                msg = "관리자 모드입니다.";
                model.addAttribute("msg", msg);
                session.setAttribute("admin", email);
                return "redirect:/admin/monthlySales";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("msg", msg);
        return "redirect:/admin/adminLogin";
    }

    @RequestMapping("monthlySales")
    public String monthlySales() {

        try {
            String bu_email =(String)session.getAttribute("bu_email");

            controllerMap.clear();
            controllerMap = adminService.sales(bu_email);

            model.addAttribute("result", controllerMap.get("result"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/admin/monthlySales";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("areaSales")
    public String areaSales(String month) {

        try {
            controllerMap.clear();
            controllerMap = adminService.areaSales(month);

            model.addAttribute("month", controllerMap.get("month"));
            model.addAttribute("result", controllerMap.get("result"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "/admin/areaSales";

    }

    @RequestMapping("categorySales")
    public String categorySales(String month) {

        try {
            controllerMap.clear();
            controllerMap = adminService.categorySales(month);

            model.addAttribute("month", controllerMap.get("month"));
            model.addAttribute("result", controllerMap.get("result"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "/admin/categorySales";
    }

    @RequestMapping("businessApproval")
    public String businessApproval(String pageNum) {

        try {
            // 페이징
            if (pageNum == null) pageNum = "1";
            int pageInt = Integer.parseInt(pageNum);
            int limit = 10;
            int bottomLine = 3;
            int startNum = (pageInt - 1) / bottomLine * bottomLine + 1;
            int endNum = startNum + bottomLine - 1;
            int count = adminService.notApprovalBuCount(); // 게시물 총 개수
            int maxNum = (count / limit) + (count % limit == 0 ? 0 : 1);
            if(endNum >= maxNum){
                endNum = maxNum;
            }

            //	ROWNUM
            int startPage = (pageInt - 1) * limit + 1;
            int endPage = (pageInt - 1) * limit + limit;
            List<BusinessDTO> businessList = adminService.notApprovalBuList(startPage,endPage);

            model.addAttribute("bottomLine", bottomLine);
            model.addAttribute("startNum", startNum);
            model.addAttribute("endNum", endNum);
            model.addAttribute("maxNum", maxNum);
            model.addAttribute("pageInt", pageInt);
            model.addAttribute("businessList", businessList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/admin/businessApproval";
    }

    @ResponseBody
    @PostMapping("approval")
    public boolean approval(String bu_email) {
        try {
            int result = adminService.businessApproval(bu_email);
            if(result != 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @ResponseBody
    @PostMapping("approvalCancel")
    public boolean cancel(String bu_email) {
        try {
            int result = adminService.businessApprovalCancel(bu_email);
            if(result != 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping("reviewReport")
    public String reviewReport() {
        try {
            List<ReviewDTO> reviewList = adminService.reportedReview();
            model.addAttribute("reviewList", reviewList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/admin/reviewReport";
    }

    @ResponseBody
    @PostMapping("reviewDelete")
    public Boolean reviewDelete(int rev_num) {
        try {
            int result = reviewService.deleteReview(rev_num);
            if(result != 0) return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    @ResponseBody
    @PostMapping("reportCancel")
    public Boolean reportCancel(int rev_num) {
        try {
            int result = reviewService.reportCancel(rev_num);
            if(result != 0) return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
