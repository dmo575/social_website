// main JS of the register.html file

document.addEventListener("DOMContentLoaded", () => {

    // get the registration form
    // stop the submittion, add a cookie and submit.

    console.log("cookie: " + document.cookie);

    const registerForm = document.querySelector("#registration-form");

    registerForm.addEventListener("submit", function (event) {

        event.preventDefault();

        document.cookie = "username= Testman; expires=Thu, 18 Dec 2025 12:00:00 UTC path=/";

        this.submit();
    });

});