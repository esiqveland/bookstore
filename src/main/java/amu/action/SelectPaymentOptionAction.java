package amu.action;

import amu.database.CreditCardDAO;
import amu.model.Cart;
import amu.model.CreditCard;
import amu.model.Customer;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class SelectPaymentOptionAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "selectPaymentOption");
            return actionResponse;
        }

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            return new ActionResponse(ActionResponseType.REDIRECT, "viewCart");
        }


        if (cart.getShippingAddress() == null) {
            return new ActionResponse(ActionResponseType.REDIRECT, "selectShippingAddress");
        }

        CreditCardDAO creditCardDAO = new CreditCardDAO();
        
        // Handle credit card selection submission
        if (request.getMethod().equals("POST")) {
            CreditCard creditCard = creditCardDAO.read(Integer.parseInt(request.getParameter("creditCardID")), customer);
            if(creditCard !=null) {
                cart.setCreditCard(creditCard);
                return new ActionResponse(ActionResponseType.REDIRECT, "reviewOrder");
            }
        }
        
        List<CreditCard> creditCards = creditCardDAO.browse(customer);
        request.setAttribute("creditCards", creditCards);

        // Else GET request
        return new ActionResponse(ActionResponseType.FORWARD, "selectPaymentOption");
    }

}
