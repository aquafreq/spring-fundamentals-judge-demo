package first.workshop.judgedemo.web.controller;

import first.workshop.judgedemo.model.binding.CommentBindingModel;
import first.workshop.judgedemo.model.binding.HomeworkBindingModel;
import first.workshop.judgedemo.model.service.CommentServiceAddModel;
import first.workshop.judgedemo.model.service.ExerciseViewServiceModel;
import first.workshop.judgedemo.service.ExerciseService;
import first.workshop.judgedemo.service.HomeworkService;
import first.workshop.judgedemo.service.UserService;
import first.workshop.judgedemo.util.WebUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static first.workshop.judgedemo.constants.ControllerConstants.*;

@Controller
@RequestMapping(value = "/homework")
public class HomeworkController {
    private final ModelMapper modelMapper;
    private final ExerciseService exerciseService;
    private final HomeworkService homeworkService;

    public HomeworkController(ModelMapper modelMapper, ExerciseService exerciseService, HomeworkService homeworkService) {
        this.modelMapper = modelMapper;
        this.exerciseService = exerciseService;
        this.homeworkService = homeworkService;
    }

    @ModelAttribute("exercises")
    public List<String> exercises() {
        return exerciseService
                .getAllExercises()
                .stream()
                .map(ExerciseViewServiceModel::getName)
                .collect(Collectors.toList());
    }

    @GetMapping("/add")
    public ModelAndView getHomeworkAdd(@ModelAttribute("homework") HomeworkBindingModel homeworkBindingModel,
                                       ModelAndView mv, HttpSession session) {
        ModelAndView view = WebUtil.Redirection.redirectNonLoggedUser(session, mv, "/users/login");
        if (view != null) return view;

        mv.addObject("exercises", exercises());
        WebUtil.Helper.setModelView(mv, HOMEWORK_ADD_VIEW);
        return mv;
    }

    @PostMapping("/add")
    public ModelAndView postHomeworkAdd(@Valid @ModelAttribute("homework") HomeworkBindingModel homeworkBindingModel,
                                        BindingResult result,
                                        ModelAndView mv, HttpSession session) {
        boolean timeHasPassed = false;
        if (!result.hasFieldErrors("exercise")) {
            timeHasPassed = homeworkService.checkIfHomeworkDueTimeHasPassed(homeworkBindingModel.getExercise());
        }

        if (result.hasErrors() || timeHasPassed) {
            mv.addObject("hasPassed", timeHasPassed);
            WebUtil.Helper.setModelView(mv, HOMEWORK_ADD_VIEW);
        } else {
            homeworkService.addHomework(
                    session.getAttribute("id").toString(),
                    homeworkBindingModel.getExercise(),
                    homeworkBindingModel.getGitAddress()
            );
            WebUtil.Helper.setRedirectView(mv, REDIRECT_ADD_HOMEWORK_PAGE_VIEW);
        }

        return mv;
    }

    @GetMapping("/check")
    public ModelAndView getHomeCheck(@ModelAttribute("comment") CommentBindingModel commentBindingModel,
                                     ModelAndView mv, HttpSession session,
                                     @ModelAttribute("githubUrl") String githubUrl,
                                     @ModelAttribute("hwError") String noHomeworkError) {
        ModelAndView view = WebUtil.Redirection.redirectNonLoggedUser(session, mv, REDIRECT_ANONYMOUS_LOGIN);
        if (view != null) return view;
        String userId = (String) session.getAttribute("id");
        String homeworkGithub = null;
        WebUtil.Helper.setModelView(mv, HOMEWORK_CHECK_VIEW);
        try {
            homeworkGithub = homeworkService.getRandomHomeworkGithub(userId);
        } catch (IllegalArgumentException e) {
            mv.addObject("hwError", e.getMessage());
        }

        if (!noHomeworkError.isBlank() || homeworkGithub == null) {
            mv.addObject("hwError", noHomeworkError);
        } else {
            mv.addObject("githubUrl", homeworkGithub);
            session.setAttribute("homeworkGithub", homeworkGithub);
        }
        return mv;
    }

    //validation done by main.js
    @PostMapping("/check")
    public ModelAndView postHomeCheck(ModelAndView mv, HttpSession session,
                                      @ModelAttribute("comment") CommentBindingModel commentBindingModel,
                                      Model model) {
        ModelAndView view = WebUtil.Redirection.redirectNonLoggedUser(session, mv, REDIRECT_ANONYMOUS_LOGIN);
        if (view != null) return view;
        String userId = (String) session.getAttribute("id");
        String homeworkGithub = (String) session.getAttribute("homeworkGithub");

        CommentServiceAddModel commentServiceAddModel = modelMapper.map(commentBindingModel, CommentServiceAddModel.class);
        commentServiceAddModel.setUserId(userId);

        try {
            homeworkService.addCommentToHomework(userId, homeworkGithub, commentServiceAddModel);
        } catch (IllegalArgumentException e) {
            model.addAttribute("hwError", e.getMessage());
        }

        session.removeAttribute("homeworkGithub");
        WebUtil.Helper.setRedirectView(mv, REDIRECT_HOMEWORK_CHECK);
        return mv;
    }
//    th:errorclass=bg-danger bootstrap field color
}
