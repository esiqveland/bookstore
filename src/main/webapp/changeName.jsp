<div class="container">
<h1>Change name</h1>
    <div>Current name: <c:out value="${customer.name}" /></div>
    <form action="changeName.do" method="post">
        <div>
            <label for="name">New name</label> 
            <input type="text" name="name" />
            <c:if test="${not empty messages.name}">
                <span class="error"><c:out value="${messages.name}" /></span>
            </c:if>
        </div>
        <div><input type="submit" value="Submit" /></div>
    </form>
</div>