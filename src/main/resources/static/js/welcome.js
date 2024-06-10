import { createDashboard, createDashboardBtn, goto } from "./elements/dashboard.js";

var bodyContainer = null;
var tabWelcome = null;
var tabLogin = null;
var tabRegister = null;
var tabContact = null;
var tabs = null;

document.addEventListener("DOMContentLoaded", () => {

    bodyContainer = document.querySelector(`#body-container`);

    tabWelcome = document.querySelector(`#tab-welcome`);
    tabWelcome.addEventListener("click", onClickTabWelcome);

    tabLogin = document.querySelector(`#tab-login`);
    tabLogin.addEventListener("click", onClickTabLogin);

    tabRegister = document.querySelector(`#tab-register`);
    tabRegister.addEventListener("click", onClickTabRegister);

    tabContact = document.querySelector(`#tab-contact`);
    tabContact.addEventListener("click", onClickTabContact);

    tabs = [tabWelcome, tabLogin, tabRegister, tabContact];

});

// returns false if the tab is already selected
function selectTab(tab) {

    // if tab is already selected, return false
    if(tab.classList.contains("is-active"))
        return false;

    // remove selection highlight from all tabs
    removeActiveTab();

    // add selection highlight to tab
    tab.classList.add("is-active");

    // replace body-container content with a loading visual
    bodyContainer.innerHTML = "LOADING";

    return true;
}

// des-select all tabs (visual)
function removeActiveTab() {

    for(let tabIndex in tabs) {
        tabs[tabIndex].classList.remove("is-active");
    }
}

function onClickTabWelcome(event) {
    if(!selectTab(event.target.parentNode)) return;

    console.log("tab welcome");
}

function onClickTabLogin(event) {
    if(!selectTab(event.target.parentNode)) return;

    console.log("tab login");

    fetch("/login")
    // analyze response
    .then(response => {
        
        if (response.ok) return response.text();
    })
    .then(loginFormHtml => {
        bodyContainer.innerHTML = loginFormHtml;
        addScriptToDOM("./js/login.js");
    });
}

function onClickTabRegister(event) {
    if(!selectTab(event.target.parentNode)) return;

    console.log("tab register");
}

function onClickTabContact(event) {
    if(!selectTab(event.target.parentNode)) return;

    console.log("tab contact");
}

function addScriptToDOM(src) {
    const script = document.createElement('script');
    script.src = src;
    script.defer = true;
    script.type = "module";
    document.body.appendChild(script);
}