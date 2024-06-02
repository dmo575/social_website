// main JS of the register.html file

document.addEventListener("DOMContentLoaded", () => {

    const registerForm = document.querySelector("#register-form");

    registerForm.addEventListener("submit", function (event) {

        const username = event.target.elements["username"].value;
        const password = event.target.elements["password"].value;

        console.log(`submit data: ${username}, ${password}`);

        event.preventDefault();

        // check username structure
        // check password structure
        // check username availability

        fetch("./check/username/" + username)
        .then(response => {
            console.log("returning json");
            return response.json();
        })
        .then(data => {
            console.log("retrieving json's data");
            console.log(data.value);
        })
    });

    function validateUsername(username) {
        const minLength = 5;

        return username.length >= 5;
    }

});