## WIP

Hi! This is what I am working on right now.

**The objective** is to make a social media website with the following:

- **Front end server**: Will serve the front end but will also live alongside the backend server (the one with the APIs).
- **Backend tech**: Java + Spring Boot + JPA
- **Frontend tech**: JS/HTML/CSS + Bootstrap
- **Database server**: SQLite
- **Build tool**: Maven

### Current structure:

```
                              Client
                         +-------------+
                         | HTML/CSS/JS |
                         +-------------+

              Server 1 (Frontend + Backend APIs)
+------------------------------------------------------------------+
|                     Controllers : HTTP                           |
+--------------------------+---------------------------------------+
| Service : Business logic | Auth : Authentication & Authorization |
+--------------------------+---------------------------------------+
|                    DAO : CRUD operations                         |
+------------------------------------------------------------------+

                            Server 2
                      +-------------------+
                      | DB : Data storage |
                      +-------------------+
```

### Solutions
- **Hashing**: bcrypt
- **Random gen**: commons-lang3

### Controllers

**AccessController:**

These provide the registration and loggin interface.

- /register
    - GET
        - if session: redirect to /
        - no session: serve /register
    - POST
        - if session: skip and redirect to /
        - no session: try to register
            - if success: 200 OK - return JSON + Location header to /
            - no success: 40X - return string with error description

- /login
    - GET
        - if session: redirect to /
        - no session: serve /login
    - POST
        - if session: skip and redirect to /
        - no session: try to login
            - if success: 200 OK - return JSON + Location header to /
            - no success: 40X - return string with error description

- /
    - GET
        - if session: serve /home
        - no session: serve /welcome

### Sequrity
I have a couple of things in place but they are basic and I'm not using Spring Security on the project since that's something that I haven't got a chance to learn yet.

These are the basic features I have implemented that I consider security-themed:
- **Authentication**: I have a class that handles basic authenticationg of users and sessions.
- **Hasing passwords**: I make sure I just store a hash to a password.
- **Expirable sessionIds**: Session IDs have an expiration date, so no loggin in once and stayed logged in forever.
- **Updating sessionIds**: I create a new sessionId on every contact with a user. This opens up a potential for optimization. Maybe have two timers, one for sessionId expiration and another substantially smaller timer to indicate that the sessionId should be refreshed.
- **Sanitating userame/password characters**: I do some basic sanitation at the service layer, before interacting with the DAO. Right now is just a list of illegal characters that I think should not get to the DAO level. With username/password what I do is to throw an exception that lets the client know the issue.

**TODO**:

- Adding some authorization so I can have admin accounts.
- Improve sanitation.
- HTTPS ?

### Optmization
Similar situation as with security.

These are some things implemented that I consider optimization-themed:
- **Client-side input validation**: saves time to the server. I do check the same things in the server again because I imagine the client is not to be trusted. But for non bad actors this saves the potential back and forth with the server. Something I have to work on for this feature is having a cenrtalized place for these rules. Right now if I want a password to be minimum 8 characters, I have to specify that rule on both the server and the client separately, so mismatches can happen.

- 

**TODO**:

- Optimize sessionId refresh rules.


## FROM HERE ON OUT THESE ARE JUST RANDOM OUTDATED NOTES ! ! !

### Registration

- Prio: WIth user and password
- Would be nice: Use emails for users, send out emails to confirm user email and register

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


### Tasks

TASKS:
Test bootstrap      DONE
Make logging work   DONE





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

