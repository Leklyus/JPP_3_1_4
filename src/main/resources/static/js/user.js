document.addEventListener("DOMContentLoaded", function () {

    fetch("/user/currentUser")
        .then(response => response.json())
        .then(user => {
            document.getElementById("idInfo").textContent = user.id;
            document.getElementById("firstnameInfo").textContent = user.firstname;
            document.getElementById("lastnameInfo").textContent = user.lastname;
            document.getElementById("ageInfo").textContent = user.age;
            document.getElementById("emailInfo").textContent = user.email;

            let roles = user.roles.map(role => role.name).join(" ");
            document.getElementById("roleInfo").textContent = roles;
            //nav nav-tabs
            document.getElementById("currentUserEmail").textContent = user.email;
            document.getElementById("currentUserRoles").textContent = roles;
        })
        .catch(error => console.error("Error fetching user data:", error));
});