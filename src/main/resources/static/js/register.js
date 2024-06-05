// main JS of the register.html file

document.addEventListener("DOMContentLoaded", () => {

    const registerForm = document.querySelector("#register-form");
    const messageContainer = document.querySelector("#message-container");

    // on registration form submit event
    registerForm.addEventListener("submit", function (event) {

        // get fields
        const username = event.target.elements["username"].value;
        const password = event.target.elements["password"].value;

        event.preventDefault();

        // client-side data validation
        if(!validatePassword(password) || !validateUsername(username)) {

            // if password and username failed validation
            if(!passClean && !usernameClean) {

                printMessage("Password and username too short, min 5 characters each");
                return;
            }
            // else if username failed validation
            else if (!usernameClean) {
                printMessage("Username too short, min 5 characters");
                return;
            }
            // if password failed validation
            printMessage("Password too short, min 5 characters");
            return;
        }

        // POST to /register. Pass in JSON with username and password
        fetch("/register",{
            method: "POST",
            headers: {"Content-Type": "Application/JSON"},
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
        // analyze response
        .then(async response => {
            
            // if 201 CREATED, follow Location
            if (response.status == 201) window.location.href = response.headers.get("Location");

            // if not 201 CREATED, it means we got an error message, so we inform the user by printing it
            let text = await response.text();
            let msg = `${text}`;
            printMessage(msg);
        });
    });


    // client-side validation for username
    function validateUsername(username) {
        const minLength = 5;

        return username.length >= 5;
    }

    // client-side validation for password
    function validatePassword(password) {
        const minLength = 5;

        return password.length >= 5;
    }

    // updates the alert window, removes previous content if any.
    function printMessage(msg) {

        htmlMessage = `<h3>${msg}</h3>`;

        messageContainer.innerHTML = htmlMessage;
    }
});