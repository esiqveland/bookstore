<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="container">
    <h1>Book Listing</h1>
    <c:choose>
        <c:when test="${empty booklist}">
            <div>Invalid booklist</div>
        </c:when>
        <c:otherwise>
                <h2><c:out value="${booklist.title}" /></h2>
                <h3><c:out value="${booklist.description}" /></h3>
                <c:forEach items="${booklist.books}" var="book">
                    <h3><a href="viewBook?isbn=<c:out value="${book.isbn13}" />"><c:out value="${book.title}" /></a></h3>
                    <div>Price: <c:out value="${book.price}" /></div>
                </c:forEach>
                <br />
        </c:otherwise>
    </c:choose>
</div>