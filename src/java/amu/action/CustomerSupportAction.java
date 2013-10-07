package amu.action;

import amu.Mailer;
import amu.model.Customer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class CustomerSupportAction implements Action {

    public static String[] allowed_departments = {"tdt4237.amu.darya@gmail.com", "tdt4237.amu.darya@gmail.com"};

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "customerSupport");
            return actionResponse;
        }

        if (request.getMethod().equals("POST")) {
            if(isValidDepartment(request.getParameter("department"))) {
                Mailer.send(request.getParameter("department"),
                        request.getParameter("subject"),
                        request.getParameter("content"),
                        request.getParameter("fromAddr"),
                        request.getParameter("fromName"));
                // TODO: Send receipt to customer
                return new ActionResponse(ActionResponseType.REDIRECT, "customerSupportSuccessful");
            }
        } 

        return new ActionResponse(ActionResponseType.FORWARD, "customerSupport");
    }

    private boolean isValidDepartment(String department) {
        for(String valid : allowed_departments) {
            if(department.equals(valid))
                return true;
        }
        return false;
    }
}
