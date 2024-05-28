<html>
<head>
    <script src="../webapp/WEB-INF/logic/addEmployee.js"></script>
</head>
<body onload="checkCookie()">
<h2>Hello KEPESHHHH! kaama rao</h2>
<form action="Login" method="post">
    <label for="username">Enter Username</label>
    <input type="text" name="username" id="username" required>
    <label for="userpassword">Enter Password</label>
    <input type="password" name="userpassword" id="userpassword" required>
    <button type="submit">Login</button>
</form>
<script>
    function getCookie(cname) {
        let name = cname + "=";
        let ca = document.cookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }
    function checkCookie() {
        let user = getCookie("user");
        if (user != "") {
            alert("Welcome Back : " + user);
            window.location.href = "";
        }
        else {
            alert("No user present....LOGIN");
            // window.location.href = "http://localhost:8080/untitled2/index.jsp";
        }
    }
</script>
</body>
</html>
