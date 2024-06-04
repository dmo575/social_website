## WIP

This is what I am working on right now. The README is just self notes. There is no working project here, just stuff in the making. 

**The objective** is to make a social media website with the following:

### Front end server
Will serve the front end but will also live alongside the backend server (the one with the APIs).
Backend tech: Java + Spring Boot + JPA
Frontend tech: JS/HTML/CSS + Bootstrap

### Database server
Tech: SQLite

### Build tool
Maven



## NOTES -- SKIP ANYTHING BELOW THIS

Skip the notes below, random.

### Content
User can register
    Prio: WIth user and password
    Extra: Use emails for users, send out emails to confirm user email and register

User can log in with username and password

Users can post. Posts go HOME
Users can filter by tags (user can add tags to a post) when viewing HOME
Users can check out other user's profiles
Users can add others as friends
Users can accept or decline other users friends request

Users can go to FRIENDS
Users can select a friend in FRIENDS to open up a chat
Users can send and receive live chats

Users can delete posts they have made


### NOTES

TASKS:
Test bootstrap      DONE
Make logging work   DOING




### SRAMBLES:
Get a register page going. Make a submit form that sends a username and a password
    - Have a `GET /register` endpoint, returns PAGE. Check if the user is logged in, if so, then redirect to `/home`
    - PAGE will allow you to send a register form to `POST /register`
    - Whenever we unfocus the username field, send a `GET /user` to see if the user already exists. The return of this request checks a condition needed for the user to be able to send the registration form
    
Have the database in the server, live.
HOME
REGISTER
At register, we prodide credentials
if registered, redirect to HOME


### Commands:
Run the server (test) - `mvn spring-boot:run`



### Things leant:
- REMEMBER: Use incognito mode when developing, makes it easy to get rid of the cookies by just closing the window
- With Thymeleaf, the templated HTML must use a path relative to `static/`
- With JS, just by setting document.cookie to something you are setting the cookie
- ^ you can instruct the browser to get rid of the cookie by setting a past date
- ^ The browser will automatically include any valid cookies to a website on any HTTP, no need to "send the cookie", just create it.
- In JS, if you want to use the `this` keyword properly, dont use the arrow func, create the function with the `function` keyword
- READ DOCUMENTATION, just hover over document.cookie. it adds ONE cookie to the list of cookies. Only one at a time.
- If you dont add an expiration time (`expires`), it lasts until the browser is closed
- With the `path` parameter, you can specify what path the cookie belongs to (path, of a domain, paths start at index /). By default they belong to the current page (not sure if it means the whole page aka index or path, prob the first one)
- https://www.w3schools.com/js/js_cookies.asp
- 



- With slf4j, it is standard practice to have a logger instance per class.
- in app.properties, you can set logging levels you wish to print (via root, you select theminimum one to consider), or you can do it per package (meaning you can decide package A can print from INFO up while package B from WARN up)

