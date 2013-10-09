package amu.action;

import amu.database.ReviewDAO;
import amu.model.Customer;
import amu.model.Review;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class VoteForReviewAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer");
            return actionResponse;
        }

        if (request.getMethod().equals("GET") && request.getParameter("review") != null) {
            int voteforreview = Integer.parseInt(request.getParameter("review"));
            ReviewDAO reviewDAO = new ReviewDAO();
            Review review = reviewDAO.getReviewsById(voteforreview);
            if (reviewDAO.voteForReview(voteforreview, customer)) {
                return new ActionResponse(ActionResponseType.FORWARD, "bookNotFound");
            } else {
                return new ActionResponse(ActionResponseType.FORWARD, "bookNotFound");
            }
        }
        return new ActionResponse(ActionResponseType.FORWARD, "bookNotFound");
    }
}
