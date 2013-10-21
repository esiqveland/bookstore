package amu.action;

import amu.database.BookDAO;
import amu.database.BookListDAO;
import amu.model.Book;
import amu.model.BookList;
import amu.model.Cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ViewBookListAction implements Action {
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        BookListDAO bookListDAO = new BookListDAO();

        BookList bookList = bookListDAO.getBookListById(Integer.parseInt(request.getParameter("id")));

        if (bookList != null) {
            session.setAttribute("booklist", bookList);
        }

        return new ActionResponse(ActionResponseType.FORWARD, "viewBooklist");
    }
}
