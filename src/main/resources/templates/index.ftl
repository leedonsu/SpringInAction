<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
</head>
<body>
    <h1>@RequestPart</h1>
    <form method="post" action="/upload-requestPart" enctype="multipart/form-data">
        <input type="file" name="file">
        <button type="submit">업로드</button>
    </form>
    <br/>
    <h1>MultipartFile</h1>
    <form method="post" action="/upload-MultipartFile" enctype="multipart/form-data">
        <input type="file" name="file">
        <button type="submit">업로드</button>
    </form>
</body>
</html>