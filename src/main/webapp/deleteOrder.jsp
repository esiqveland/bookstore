<div class="container">
    <h1>Delete Order</h1>
    <div>Do you want to delete the following Order?</div>
    <pre><c:out value="${order.address.address}" /></pre>
    <form action="deleteOrder.do" method="post">
        <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="error">${message}</span>
                </div>
            </c:forEach>
        </c:if>
        <input name="id" value="${order.id}" type="hidden" />
        <div><input type="submit" value="Confirm" /></div>
    </form>
</div>