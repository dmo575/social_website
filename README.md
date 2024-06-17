## WIP

Hi! This is what I am working on right now.

**The objective** is to make a social media blogpost website.

- **Front end server**: Will serve the front end but will also live alongside the backend server (the one with the APIs).
- **Backend tech**: Java + Spring Boot + JPA
- **Frontend tech**: JS/HTML/CSS + [BULMA](https://bulma.io/)
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
        - no session: serve /register VIEW
    - POST
        - if session: skip and redirect to /
        - no session: try to register
            - if success: 200 OK - return JSON + Location header to /
            - no success: 40X - return string with error description

- /login
    - GET
        - if session: redirect to /
        - no session: serve /login VIEW
    - POST
        - if session: skip and redirect to /
        - no session: try to login
            - if success: 200 OK - return JSON + Location header to /
            - no success: 40X - return string with error description

- /
    - GET
        - if session: serve /portal PAGE
        - no session: serve /welcome PAGE

### Sequrity
I have a couple of basic things in place. No Spring Security added to the project (In my TOLEARN list)

These are the features I have implemented that I consider security-themed:
- **Authentication**: I have a class that handles basic authenticationg of users and sessions.
- **Hasing passwords**: I make sure I just store a hash to a password.
- **Expirable sessionIds**: Session IDs have an expiration date, so no logging in once and staying logged in forever.
- **Updating sessionIds**: I create a new sessionId on every contact with a user. This opens up a potential for optimization. Maybe have two timers, one for sessionId expiration and another substantially smaller timer to indicate that the sessionId should be refreshed.
- **Sanitazing userame/password characters**: I do some basic sanitation at the service layer, before interacting with the DAO. Right now is just a list of illegal characters that I think should not get to the DAO level. With username/password what I do is to throw an exception that lets the client know about the issue.

**TODO**:

- Adding some authorization so I can have admin accounts.
- Improve sanitation.
- HTTPS ?
- Check out JWT (JSON Web Token)

### Optmization
Similar situation as with security.

These are some things implemented that I consider optimization-themed:
- **Client-side input validation**: saves time to the server. I do check the same things in the server again because I imagine the client is not to be trusted. But for non bad actors this saves the potential back and forth with the server. Something I have to work on for this feature is having a cenrtalized place for these rules. Right now if I want a password to be minimum 8 characters, I have to specify that rule on both the server and the client separately, so mismatches can happen.


**TODO**:

- Optimize sessionId refresh rules: instead of every time, a refresh expiration date.

### Other TODOs

- Create tests
- Database for translation: Have the website text in a database and allow for different languages
- Card that explains the website features (Auth, languages, etc).
- Markdown ?



### User stories, ideas:

**Welcome - page**
- User can read about the website via the Welcome tab
- User can register via the register tab
- User can log in via the login tab
- User can read about contact information via the Contact tab

**Portal - page**
- User can manage its user profile via the Me tab
- User can see personal feed via the Private tab
- User can see the latest going on and search all posts via the Public tab
- User can manage subscriptions (people whos posts appear on "Private") via the Collection tab
- User can see and manage saves posts via the Collection tab


**Me - tab**

A place where you can see all the posts you have created, its the profile page, but when its your own you have an additional "post" with some fields that allow you to edit your stuff
- User can edit user settings:
    - email
    - password
    - username
- User can manage own posts:
    - Delete a post
    - Add a post
    - Edit a post
    - Pin a post

**Private - tab**

A place where you can see all the posts from users you are subscribed to, ordered by creation time.

**Public - tab**

A place where you can see the most popular posts at the moment, also most popular tags and topics.
You can also globally search for posts with the following: Category, hashtag and keywords.

**Collection - tab**

Place where you can see and manage users you are subscribed to and posts you have saved.





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

