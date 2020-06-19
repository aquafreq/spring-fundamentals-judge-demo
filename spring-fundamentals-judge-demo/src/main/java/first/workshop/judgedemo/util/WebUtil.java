package first.workshop.judgedemo.util;

import first.workshop.judgedemo.model.service.UserServiceProfileInfoModel;
import first.workshop.judgedemo.model.view.UserProfileViewModel;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

import java.util.Map;

import static first.workshop.judgedemo.constants.ControllerConstants.MAIN_LAYOUT;
import static first.workshop.judgedemo.constants.ControllerConstants.VIEW_NAME;

@Component
public class WebUtil {
    public static class Redirection {
        public static ModelAndView redirectNonLoggedUser
                (HttpSession session, ModelAndView mv, String path) {
            if (isUserNotLogged(session)) {
                mv.setView(new RedirectView(path));
                return mv;
            }
            return null;
        }

        public static ModelAndView redirectUnauthorizedUser
                (HttpSession session, ModelAndView mv, String path) {
            if (isUserNotLogged(session) ||
                    !session.getAttribute("role").equals("ADMIN_ROLE")) {
                mv.setView(new RedirectView(path));
                return mv;
            }
            return null;
        }


        private static boolean isUserNotLogged(HttpSession session) {
            return session.getAttribute("role") == null;
        }
    }

    public static class Helper {
        public static void setModelView(ModelAndView mv, String view) {
            mv.addObject(VIEW_NAME, view);
            setMainLayout(mv);
        }

        private static void setMainLayout(ModelAndView mv) {
            mv.setViewName(MAIN_LAYOUT);
        }

        public static void setModelView(ModelAndView mv,
                                        Map<String, Object> viewObjectModel) {
            mv.addAllObjects(viewObjectModel);
            setMainLayout(mv);
        }

        public static void setRedirectView(ModelAndView mv, String redirectView) {
            mv.setView(new RedirectView(redirectView));
        }

        public static void setModelView(ModelAndView mv,
                                        UserProfileViewModel userProfileInfo,
                                        String view) {
            mv.addObject("userProfile", userProfileInfo);
            setModelView(mv, view);
        }
    }
}


