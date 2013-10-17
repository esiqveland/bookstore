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

        if (request.getMethod().equals("GET") && request.getParameter("id") != null) {
            int voteforreview = Integer.parseInt(request.getParameter("id"));
            ReviewDAO reviewDAO = new ReviewDAO();
            Review review = reviewDAO.getReviewById(voteforreview);

            if (reviewDAO.voteForReview(review, customer)) {
                ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "viewBook");
                actionResponse.addParameter("isbn", review.getBook().getIsbn13());
                return actionResponse;
            } else {
                ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "viewBook");
                actionResponse.addParameter("isbn", review.getBook().getIsbn13());
                return actionResponse;
            }
        }
        return new ActionResponse(ActionResponseType.REDIRECT, "viewBook");
    }
}
