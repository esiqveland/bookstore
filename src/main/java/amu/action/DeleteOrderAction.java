/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.OrderDAO;
import amu.model.Customer;

/**
 *
 * @author Knut
 */
public class DeleteOrderAction implements Action{
	
	@Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
		
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer");
            return actionResponse;
        }

        OrderDAO orderDAO = new OrderDAO();
        orderDAO.cancel(Integer.parseInt(request.getParameter("id")), customer.getId());
        
        return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
    }  
}
