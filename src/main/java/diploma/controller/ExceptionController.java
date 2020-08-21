//package diploma.controller;
//
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@ControllerAdvice
//public class ExceptionController {
//
//    public ModelAndView handlerError(HttpServletRequest request, Exception e) {
//        Logger.getLogger(getClass().getName())
//                .log(Level.SEVERE, "Request: " + request.getRequestURL() + " raiser " + e);
//        return new ModelAndView("error");
//    }
//
//    public ModelAndView handlerError404(HttpServletRequest request, Exception e) {
//        Logger.getLogger(getClass().getName())
//                .log(Level.SEVERE, "Request: " + request.getRequestURL() + " raiser " + e);
//        return new ModelAndView("404");
//    }
//
//}
