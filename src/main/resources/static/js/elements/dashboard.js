// given an array of dashboard buttons, returns a dashboard
export function createDashboard(buttonsArr) {

    // create dashboard
    const dashboard = document.createElement(`div`);
    // ...

    // populate it with buttons
    for(let i in buttonsArr) {
        dashboard.appendChild(buttonsArr[i]);
    }

    return dashboard;
}

// returns a dashboard button, set up to be added to a dashboard
export function createDashboardBtn(name, icon, onClick) {
    var buttonContainer = document.createElement('div');
    var button = document.createElement('button');

    button.textContent = name;
    button.onclick = onClick;


    buttonContainer.appendChild(button);

    return buttonContainer;
}

// directs the client to a relative path
// helper function to be used with createButton's onClick
export function goto(path) {
    window.location.href = path;
}