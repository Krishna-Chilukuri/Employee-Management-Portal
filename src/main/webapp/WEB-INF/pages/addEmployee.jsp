<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Employee</title>
    <script src="../logic/addEmployee.js"></script>
</head>
<body>
    <form onsubmit="saveEmployee()" method="post">
        <label for="eid">Enter ID</label>
        <input type="number" name="eid" id="eid">
        <label for="ename">Enter Name</label>
        <input type="text" name="ename" id="ename">
        <label for="erank">Enter Rank</label>
        <input type="text" name="erank" id="erank">
        <button type="submit">Submit</button>
    </form>
</body>
</html>