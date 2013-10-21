package amu.action;

import amu.database.BookListDAO;
import amu.model.BookList;
import amu.model.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteBookListAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "myBooklists");
            return actionResponse;
        }
        int booklistId = Integer.parseInt(request.getParameter("id"));

        BookListDAO bookListDAO = new BookListDAO();
        BookList bookList = bookListDAO.getBookListById(booklistId);

        bookListDAO.deleteBooklist(bookList, customer);

        return new ActionResponse(ActionResponseType.REDIRECT, "myBooklists");

    }
}
