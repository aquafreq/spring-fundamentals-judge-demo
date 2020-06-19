package first.workshop.judgedemo.web.controller;

import first.workshop.judgedemo.model.binding.RoleBindingModel;
import first.workshop.judgedemo.model.view.UserViewModel;
import first.workshop.judgedemo.service.RoleService;
import first.workshop.judgedemo.service.UserService;
import first.workshop.judgedemo.util.WebUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static first.workshop.judgedemo.constants.ControllerConstants.*;

@Controller
@RequestMapping(value = "/roles")
public class RoleController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final RoleService roleService;

    public RoleController(UserService userService, ModelMapper modelMapper, RoleService roleService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }

    @GetMapping("/add")
    public ModelAndView getAddRole(@ModelAttribute("error") String error,
                                   HttpSession session,
                                   ModelAndView mv,
                                   Model model) {
        ModelAndView modelAndView = WebUtil.Redirection.redirectUnauthorizedUser(session, mv, "/users/login");
        if (modelAndView != null) return modelAndView;

//        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("ADMIN_ROLE")) {
//            mv.setViewName(REDIRECT_ANONYMOUS_HOME);
//        }

        mv.addObject("role", new RoleBindingModel());

        if (!error.isBlank()) {
            mv.addObject("error", error);
        } else if (model.containsAttribute("role")) {
            mv.addAllObjects(fillModelAndViewMap(model));
        }

        List<UserViewModel> allUsers = fetchAllUsers(session);
        mv.addObject(USERS_VIEW_MODEL, allUsers);
        WebUtil.Helper.setModelView(mv, ROLE_ADD_VIEW);

        return mv;
    }

    private Map<String, Object> fillModelAndViewMap(Model model) {
        return model
                .asMap()
                .entrySet()
                .stream()
                .filter(k -> !k.getKey().toLowerCase().contains("modelandview"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<UserViewModel> fetchAllUsers(HttpSession session) {
        return userService.getAllUsers()
                .stream()
                .map(u -> modelMapper.map(u, UserViewModel.class))
                .filter(u -> !u.getUsername().equals(session.getAttribute("username")))
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ModelAndView postAddRole(@Valid @ModelAttribute("role") RoleBindingModel roleBindingModel,
                                    BindingResult result, ModelAndView mv,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            mv.setView(new RedirectView("/roles/add"));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.role", result);
            redirectAttributes.addFlashAttribute("role", roleBindingModel);
            return mv;
        } else {
            try {
                userService.changeUserRole(roleBindingModel.getUsername(), roleBindingModel.getRole());
            } catch (IllegalArgumentException e) {
                mv.setView(new RedirectView("/roles/add"));
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return mv;
            }
        }

        WebUtil.Helper.setModelView(mv, ROLE_ADD_VIEW);
        return mv;
    }
}
