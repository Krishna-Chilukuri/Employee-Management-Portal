<html>
<head>
    <script src="../webapp/WEB-INF/logic/addEmployee.js"></script>
</head>
<body>
<h2>Hello KEPESHHHH! kaama rao</h2>
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
<form action="ViewAllEmployees" method="get">
    <button type="submit">View All Employees</button>
</form>
<form action="RemoveEmployeeServlet" method="post">
    <label for="remid">Enter ID</label>
    <input type="number" name="remid" id="remid">
    <button type="submit">Remove Employee</button>
</form>
<br><br>
<form action="ViewEmployeeServlet" method="get">
    <label for="viewId">Enter ID</label>
    <input type="number" name="viewId" id="viewId">
    <button type="submit">View Employee</button>
</form>
<form action="UpperHierarchy" method="get">
    <label for="eid">Enter ID</label>
    <input type="number" name="eid" id="eid">
    <button type="submit">View Upper Hierarchy</button>
</form>
<form action="LowerHierarchy" method="get">
    <label for="eid">Enter ID</label>
    <input type="number" name="eid" id="eid">
    <button type="submit">View Lower Hierarchy</button>
</form>
<form action="KStepHierarchy" method="get">
    <label for="eid">Enter ID</label>
    <input type="number" name="eid" id="eid">
    <label for="kval">Enter k</label>
    <input type="number" name="kval" id="kval">
    <button type="submit">View K Step Hierarchy</button>
</form>
<form action="PromoteEmployee" method="post">
    <label for="eid">Enter ID to promote</label>
    <input type="number" name="eid" id="eid">
    <label for="kval">Enter k</label>
    <input type="number" name="kval" id="kval">
    <button type="submit">Promote Employee</button>
</form>
<form action="DemoteEmployee" method="post">
    <label for="eid">Enter ID to demote</label>
    <input type="number" name="eid" id="eid">
    <label for="kval">Enter k</label>
    <input type="number" name="kval" id="kval">
    <button type="submit">Demote Employee</button>
</form>
<form action="MultiThreadInsertions" method="post">
    <label for="countOfEmps">Enter number of Employees to insert</label>
    <input type="number" name="countOfEmps" id="countOfEmps">
    <button type="submit">Insert Employees</button>
</form>
<form action="LoopingInsertions" method="post">
    <label for="countOfEmps">Enter number of Employees to insert</label>
    <input type="number" name="countOfEmps" id="countOfEmps">
    <button type="submit">Insert Employees</button>
</form>
</body>
</html>
