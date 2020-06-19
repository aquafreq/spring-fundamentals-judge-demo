package first.workshop.judgedemo.web.controller;

import first.workshop.judgedemo.model.service.ExerciseViewServiceModel;
import first.workshop.judgedemo.service.ExerciseService;
import first.workshop.judgedemo.service.UserService;
import first.workshop.judgedemo.util.WebUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

import static first.workshop.judgedemo.constants.ControllerConstants.HOME_VIEW;

@Controller
public class HomeController {
    private final UserService userService;
    private final ExerciseService exerciseService;
    private final ModelMapper modelMapper;

    public HomeController(UserService userService, ExerciseService exerciseService, ModelMapper modelMapper) {
        this.userService = userService;
        this.exerciseService = exerciseService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/home")
    public ModelAndView getHome(ModelAndView modelAndView, HttpSession session) {
        ModelAndView view = WebUtil.Redirection.redirectNonLoggedUser(session, modelAndView, "/users/login");
        if (view != null) return view;


        List<String> users = userService.getAllUsernameByAverageGrade();
        List<String> exercises = exerciseService
                .getAllExercises()
                .stream()
                .map(ExerciseViewServiceModel::getName)
                .collect(Collectors.toList());

        modelAndView.addObject("exercises", exercises);
        modelAndView.addObject("users", users);
        modelAndView.setViewName(HOME_VIEW);
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
