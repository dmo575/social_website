## How to construct a template
Templates and their JS scripts attached must follow certain rules in order to peroperly work.

### Configuring the HTML:
- You can include ```<script>``` tags that reference script files.
- Inline scripts are not supported

### Configuring the JS script files:
- Make sure to include an init() function. Treat it as the entry point of the script.
- Make sure you call the init() once in the global scope. (working to remove this)
- Make sure you also use the global scope to subscribe to a view event at the document level. The event name goes by the following convention:
    - ```[VIEW_PATH]-view-event```
    - If you return the view from a GET request to **/register**, this is the event name:
        ```/register-view-event```

Example from the ```/login``` view:

```
init();
document.addEventListener("/login-view-event", (event) => {
    init();
});

function init () {
    
    const loginForm = document.querySelector("#login-form");
    const messageContainer = document.querySelector(`#message-container`);

    // any other code I want to run every time I fetch the view
    // . . .
}
```

Any code in the global scope will only run once on the first fetch, so any references to view objects there will be useless once that view gets destroyed and recalled.

For more info, read the comments inside [**src/main/resources/static/js/tools.js**](./../static/js/tools/tools.js)