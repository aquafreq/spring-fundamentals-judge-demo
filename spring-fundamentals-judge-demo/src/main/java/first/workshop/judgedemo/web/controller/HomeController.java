package first.workshop.judgedemo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
public class HomeController {

    @GetMapping("/home")
    public ModelAndView getHome(ModelAndView modelAndView, HttpSession session) {

        System.out.println(session);
        modelAndView.addObject("username", session.getAttribute("username"));
        modelAndView.setViewName("home");

        return modelAndView;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session, HttpServletResponse response) {
        session
                .getAttributeNames()
                .asIterator()
                .forEachRemaining(e -> {
                    System.out.println();
                    if (session.getAttribute(e) instanceof Cookie) {
                        response.addCookie((Cookie) session.getAttribute(e));
                    }
                });

        model.addAttribute("username", session.getAttribute("username"));
        return "index";
    }

}
