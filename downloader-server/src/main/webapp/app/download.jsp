<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
          integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
</head>
<body>
<h1>${helloHelper.message}</h1>
<script>
    console.info("assigning data");
</script>
<div>
    <form method="POST" action="">
        <label for="url">Url:</label>
        <input type="text" name="url" id="url"/><br>
        <label for="extractAudio">Només so</label>
        <input type="checkbox" name="extractAudio" id="extractAudio"/>
        <br/>

        <label for="ownerLabel">Destinatari:</label>
        <div id="ownerLabel">
            <input type="radio" name="owner" value="albert" checked>Albert<br/>
            <input type="radio" name="owner" value="ruth">Ruth<br/>
            <input type="radio" name="owner" value="esther">Esther<br/>
            <input type="radio" name="owner" value="hannah">Hannah<br/>
            <input type="radio" name="owner" value="tots">Tots
        </div>
        <br/>
        <button type="submit">Send</button>
    </form>
</div>
<script src="/downloader/app/js/app.js"></script>

<c:if test="${jobId.id!= null}">
    <h3><a target="_blank" href="${jobId.location}">Your download job: ${jobId.id}</a></h3>
</c:if>
</body>
</html>