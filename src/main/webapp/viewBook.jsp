<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="container">
    <h1>Book</h1>
    <c:choose>
        <c:when test="${empty book}">
            <h2>Book not found!</h2>
            <div class = "index-item"><a href="debug/list_books.jsp">List books</a></div>
        </c:when>
        <c:otherwise>
            <h2><c:out value="${book.title.name}" /></h2>
            <div>
                <ul>
                    <li>
                        <b>Authors:</b> 
                        <c:forEach items="${book.author}" var="author" varStatus="it">
                            <c:out value="${author.name}" /><c:if test="${!it.last}">, </c:if>
                        </c:forEach>
                    </li>
                    <li><b>Publisher:</b> ${book.publisher.name}</li>
                    <li><b>Published:</b> ${book.published}</li>
                    <li><b>Edition:</b> ${book.edition} (${book.binding})</li>
                    <li><b>ISBN:</b> ${book.isbn13}</li>
                    <li><b>Price:</b> ${book.price}</li>
                </ul>
            </div>
            <div>
                <c:out value="${book.description}" />
            </div>
            <div>
                <form action="addBookToCart.do" method="post">
                    <input type="hidden" name="isbn" value="${book.isbn13}" />
                    <input type="text" name="quantity" value="1" />
                    <input type="submit" value="Add to cart" />
                </form>
            </div>
            <div>
            <c:choose>
                <c:when test="${empty book.reviews}">
                    <div>No reviews for this book!</div>
                </c:when>
                <c:otherwise>
                    <div>
                        <div><h3>Reviews:</h3></div>
                        <c:forEach items="${book.reviews}" var="reviews" varStatus="it">
                            <div>
                            <a href="voteReview.do?id=${reviews.id}"><img style="height:16px" src="<c:url value="/img/upvote.png" />" /></a>
                            <strong><c:out value="${reviews.author.name}" /> wrote (<c:out value="${reviews.score}" /> votes):</strong>
                            </div>
                            <div>
                                <em><c:out value="${reviews.content}" /></em>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
            <div>
                <h3>Write a review:</h3>
                <form method="post" action="postReview.do">
                    <textarea rows="4" cols="50" placeholder="Write your review here!" name="content"></textarea>
                    <input type="hidden" name="isbn" value="${book.isbn13}" />
                    <input type="submit" value="Submit" />
                </form>
            </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>