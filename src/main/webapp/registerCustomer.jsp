<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<div class="container">
    <h1>Register</h1>
    <c:choose>
        <c:when test="${empty sessionScope.customer}">
            <c:choose>
                <c:when test="${not empty register_success}">
                    <div>
                        You've successfully created a user. Maybe you should <a href="loginCustomer.do">sign in</a>?
                    </div>
                </c:when>
                <c:otherwise>
                    <c:if test="${not empty register_error}">
                        <div>
                            ${register_error}
                        </div>
                    </c:if>
                    <div class="general-form">
                        <form action="registerCustomer.do" method="post">
                            <table class="general-table">
                                <tr>
                                    <td><label for="email">Email</label></td>
                                    <td><input id="email" name="email" type="text" /></td>
                                </tr>
                                <tr>
                                    <td><label for="name">Name</label></td>
                                    <td><input id="name" name="name" type="text" /></td>
                                </tr>
                                <tr>
                                    <td><label for="password">Password</label></td>
                                    <td><input id="password" name="password" type="password" autocomplete="off" /></td>
                                </tr>
                                <tr>
                                <td>
                                </td>
                                <td>
                                    <%
                                      ReCaptcha c = ReCaptchaFactory.newReCaptcha("6Lc8QOkSAAAAAHI1R4dDPsAQn3Fu_Ap618Y8POYE",
                                       "6Lc8QOkSAAAAABqBZdDu8ksk95Ew57Xacipc4F-w", false);
                                      out.print(c.createRecaptchaHtml(null, null));
                                    %>
                                </td>
                            </table>

                            <div><input type="submit" value="Submit"></div>
                        </form>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <div>
                You're already logged in. Now, don't be greedy, one account should be enough.
            </div>
        </c:otherwise>
    </c:choose>
</div>