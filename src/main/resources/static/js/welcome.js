import { createDashBoard } from './dashboard.js';


document.addEventListener("DOMContentLoaded", () => {

    // initiates an async operation that returns the dashboard
    createDashBoard(["guest", "general"])
    .then(dashboard => {
        // append resulting dashboard to the page
        document.body.appendChild(dashboard);
    })
    .catch(err => {
        console.error(`Error while creating dashboard -> ${err}`);
    });

    // continue welcome's page generation

});