package first.workshop.judgedemo.web.controller;

import first.workshop.judgedemo.model.binding.ExerciseBindingModel;
import first.workshop.judgedemo.model.entity.Exercise;
import first.workshop.judgedemo.model.service.ExerciseAddServiceModel;
import first.workshop.judgedemo.service.ExerciseService;
import first.workshop.judgedemo.util.WebUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static first.workshop.judgedemo.constants.ControllerConstants.*;

@Controller
@RequestMapping(value = "/exercises")
public class ExerciseController {
    private final ModelMapper modelMappper;
    private final ExerciseService exerciseService;

    public ExerciseController(ModelMapper modelMappper, ExerciseService exerciseService) {
        this.modelMappper = modelMappper;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/add")
    public ModelAndView getExercise(ModelAndView mv, HttpSession session, Model model,
                                    @ModelAttribute(value = "exercise") ExerciseBindingModel exerciseBindingModel) {
        ModelAndView view = WebUtil
                .Redirection
                .redirectUnauthorizedUser(session, mv, "/home");

        if (view != null) return view;
        mv.addObject("exercise", exerciseBindingModel);

        WebUtil.Helper.setModelView(mv, EXERCISE_ADD_VIEW);
        return mv;
    }

    @PostMapping("/add")
    public ModelAndView postExercise(@Valid @ModelAttribute("exercise") ExerciseBindingModel exerciseBindingModel,
                                     BindingResult result, ModelAndView mv,
                                     RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.exercise", result);
            attributes.addFlashAttribute("exercise", exerciseBindingModel);

            WebUtil.Helper.setModelView(mv, EXERCISE_ADD_VIEW);
            return mv;
        } else {
            exerciseService.addExercise(modelMappper.map(exerciseBindingModel, ExerciseAddServiceModel.class));
        }

        mv.setView(new RedirectView("/exercises/add"));
        return mv;
    }
}
