<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<h1>${helloHelper.message}</h1>
<script>
    console.info("assigning data");

    <%--var jobId =--%>
        <%--${downloadResult.json.toString()};--%>

</script>
<div>
    <form method="POST" action="">
        <label for="url">Url:</label>
        <input type="text" name="url" id="url"/><br>
        <label for="extractAudio">only audio</label>
        <input type="checkbox" name="extractAudio" id="extractAudio"/>
        <br/>
        <button type="submit">Send</button>
    </form>
</div>
<script src="/downloader/app/js/app.js"></script>
<h3><a target="_blank" href="${jobId.location}">Your download: ${jobId.id}</a></h3>
</body>
</html>