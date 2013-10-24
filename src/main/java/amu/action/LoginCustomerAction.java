package amu.action;

import amu.database.CustomerDAO;
import amu.model.Customer;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class LoginCustomerAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, String> values = new HashMap<String, String>();
        request.setAttribute("values", values);
        if (ActionFactory.hasKey(request.getParameter("from"))) {
            values.put("from", request.getParameter("from"));
        }

        if (request.getMethod().equals("POST")) {

            String remoteAddr = request.getRemoteAddr();
            ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
            reCaptcha.setPrivateKey("6Lc8QOkSAAAAABqBZdDu8ksk95Ew57Xacipc4F-w");

            String challenge = request.getParameter("recaptcha_challenge_field");
            String uresponse = request.getParameter("recaptcha_response_field");
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

            if (!reCaptchaResponse.isValid()) {
                return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
            }

            Map<String, String> messages = new HashMap<String, String>();
            request.setAttribute("messages", messages);

            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.findByEmail(request.getParameter("email"));

            if (customer != null) {
                values.put("email", request.getParameter("email"));

                if (customer.getActivationToken() == null) {
                    if (customer.getPassword().equals(CustomerDAO.hashPassword(request.getParameter("password")))) {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("customer", customer);
                        if (ActionFactory.hasKey(request.getParameter("from"))) {
                            return new ActionResponse(ActionResponseType.REDIRECT, request.getParameter("from"));
                        }
                    } else { // Wrong password
                        messages.put("email", "Email or password was incorrect.");
                    }
                } else { // customer.getActivationToken() != null
                    return new ActionResponse(ActionResponseType.REDIRECT, "activateCustomer");
                }
            } else { // findByEmail returned null -> no customer with that email exists
                messages.put("email", "Email or password was incorrect.");
            }

            // Forward to login form with error messages
            return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
        }

        return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
    }
}
