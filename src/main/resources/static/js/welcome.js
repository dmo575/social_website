import { selectTab } from "./elements/dashboard.js";
import { loadView } from "./tools/tools.js";



document.addEventListener("DOMContentLoaded", () => {

    const bodyContainer = document.querySelector(`#body-container`);
    const messageContainer = document.querySelector(`#message-container`);
    const tabWelcome = document.querySelector(`#tab-welcome`);
    const tabWelcomeEvent = new CustomEvent("tab-welcome");
    const tabLogin = document.querySelector(`#tab-login`);
    const tabLoginEvent = new CustomEvent("tab-login");
    const tabRegister = document.querySelector(`#tab-register`);
    const tabRegisterEvent = new CustomEvent("tab-register");
    const tabContact = document.querySelector(`#tab-contact`);
    const tabContactEvent = new CustomEvent("tab-contact");
    const tabs = [tabWelcome, tabLogin, tabRegister, tabContact];

    tabWelcome.addEventListener("click", onClickTabWelcome);
    tabLogin.addEventListener("click", onClickTabLogin);
    tabRegister.addEventListener("click", onClickTabRegister);
    tabContact.addEventListener("click", onClickTabContact);
    
    function onClickTabWelcome(event) {
        if(!selectTab(event.target.parentNode, tabs)) return;

        console.log("tab welcome");
    }

    function onClickTabLogin(event) {
        if(!selectTab(event.target.parentNode, tabs)) return;

        fetch("/login")
        .then(response => {
            if (response.ok) return response.text();
        })
        .then(loginView => {
            loadView(bodyContainer, loginView, tabLoginEvent);
        });
    }

    function onClickTabRegister(event) {
        if(!selectTab(event.target.parentNode, tabs)) return;

        fetch("/register")
        .then(response => {
            if (response.ok) return response.text();
        })
        .then(registerView => {
            loadView(bodyContainer, registerView, tabRegisterEvent);
        });

    }

    function onClickTabContact(event) {
        if(!selectTab(event.target.parentNode, tabs)) return;

        console.log("tab contact");
    }

});