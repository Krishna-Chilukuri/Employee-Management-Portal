<html>
<head>
    <script src="../webapp/WEB-INF/logic/addEmployee.js"></script>
</head>
<body>
<h2>Hello World! kaama rao</h2>
<div class="buttonContainer">
    <button type="button" onclick="location.href='/pages/addEmployee.jsp'">Add Employee</button>
</div>
<form action="AddEmployeeServlet" method="post">
    <label for="eid">Enter ID</label>
    <input type="number" name="eid" id="eid">
    <label for="ename">Enter Name</label>
    <input type="text" name="ename" id="ename">
    <label for="erank">Enter Rank</label>
    <input type="text" name="erank" id="erank">
    <button type="submit">Submit</button>
</form>
<br><br><br>
<form action="ViewAllEmployees" method="post">
    <button type="submit">View All Employees</button>
</form>
<form action="RemoveEmployeeServlet" method="post">
    <label for="remid">Enter ID</label>
    <input type="number" name="remid" id="remid">
    <button type="submit">Remove Employee</button>
</form>
<br><br>
<form action="ViewEmployeeServlet" method="post">
    <label for="viewId">Enter ID</label>
    <input type="number" name="viewId" id="viewId">
    <button type="submit">View Employee</button>
</form>
</body>
</html>
