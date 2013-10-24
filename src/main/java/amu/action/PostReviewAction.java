package amu.action;

import amu.common.AmuDariaUtils;
import amu.database.ReviewDAO;
import amu.model.Customer;
import amu.model.Review;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PostReviewAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!request.getMethod().equals("POST")) {
            return new ActionResponse(ActionResponseType.FORWARD, "index");
        }

        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "postReview");
            return actionResponse;
        }

        String remoteAddr = request.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("6Lc8QOkSAAAAABqBZdDu8ksk95Ew57Xacipc4F-w");

        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

        if (!reCaptchaResponse.isValid()) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "viewBook");
            actionResponse.addParameter("isbn", request.getParameter("isbn"));
            return actionResponse;
        }

        String reviewContent = AmuDariaUtils.sanitizeUserHtml(request.getParameter("content"));

        String isbn13 = request.getParameter("isbn");

        ReviewDAO reviewDAO = new ReviewDAO();
        Review review = reviewDAO.createReview(isbn13, customer, reviewContent);

        ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "viewBook");

        if(createReviewSuccessful(review)) {
            actionResponse.addParameter("isbn", isbn13);
        }

        return actionResponse;
    }

    private boolean createReviewSuccessful(Review review) {
        if(review != null && review.getBook() != null) {
            return true;
        }
        return false;
    }

}
