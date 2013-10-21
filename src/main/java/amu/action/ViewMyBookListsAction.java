package amu.action;

import amu.database.BookListDAO;
import amu.model.BookList;
import amu.model.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ViewMyBookListsAction implements Action {
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "myBooklists");
            return actionResponse;
        }

        BookListDAO bookListDAO = new BookListDAO();
        List<BookList> booklist = bookListDAO.getBookListsForCustomer(customer);

        session.setAttribute("booklists", booklist);
        ActionResponse actionResponse = new ActionResponse(ActionResponseType.FORWARD, "myBooklists");
        return actionResponse;
    }
}
