// main JS of the register.html file

document.addEventListener("DOMContentLoaded", () => {

    const registerForm = document.querySelector("#register-form");
    const messageContainer = document.querySelector("#message-container");

    registerForm.addEventListener("submit", function (event) {

        const username = event.target.elements["username"].value;
        const password = event.target.elements["password"].value;

        console.log(`submit data: ${username}, ${password}`);

        event.preventDefault();

        const passClean = validatePassword(password);
        const usernameClean = validateUsername(username);


        // client side data validation check
        if(!passClean || !usernameClean) {

            if(!passClean && !usernameClean) {

                printMessage("Password and username too short, min 5 characters each");
                return;
            }
            else if (!usernameClean) {
                printMessage("Username too short, min 5 characters");
                return;
            }

            printMessage("Password too short, min 5 characters");

            return;
        }

        // send POST-/register, 
        fetch("/register",{
            method: "POST",
            headers: {"Content-Type": "Application/JSON"},
            body: JSON.stringify({
                name: username,
                password: password
            })
        })
        .then(async response => {
            
            // if 201-CREATED, redirect
            if (response.status == 201) {
                console.log("redirecting...");
                window.location.href = "http://localhost:8080/home"
            }

            // else, send message with error text

            let text = await response.text();
            let msg = `Error: ${text}`;
            printMessage(msg);
        });
    });


    
    function validateUsername(username) {
        const minLength = 5;

        return username.length >= 5;
    }

    function validatePassword(password) {
        const minLength = 5;

        return password.length >= 5;
    }

    function printMessage(msg) {

        htmlMessage = `<h3>${msg}</h3>`;

        messageContainer.innerHTML = htmlMessage;
    }


});