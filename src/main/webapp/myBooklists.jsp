<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="container">
    <h1>Book lists</h1>
    <c:choose>
        <c:when test="${empty booklists}">
            <h2>No booklists found!</h2>
            Create one?
            <div class = "index-item"><a href="debug/list_books.jsp">List books</a></div>
        </c:when>
        <c:otherwise>
            <div>
                <ul>
                    <c:forEach items="${booklists}" var="booklist" varStatus="it">
                    <li>
                            <a href="viewBooklist.do?id=<c:out value="${booklist.id}" />"><c:out value="${booklist.title}" /></a>
                    </li>
                    </c:forEach>
                </ul>
            </div>
        </c:otherwise>
    </c:choose>
</div>