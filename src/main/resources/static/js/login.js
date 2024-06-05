// main JS of the login.html file

document.addEventListener("DOMContentLoaded", () => {

    const registerForm = document.querySelector("#login-form");
    const messageContainer = document.querySelector("#message-container");

    // on login form submit event
    registerForm.addEventListener("submit", function (event) {

        // get fields
        const username = event.target.elements["username"].value;
        const password = event.target.elements["password"].value;

        event.preventDefault();

        // POST to /login. Pass in JSON with username and password
        fetch("/login",{
            method: "POST",
            headers: {"Content-Type": "Application/JSON"},
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
        // analyze response
        .then(async response => {
            
            // if 200 OK, redirect to home
            if (response.status == 200) window.location.href = response.headers.get("Location");

            // if not 200 OK, inform user
            let text = await response.text();
            let msg = `Error: ${text}`;
            printMessage(msg);
        });
    });

    // updates the alert window, removes previous content if any.
    function printMessage(msg) {

        htmlMessage = `<h3>${msg}</h3>`;

        messageContainer.innerHTML = htmlMessage;
    }
});