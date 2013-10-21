package amu.action;

import amu.database.BookDAO;
import amu.database.BookListDAO;
import amu.model.BookList;
import amu.model.Cart;
import amu.model.CartItem;
import amu.model.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreateBookListAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer");
            return actionResponse;
        }

        if(request.getMethod().equals("GET")) {
            return new ActionResponse(ActionResponseType.FORWARD, "createBooklist");
        }

        Cart cart = (Cart) session.getAttribute("cart");
        if(cart == null || cart.getNumberOfItems()==0) {
            return new ActionResponse(ActionResponseType.REDIRECT, "viewCart");
        }

        BookList bookList = new BookList();
        bookList.setAuthor(customer);
        bookList.setTitle(request.getParameter("title"));
        bookList.setDescription(request.getParameter("description"));
        for(CartItem item : cart.getItems().values()) {
            bookList.addBook(item.getBook());
        }
        BookListDAO bookListDAO = new BookListDAO();
        bookListDAO.save(bookList);

    }
}
