
/* 
 * The following explais the reasons behind the function "loadView":
 * 
 * This function is important because as far as I understand, the following is true:
 * - When adding HTML to the document via "element.innerHTML = someHTMLString", the client
 * does not scan that HTML for <script> tags to execute, so it doesn't run any script code
 * nor does it requests to the server any script that we might need.
 * - When adding a <script> element via "createElement" and "appendChild", the client does
 * read the script element and acts accordingly.
 * - When a script is added, the client only runs it once. Meaning that any global scope
 * variable and function calls will ever run once when the script is first added.
 * - Any attempt to load that same script again won't do anything, as the client seems to understand
 * that the script has already been fetched, which is fine but it also means that if you depend
 * on the script running its global calls every time you add it, you need to find a way
 * around that.
 * - When subscribing to an element's event, even if you use an ID, that subscription only
 * applies to that element, so if you delete the element and add an exact copy of it to the
 * document, wathever listeners you had won't work once the even triggers on the copy because
 * they are considered different objects
 * 
 * Also, some preferences I wanted for this:
 * - I don't want to have to manually call for the script on top of calling for the view, I want
 * to call for the view and have that automatically take care of properly loading any script
 * element that I define in the view. I don't want the client to have to worry about not only which
 * view to fetch but also which scripts that view needs. For that I need a way that allows me to
 * define the scripts I need in the view file and have it work.
 * 
 * The solution:
 * - Because I want to have the view declare the scripts that it needs, I use the <script> tag in
 * the view and let the client figure out a way to properly run it (which is the method below)
 * - Because when a script is loaded into the client it only ever runs once, I structure my view
 * scripts so that on that global scope I subscribe to a custom event to be triggered each time I
 * want the script to run. I run an init() function on event trigger.
 * - I also call that init() in the global scope because the first run of the script the event
 * doesn't trigger. I imagine because by the time I first dispatch the event the script is still
 * being downloaded by the browser. So it goes like this (assuming it is the first time the
 * script is being requested):
 *      > Browser understands it needs a new script, so initiates a fetch for it, which is async
 *      > Next line dispatched the "view loaded" event, but there is nobody to listen yet
 *      > The script finishes loading, so the browser runs it once. There it subs to the event
 *        and it also runs the init().
 * - I make sure I trigger the event after the following:
 *      > First, load the HTML
 *      > Second, add the <script> tags via "createElement" and "appendChild". This is what
 *          triggers the first run of the script (if nt previously added to the client)
 *          which allows me to subscribe the script's init function to the "view loaded" event.
 *      > Third, dispatch the "view loaded" event. This only triggers from the second time on,
 *          so I need to remember to call the init() for each script on the global scope once.
 * 
 * Pros:
 * - If I follow the setup, I don't have to worry about my view being recalled and my scripts
 * breaking because of that.
 * 
 * Cons:
 * - When loading a view, I have to not only load the view but also make a custom event, I
 * use the same naming convention for events based on the view name so I don't have to
 * keep track of the event name. Less than ideal but it works. (This could be automated)
 * - I need to remember that if I want my view script to properly work I need to do the whole
 * init() plus event and subscription set up and make sure I don't use the global scope
 * for things that I need to do every time the view enters the picture.
 * 
 */

/**
 * Loads a view's HTML into the document and fires its scripts.
 * 
 * @param {HTMLElement} viewParent - the element to which we should copy the HTML into
 * @param {String} view - the HTML view content
 * @param {CustomEvent} viewPath - the path of the view
 * @param {Boolean} addToParent - whether if the HTML should be added to the existing one or not
 */
export function loadView(viewParent, view, viewPath, addToParent=false) {

    // adds the view HTML to the parent container
    viewParent.innerHTML = addToParent ? viewParent.innerHTML + view : view;

    // selects all of the view's <script> tags
    const viewScripts = viewParent.querySelectorAll("script");

    // creates an element per <script> and adds it to the document
    viewScripts.forEach(script => {
        const scriptElement = document.createElement("script");
        scriptElement.type = script.type;
        scriptElement.src = script.src;

        document.body.appendChild(scriptElement);
    });

    // lets any viewEvent listeners know that the view and its scripts have been loaded
    document.dispatchEvent(new CustomEvent(`${viewPath}-view-event`));
}