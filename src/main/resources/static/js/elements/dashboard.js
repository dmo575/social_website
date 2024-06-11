// returns false if the tab is already selected
export function selectTab(tab, tabs) {

    // if tab is already selected, return false
    if(tab.classList.contains("is-active"))
        return false;

    // remove selection highlight from all tabs
    for(let tabIndex in tabs) {
        tabs[tabIndex].classList.remove("is-active");
    }

    // add selection highlight to tab
    tab.classList.add("is-active");

    return true;
}