<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
          integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
</head>
<style>
    form {
        display: flex;
        justify-content: right;
        align-items: flex-start;
        flex-direction: column;
        font-size: 2em;
    }

</style>
<body>
<h1>${helloHelper.message}</h1>
<script>
</script>
<div>
    <form method="POST" action="">
        <div class="input-group mb-3">
            <div class="input-group-append">
                <div class="input-group-text">
                    <span type="text">URL</span>
                </div>
            </div>
            <input type="text" class="form-control" name="url" id="url"/><br>
        </div>

        <div class="input-group mb-3">
            <div class="input-group-append">
                <div class="input-group-text">
                    <input type="checkbox" name="extractAudio" id="extractAudio">
                </div>
            </div>
            <span type="text" class="form-control">Només so</span>
        </div>

        <label for="ownerLabel">Destinatari:</label>
        <div id="ownerLabel">
            <div class="input-group">
                <div class="input-group-prepend">
                    <div class="input-group-text">
                        <input type="radio" name="owner" value="albert" checked>
                    </div>
                </div>
                <span type="text" class="form-control">Albert</span>
            </div>
            <div class="input-group">
                <div class="input-group-prepend">
                    <div class="input-group-text">
                        <input type="radio" name="owner" value="ruth">
                    </div>
                </div>
                <span type="text" class="form-control">Ruth</span>
            </div>
            <div class="input-group">
                <div class="input-group-prepend">
                    <div class="input-group-text">
                        <input type="radio" name="owner" value="esther">
                    </div>
                </div>
                <span type="text" class="form-control">Esther</span>
            </div>
            <div class="input-group">
                <div class="input-group-prepend">
                    <div class="input-group-text">
                        <input type="radio" name="owner" value="hannah">
                    </div>
                </div>
                <span type="text" class="form-control">Hannah</span>
            </div>
            <div class="input-group">
                <div class="input-group-prepend">
                    <div class="input-group-text">
                        <input type="radio" name="owner" value="tots">
                    </div>
                </div>
                <span type="text" class="form-control">Tots</span>
            </div>
        </div>
        <button class="btn btn-outline-secondary" type="submit">Send</button>
    </form>
</div>
<script src="/downloader/app/js/app.js"></script>

<c:if test="${jobId.id!= null}">
    <h3><a target="_blank" href="${jobId.location}">Your download job: ${jobId.id}</a></h3>
</c:if>
</body>
</html>