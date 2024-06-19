## This document is a WIP


**The idea** is to make a social media blogpost website.

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

Description types:
- **View**: Means the endpoint returns HTML that is considered a part of a full page, a component. Like a card or a banner.
- **Page**: Means the endpoint returns a full page, or the foundations of it where other elements are to be placed.
- **CRUD**: Means the website does some CRUD operation.

**AccessController**: These provide the registration and loggin interface.
|Endpoint      | Verb  |Session?|Response                |Description|SC        |Req. Body    |
|--------------|-------|--------|------------------------|-----------|----------|-------------|
|/register     | GET   |✖️      |register.html           |View       |200 OK    |-            |
|/register     | GET   |✔️      |re to /                 |-          |302 FOUND |-            |
|/register     | POST  |✔️      |re to /                 |-          |200 OK    |-            |
|/register     | POST  |✖️      |*register user* + Loc   |CRUD       |302 FOUND |UserModel    |
|/login        | GET   |✖️      |login.html              |View       |200 OK    |-            |
|/login        | GET   |✔️      |re to /                 |-          |302 FOUND |-            |
|/login        | POST  |✔️      |re to /                 |-          |200 OK    |-            |
|/login        | POST  |✖️      |*log in user* + Loc     |CRUD       |200 OK    |UserModel    |


**Frontend**: Endpoints related to serving HTML pages.
|Endpoint      | Verb  |Session?|Response                |Description|SC        |Req. Body    |
|--------------|-------|--------|------------------------|-----------|----------|-------------|
|/             | GET   |✖️      |welcome.html            |Page       |200 OK    |-            |
|/             | GET   |✔️      |portal.html             |Page       |200 OK    |-            |

**From here on out, we can assume that any request made to the endpoints below that doesnt provide a valid session ID will result on a redirect to /.**

**PostController**: Endpoints related to serving posts.
|Endpoint                      | Verb  |Response                |Description                    |SC      |Req. body    |
|------------------------------|-------|------------------------|-------------------------------|--------|-------------|
|/post                         | GET   |welcome.html            |View                           |200 OK  |-            |
|/post/{post_id}               | GET   |PostModel               |Query post                     |200 OK  |-            |
|/posts/{user_id}              | GET   |PostModel[10]           |Query posts (0 to 10)          |200 OK  |-            |
|/posts/{user_id}?page=P       | GET   |PostModel[10]           |Query posts (10\*P to 10\*P+10)|200 OK  |-            |
|/posts/{user_id}?page=P&len=L | GET   |PostModel[L]            |Query posts (L\*P to L\*P+L)   |200 OK  |-            |


/post/{id}
/posts?filter=X&orb=Y&ord=Z
/posts/{user_id}?filter=X&orb=Y&ord=Z

/posts/{user_id}    number
/posts/{category}   String
/posts/{hashtag}    #String
/


### Database
Below are the tables (WIP):


**Tables**:
Table name|Element 1      |Element 2      |Element 3      |Element 4      |Element 5      |Element 6      |Element 7|
|---------|---------------|---------------|---------------|---------------|---------------|---------------|---------|
|POST     |post_id NUM    |user_id NUM    |title STR      |description STR|content STR    |views NUM      |date DATE|
|COMMENT  |comment_id NUM |parent_id NUM  |post_id        |user_id NUM    |content STR    |date DATE      |
|CATEGORY |category STR   |post_id NUM    |
|HASHTAG  |hashtag STR    |post_id NUM    |
|LIKES    |post_id NUM    |user_id NUM    |
|SAVES    |saves_id NUM   |user_id NUM    |
|USER     |user_id NUM    |pass_hash STR  |username STR   |
|SESSION  |hash STR       |


**Indexes**:
- **Post**:
    - Clustered: post_id. Helps when retrieving a spcific post.
    - Non-clustered, composite: *user_id* **>** *date*. Helps when retrieving posts of a given user in chronological order.
- **Category**:
    - Non-clustered, composite: *category* **>** *date*. Helps when retrieving posts from a given category in chronological order.
- **Hashtag**:
    - Non-clustered, composite: *hashtag* **>** *date*. Helps whenretrieving posts from a given hashtag in chronological order.
- **Likes**:
    - Non-clustered: *post_id*. Helps when retrieving likes belonging to a given post.
- **Saves**:
    - Non-clustered: *post_id*. Helps when retrieving saves belonging to a given post.
- **Comment**:
    - Clustered: *comment_id*. Helps when retrieving a specific comment.
    - Non-clustered: *post_id*. Helps when retrieving comments belonging to a given post.
- **User**:
    - Clustered: *user_id*. Helps when retrieving a specific user.


### Searching
For the searching, we parse the card content for the 10 most common words, and take that along the cards category and hashtags.



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
You can see:
- All posts you have created ordered by date
- Your profile section box
- Hovering a post will show a gear icon, when licked you will see buttons labeled as: pin, edit, delete.
- Hovering the profile section box will show a gear button that when clicked takes you to an edit profiel page.
- A button for creating a new post, takes you to the post creation page.

This is essentially the account page, and the edit options only appear if you are logged in as the user, otherwise this is how checking anyones user will look like (plus a subscribe button to subscribe to that user in particular)

**Private - tab**

A place where you can see all the posts from users you are subscribed to, ordered by creation time.

**Public - tab**

A place where you can see the most popular posts at the moment, also most popular tags and topics.
You can also globally search for posts with the following: Category, hashtag and keywords.

**Collection - tab**

Place where you can see and manage users you are subscribed to and posts you have saved.


### Notes:
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

