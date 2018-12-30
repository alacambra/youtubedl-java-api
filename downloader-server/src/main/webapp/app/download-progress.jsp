<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<script>
    console.info("assigning data");

    var serverData = "${jobId.id}";

</script>
<div>
    <div id="progress"></div>
</div>
that is progress
<div>result: ${downloadResult.exitCode}</div>
<script src="/downloader/app/js/progress.js"></script>
</body>
</html>