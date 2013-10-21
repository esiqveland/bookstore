<div class="container">
    <h1>Create booklist</h1>
        <div class="general-form">
            <form action="createBooklist.do" method="post">
                <table class="general-table">
                    <tr>
                        <td>
                            <label for="title">Title</label>
                        </td>
                        <td>
                            <input id="title" name="title" type="text" placeholder="Enter booklist title" />
                        </td>
                    </tr>
                    <tr>
                        <td><label for="description">Description</label></td>
                        <td><textarea id="description" name="description" placeholder="Write whatever you want!"></textarea></td>
                    </tr>
                </table>
                <div>
                    <input type="submit" value="Submit">
                </div>
            </form>
        </div>
</div>
