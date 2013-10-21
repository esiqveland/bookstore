package amu.action;

import amu.database.BookDAO;
import amu.database.BookListDAO;
import amu.model.Book;
import amu.model.BookList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewBookListAction implements Action {
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        BookListDAO bookListDAO = new BookListDAO();

        BookList bookList = bookListDAO.getBookListById(Integer.parseInt(request.getParameter("id")));

        if (bookList != null) {
            request.setAttribute("booklist", bookList);
        }

        return new ActionResponse(ActionResponseType.FORWARD, "viewBooklist");
    }
}
