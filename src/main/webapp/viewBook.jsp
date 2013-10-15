<div class="container">
    <h1>Book</h1>
    <c:choose>
        <c:when test="${empty book}">
            <h2>Book not found!</h2>
            <div class = "index-item"><a href="debug/list_books.jsp">List books</a></div>
        </c:when>
        <c:otherwise>
            <h2>${book.title.name}</h2>
            <div>
                <ul>
                    <li>
                        <b>Authors:</b> 
                        <c:forEach items="${book.author}" var="author" varStatus="it">
                            ${author.name}<c:if test="${!it.last}">, </c:if>
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
                ${book.description}
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
                    No reviews for this book!
                </c:when>
                <c:otherwise>
                    <h3>Reviews:</h3>
                    <c:forEach items="${book.reviews}" var="reviews" varStatus="it">
                        <strong>${reviews.author.name} wrote:</strong>
                        <div>
                            <em>${reviews.content}</em>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </div>
        </c:otherwise>
    </c:choose>
</div>