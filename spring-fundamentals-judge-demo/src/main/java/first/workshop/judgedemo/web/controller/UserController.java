package first.workshop.judgedemo.web.controller;

import first.workshop.judgedemo.model.binding.UserLoginBindingModel;
import first.workshop.judgedemo.model.binding.UserRegisterBindingModel;
import first.workshop.judgedemo.model.service.UserServiceLoginModel;
import first.workshop.judgedemo.model.service.UserServiceRegisterModel;
import first.workshop.judgedemo.model.view.UserViewModel;
import first.workshop.judgedemo.service.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.*;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/users")
public class UserController {
    private final ModelMapper modelMapper;
    private final UserServiceImpl userService;

    @Autowired
    public UserController(ModelMapper modelMapper, UserServiceImpl userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin(@ModelAttribute("user") UserLoginBindingModel userLoginBindingModel) {
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@Valid UserLoginBindingModel userLoginBindingModel,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            HttpServletResponse response,
                            HttpServletRequest request
    ) {

        if (result.hasErrors()) {

//            List<String> errors = result
//                    .getFieldErrors()
//                    .stream()
//                    .map(e -> MessageFormat.format("{0} {1}", e.getField(), e.getDefaultMessage()))
//                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("user", userLoginBindingModel);
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
                                Cookie cookie = createCookie("postLogin", "LOKGPLZ");
                                Cookie cookie2 = createCookie("theme", "whiteRaceOP");
                                Cookie usernameCookie = createCookie("user", viewModel.getUsername());

                                session.setAttribute("dobqvqm koki", cookie);
                                session.setAttribute("theme", cookie2);
                                session.setAttribute("username", viewModel.getUsername());

                                response.addCookie(cookie);
                                response.addCookie(cookie2);
                                response.addCookie(usernameCookie);
                                route.route = "redirect:/";
                            },
                            () -> route.route = "redirect:/users/login");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("noSuchUser", e.getMessage());
            return "redirect:/users/login";
        }

        return route.route;
    }

    @GetMapping("/register")
    public String getRegister(Model model,
                              @ModelAttribute("usp") UserRegisterBindingModel bindingModel) {

//        model.addAttribute("errors", new HashMap<String, List<String>>() {{
//            put("gitError", new ArrayList<>() {{
//                add("asd");
//                add("dwadaw");
//                add("zzzz");
//            }});
//        }});

        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute UserRegisterBindingModel userRegisterBindingModel,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               HttpServletResponse response,
                               HttpServletRequest request,
                               HttpSession session) {
//standartSessionFacade 12551
//stanstandSession 12580
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
                    .collect(Collectors.groupingBy(k -> k.getField() +"Error",
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

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);

        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setMaxAge(60);
        return cookie;
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        RedirectView redirectView = new RedirectView("/");
        redirectView.setStatusCode(HttpStatus.BAD_GATEWAY);
        session.invalidate();
        return redirectView;
    }
}
