// main JS of the register.html file

document.addEventListener("DOMContentLoaded", () => {

    // get the registration form
    // stop the submittion, add a cookie and submit.

    console.log("cookie: " + document.cookie);

    const registerForm = document.querySelector("#registration-form");

    registerForm.addEventListener("submit", function (event) {

        event.preventDefault();

        document.cookie = "somevalue=hi there; expires=Thu, 18 Dec 2025 12:00:00 UTC path=/;";
        document.cookie = "somevalue2=dsdsd; expires=Thu, 18 Dec 2025 12:00:00 UTC path=/;";
        document.cookie = "somevalue3=ggg; expires=Thu, 18 Dec 2025 12:00:00 UTC path=/;";
        document.cookie = "somevalue=XXXXXX; expires=Thu, 18 Dec 2025 12:00:00 UTC path=/;";

        console.log("CLIENT: " + document.cookie);

        //this.submit();
    });

});