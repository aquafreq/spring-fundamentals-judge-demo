package first.workshop.judgedemo.web.controller;

import first.workshop.judgedemo.model.binding.UserLoginBindingModel;
import first.workshop.judgedemo.model.binding.UserRegisterBindingModel;
import first.workshop.judgedemo.model.service.UserServiceLoginModel;
import first.workshop.judgedemo.model.service.UserServiceRegisterModel;
import first.workshop.judgedemo.model.view.UserProfileViewModel;
import first.workshop.judgedemo.model.view.UserViewModel;
import first.workshop.judgedemo.service.impl.UserServiceImpl;
import first.workshop.judgedemo.util.WebUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.*;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static first.workshop.judgedemo.constants.ControllerConstants.*;
import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/users")
public class UserController {
    public static final String USER_HOMEWORKS = "homeworks";
    public static final String USER_PROFILE_VIEW = "profile";
    private final ModelMapper modelMapper;
    private final UserServiceImpl userService;

    @Autowired
    public UserController(ModelMapper modelMapper, UserServiceImpl userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin(Model model, @ModelAttribute("error") String error, HttpSession session) {
        if (session.getAttribute("id") != null) {
            return "redirect:/home";
        }

        if (!model.containsAttribute("user"))
            model.addAttribute("user", new UserLoginBindingModel());
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@Valid @ModelAttribute UserLoginBindingModel userLoginBindingModel,
                            BindingResult result, RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {

        if (result.hasErrors()) {
//            List<String> errors = result
//                    .getFieldErrors()
//                    .stream()
//                    .map(e -> MessageFormat.format("{0} {1}", e.getField(), e.getDefaultMessage()))
//                    .collect(Collectors.toList());
            redirectAttributes.addFlashAttribute("user", userLoginBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            return "redirect:/users/login";
        }

        UserServiceLoginModel user = modelMapper.map(userLoginBindingModel, UserServiceLoginModel.class);

        var route = new Object() {
            String route = "";
        };

        try {
            userService
                    .logUser(user)
                    .ifPresentOrElse(u ->
                    {
                        UserViewModel viewModel = modelMapper.map(u, UserViewModel.class);
                        HttpSession session = request.getSession(true);
                        session.setAttribute("username", viewModel.getUsername());
                        session.setAttribute("id", viewModel.getId());
                        session.setAttribute("role", viewModel.getRoleName());

                        route.route = "redirect:/home";
                    }, () -> route.route = "redirect:/users/login");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/login";
        }

        return route.route;
    }

    @GetMapping("/register")
    public String getRegister(@ModelAttribute("usp") UserRegisterBindingModel bindingModel, HttpSession session) {
        return session.getAttribute("id") == null ? "register" :
                "redirect:/home";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute UserRegisterBindingModel userRegisterBindingModel,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("usp", userRegisterBindingModel);
//            redirectAttributes.addAllAttributes(new HashMap<String, String>() {{
//                put("username", userRegisterBindingModel.getUsername());
//                put("password", userRegisterBindingModel.getPassword());
//                put("confirmPassword", userRegisterBindingModel.getConfirmPassword());
//                put("email", userRegisterBindingModel.getEmail());
//                put("git", userRegisterBindingModel.getGit());
//            }});

//            Map<String, List<String>> errors = new HashMap<>();
//            result
//                    .getFieldErrors()
//                    .forEach(k -> errors.computeIfAbsent(
//                            k.getField(), x -> new ArrayList<>()).add(k.getDefaultMessage()));

            Map<String, List<String>> errors = result
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.groupingBy(k -> k.getField() + "Error",
                            Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, toList())));

            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/users/register";
        }

        UserServiceRegisterModel map = modelMapper.map(userRegisterBindingModel, UserServiceRegisterModel.class);
        userService.saveUser(map);

        Cookie cookie = createCookie("register", "valkata");

        session.setAttribute("nqkfo kuki", cookie);
        return "redirect:/users/login";
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);

        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setMaxAge(60);
        return cookie;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/profile/{id}")
    public ModelAndView getUserProfile(@PathVariable("id") String id, ModelAndView mv, HttpSession session) {
//        UserProfileViewModel userServiceModel =
//                modelMapper.map(userService.getUserServiceModelById(id), UserProfileViewModel.class);
//        UserProfileViewModel profileViewModel =
//                modelMapper.map(userServiceModel, UserProfileViewModel.class);
//        String userHomeworkNames = userService.getUserExercisesById(id);
//        WebUtil
//                .Helper
//                .setModelView(mv,
//                        new HashMap<String, Object>() {{
//                            put(USER_HOMEWORKS, userHomeworkNames);
//                            put(VIEW_NAME, PROFILE_VIEW);
//                            put(USER_VIEW_MODEL, profileViewModel);
//                        }});
//        to change the view to the profile page
        ModelAndView modelAndView = WebUtil
                .Redirection
                .redirectNonLoggedUser(session, mv, REDIRECT_ANONYMOUS_LOGIN);
        if (modelAndView != null) return modelAndView;

        UserProfileViewModel viewModel = modelMapper.map(userService.getUserProfileInfo(id), UserProfileViewModel.class);
        WebUtil.Helper.setModelView(mv, viewModel, PROFILE_VIEW);
        return mv;
    }
}
