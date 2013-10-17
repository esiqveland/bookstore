/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import amu.database.AddressDAO;
import amu.database.OrderDAO;
import amu.database.OrderItemsDAO;
import amu.model.Address;
import amu.model.Customer;
import amu.model.OrderItems;
import amu.model.SimpleOrder;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Knut
 */

class EditOrderAction implements Action {

	@Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");
        

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer");
            return actionResponse;
        }

        //Fetch existing order and order items:
        OrderDAO orderDAO = new OrderDAO();
        OrderItemsDAO orderItemsDAO = new OrderItemsDAO();
        List<OrderItems> orderItemsList;
        int orderId = Integer.parseInt(request.getParameter("id"));
        
        SimpleOrder sOrder = orderDAO.findById(orderId);
        orderItemsList = orderItemsDAO.browseByOrderId(orderId);
        
        //Cancel orginale poster
        orderDAO.cancel(orderId);
        
        //Legg inn ny ordre som er modifisert til nye produkter
        for (OrderItems orderItems : orderItemsList) {
			//Create new orderitems for each post in hashmap
        	int bookId = orderItems.getBook_id();
        	int quantity = (int) request.getAttribute(String.valueOf(bookId));
		}
        
        //Legg inn 
        
        
        
        if (request.getMethod().equals("POST")) {
           
        }

        // (request.getMethod().equals("GET")) 
        
        return new ActionResponse(ActionResponseType.FORWARD, "editAddress");
    }

   
}
