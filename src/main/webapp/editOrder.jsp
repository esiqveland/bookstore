<div class="container">
    <h1>Edit Order</h1>
    <c:choose>
        <c:when test="${empty cart.items}">
            <div>No items in shopping cart.</div>
        </c:when>
        <c:otherwise>
            <form action="editOrder.do?id=${orderid}" method="post">
                <c:forEach items="${cart.items}" var="item">
                    <h3><c:out value="${item.value.book.title.name}" /></h3>
                    <div>Price: ${item.value.book.price}</div>
                    <div> Quantity:
                        <input type="text" name="${item.value.book.isbn13}" value="${item.value.quantity}" />
                    </div>
                </c:forEach>
                <br />
                <input type="submit" value="Cancel and remake order" />
            </form>
            <c:choose>
                <c:when test="${cart.numberOfItems == 1}">
                    <div>Subtotal: ${cart.subtotal}</div>
                </c:when>
                <c:otherwise>
                    <div>Subtotal (${cart.numberOfItems} items): ${cart.subtotal}</div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</div>