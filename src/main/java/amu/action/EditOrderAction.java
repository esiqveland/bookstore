package amu.action;

import amu.database.AddressDAO;
import amu.database.OrderDAO;
import amu.database.OrderItemsDAO;
import amu.model.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        OrderDAO orderDAO = new OrderDAO();
        int orderId = Integer.parseInt(request.getParameter("id"));

        Order oldOrder = orderDAO.getOrder(customer.getId(), orderId);

        if (request.getMethod().equals("POST")) {
            Cart oldOrderCart = oldOrder.getCart();
            Cart newCart = new Cart();

            double subTotal = 0.0;
            for (CartItem cartItem : oldOrderCart.getItems().values()) {
                String bookQuantity = request.getParameter(cartItem.getBook().getIsbn13());
                int quantity = Integer.parseInt(bookQuantity);
                newCart.addItem(new CartItem(cartItem.getBook(), quantity));
                subTotal += cartItem.getBook().getPrice() * quantity;
            }

            Order newOrder = new Order(customer, oldOrder.getAddress(), String.valueOf(subTotal), newCart);
            orderDAO.add(newOrder);
            orderDAO.cancel(orderId, customer.getId());
        }

        if (request.getMethod().equals("GET")) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.FORWARD, "editOrder");
            actionResponse.addParameter("id", String.valueOf(orderId));
            request.setAttribute("cart", oldOrder.getCart());
            request.setAttribute("orderid", orderId);
            return actionResponse;
        }
        
        return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
    }



}
