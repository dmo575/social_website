## This project and codument are a WIP
Social media blogpost website.


### Structure - WIP

**Tiers**:
```
                                         Client
                                    +-------------+
                                    | HTML/CSS/JS |
                                    +-------------+
          
                                          ⬇⬆
          
                           Server 1 (Frontend + Backend APIs)
+----------------------------------------------------------------------------------------+
| AOP (Auth) ▶️ InterceptorHandlers (HTTP session updt) ▶️ Controllers (HTTP, endpoints) |
+----------------------------------------------------------------------------------------+
|                                Service : Business logic                                |
+----------------------------------------------------------------------------------------+
|                            DAO : CRUD, concurrency (session)                           |
+----------------------------------------------------------------------------------------+
          
                                          ⬇⬆
          
                                        Server 2
                                  +-------------------+
                                  | DB : Data storage |
                                  +-------------------+
```

**Tech**:

- **Database**: MySQL
- **Backend**: Java + Spring Boot + JPA
- **Frontend**: JS/HTML/CSS + [BULMA](https://bulma.io/)
- **Build tool**: Maven
- **Hashing**: bcrypt
- **Random gen**: commons-lang3


### Controllers - WIP

Below is a list of the controller classes and their endpoints.

Description types:
- **View**: Means the endpoint returns HTML that is considered a part of a full page, a component. Like a card or a banner.
- **Page**: Means the endpoint returns a full page, or the foundations of it where other elements are to be placed.
- **CRUD**: Means the website does some CRUD operation.


**AccessController**: These provide the registration and loggin interface.
|Endpoint      | Verb |Session?|Response                |Description|SC       |Req. Body |
|--------------|------|--------|------------------------|-----------|---------|----------|
|/register     |GET   |✖️      |register.html           |View       |200      |-         |
|/register     |GET   |✔️      |re to /                 |-          |303      |-         |
|/register     |POST  |✔️      |Error message           |-          |403      |-         |
|/register     |POST  |✖️      |Register user           |CRUD       |200, 400 |-         |
|/login        |GET   |✖️      |login.html              |View       |200      |-         |
|/login        |GET   |✔️      |re to /                 |-          |303      |-         |
|/login        |POST  |✔️      |Error message           |-          |403      |-         |
|/login        |POST  |✖️      |Log in user             |CRUD       |200, 400 |-         |


**PageController**: MVC endpoints related to serving HTML **pages**.
|Endpoint      | Verb  |Session?|Response                |Description|SC   |Req. Body    |
|--------------|-------|--------|------------------------|-----------|-----|-------------|
|/             | GET   |✖️      |welcome.html            |Page       |200  |-            |
|/             | GET   |✔️      |portal.html             |Page       |200  |-            |


**PostController**: RESTful endpoints related to posts.
|Endpoint                                | Verb  |Response                |Description                      |SC                |Req. body    |
|----------------------------------------|-------|------------------------|---------------------------------|------------------|-------------|
|/post/{post_id}                         |GET    |PostModel               |Query post                       |200, 400, 401, 404|-            |
|/posts?filter={F}                       |GET    |PostModel[10]           |Query posts (0 to 9)             |200, 400, 401, 404|-            |
|/posts?filter={F}&page={P}              |GET    |PostModel[10]           |Query posts (10\*P to 10\*P+10)  |200, 400, 401, 404|-            |
|/posts?filter={F}&page={P}&len={L}      |GET    |PostModel[L]            |Query posts (L\*P to L\*P+L)     |200, 400, 401, 404|-            |
|/post/{post_id}                         |DELETE |PostModel               |Delete post                      |200, 400, 401, 404|-            |
|/post                                   |POST   |PostModel               |Create post                      |201, 400, 401     |PostModel    |
|/post/{post_id}                         |PUT    |PostModel               |Full update on a post            |200, 400, 401, 404|PostModel    |
|/post/{post_id}                         |PATCH  |PostModel               |Partial update on a post         |200, 400, 401, 404|PostModel    |
**Filters**: user_id, category, hashtag

Note: when querying posts, the order of those is from most to least recently created.


**UserController**: RESTful endpoints related to users.
|Endpoint                                | Verb  |Response                |Description                      |SC                |Req. body    |
|----------------------------------------|-------|------------------------|---------------------------------|------------------|-------------|
|/user/{user_id}                         |GET    |UserModel               |Query user                       |200, 400, 401, 404|-            |
|/user/{user_id}                         |DELETE |UserModel               |Delete user                      |200, 400, 401, 404|-            |
|/user (WILL NOT DO, we got /register)   |POST   |UserModel               |Create user                      |201, 400, 401     |UserModel    |
|/user/{user_id}                         |PUT    |UserModel               |Full update on a user            |200, 400, 401, 404|UserModel    |
|/user/{user_id}                         |PATCH  |UserModel               |Partial update on a user         |200, 400, 401, 404|UserModel    |


**CommentController**: RESTful endpoints related to post's comments.
|Endpoint                                | Verb  |Response                |Description                      |SC                |Req. body    |
|----------------------------------------|-------|------------------------|---------------------------------|------------------|-------------|
|/comment/{comment_id}                   |GET    |CommentModel            |Query a comment                  |200, 400, 401, 404|-            |
|/comments/{post_id}                     |GET    |CommentModel[10]        |Query a post's comments (0 to 9) |200, 400, 401, 404|-            |
|/comments/{post_id}?page={P}            |GET    |CommentModel[10]        |^ (10\*P to 10\*P+10)            |200, 400, 401, 404|-            |
|/comments/{post_id}?page={P}&len={L}    |GET    |CommentModel[L]         |^ (L\*P to L\*P+L)               |200, 400, 401, 404|-            |
|/comment/{post_id}                      |POST   |CommentModel            |Create comment on post           |201, 400, 401     |CommentModel |
|/comment/{comment_id}                   |PUT    |CommentModel            |Full update on a comment         |200, 400, 401, 404|CommentModel |
|/comment/{comment_id}                   |PATCH  |CommentModel            |Partial update on a comment      |200, 400, 401, 404|CommentModel |


**ViewController**: MVC endpoints that provide templates for rendering data.
|Endpoint                                | Verb  |Response                |Description                      |SC       |Req. body    |
|----------------------------------------|-------|------------------------|---------------------------------|---------|-------------|
|/views/element/post                     |GET    |post.html               |View                             |200, 401 |-            |
|/views/element/user                     |GET    |user.html               |View                             |200, 401 |-            |
|/views/element/comment                  |GET    |comment.html            |View                             |200, 401 |-            |
|/views/tab/account                      |GET    |account.html            |View                             |200, 401 |-            |
|/views/tab/private                      |GET    |private.html            |View                             |200, 401 |-            |
|/views/tab/public                       |GET    |public.html             |View                             |200, 401 |-            |



### Database - WIP

**Tables**:
Table name|Column         |Column         |Column         |Column           |Column         |Column         |Column   |
|---------|---------------|---------------|---------------|-----------------|---------------|---------------|---------|
|POST     |post_id NUM    |user_id NUM    |title STR      |description STR  |content STR    |views NUM      |date DATE|
|COMMENT  |comment_id NUM |parent_id NUM  |post_id NUM    |user_id NUM      |content STR    |date DATE      |
|SESSION  |session_id STR |username NUM   |expires NUM    |refresh DATE     |
|USER     |user_id NUM    |pass_hash STR  |username STR   |
|CATEGORY |category STR   |post_id NUM    |
|HASHTAG  |hashtag STR    |post_id NUM    |
|LIKES    |post_id NUM    |user_id NUM    |
|SAVES    |saves_id NUM   |user_id NUM    |


**Indexes**:
- **Post**:
    - Clustered: post_id. Helps when retrieving a specific post.
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


### Searching - WIP
For the searching, we could parse the card content for the 10 most common words, and save that along the cards category and hashtags.


### Sequrity
I have a couple of basic things in place. No Spring Security added to the project (In my TOLEARN list)

These are the features I have implemented that I consider security-themed:
- **Authentication**: I have a class that handles basic authenticationg of users and sessions (Auth).
- **Hasing passwords**: I make sure I just store a hash to a password.
- **Expirable session IDs**: Session IDs have an expiration date, so a client cannot log in once and stay logged in forever.
- **Re-generating session IDs**: Session IDs have a refresh countdown, meaning even when logged in, the server is always re-generating the session ID every X minutes. This shortens any window of opportunity for an attacker.
- **Sanitazing userame/password characters**: I do some basic sanitation before interacting with the DAO in order to look for SQL queries. WIP.

**Sequrity - TODO**:
- Adding some authorization so I can have admin accounts.
- Improve sanitation.
- HTTPS ?
- Check out JWT (JSON Web Token)

### Optmization
Similar situation as with security.

These are some things implemented that I consider optimization-themed:
- **Client-side input validation**: Saves time to the server. I do check the same things in the server again because I imagine the client is not to be trusted. But for non bad actors this saves the potential back and forth with the server.

**Optimization - TODO**:
- Find a single source of truth for input validation that the client and the backend can relate to. Database probably.

### Other things:
- **Concurrency**: For the sessions, the backend run a second thread that scans the table every X time and removes expired keys, so I implement Optimistic locking for the session table (WIP).

### Backlog:
- Create tests
- Database for translation: Have the website text in a database and allow for different languages
- Card that explains the website features (Auth, languages, etc).
- Markdown ?
- Work on concurrency. Do transactions and lock levels.



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

#### END - TODO list:
- The other TODOs scattered across the document
- Properly design Exceptions. Right now I don't really follow a logic for which ones should be checked and which ones should not. Think it trough
- Now that we are adding AOP with custom annotations that take in exceptions, properly organize exception hierarchy
- Properly organize the project. Instead of socialWebsite/annotations/, let the directory structure be guided by themes, like socialWebsite/security/annotations
- Finish implementing AOP for cross-cutting (even tho theres none atm, is just Auth) and InterceptorHandlers for HTTP session management.
- Figure out what's going on with the H2 dependency. I can't get it out the project. I will use MySQL
- Start setting up a MySQL database, decide on how to proceed on the backend: Plain JDBC (DriverManager || DataSource) or also add Spring JPA + Repositories. Do some refreshing on this.
- Concurrency, transactions

### Exceptions and data transfer between layers - WIP


#### Database:
TODO

#### DAO:
DAO layers always return the following:
- Read: Data model with retrieved record data or `null` if error.
- Create: Data model with created record data or `null` if error.
- Update: Data model with updated record data or `null` if error.
- Delete: `boolean` indicating operation success status.
- DAO instances throw no exceptions for the time being.


#### Service:
Service layers do throw exceptions:
- Exceptions to represent a faulty return from a CRUD operation.
- Exceptions to represent failure at the service layer.
- Ambiguous exceptions are thrown to indicate that something went wrong in the process, but their name doesnt specifically points to the issue. Their description message will describe the issue, which will be a business logic related issue unless the error comes from the DAO (some null return), in which case the message will be generic.

|Layer             |Exception type |Exception name                 |Reasons to throw it                   |
|------------------|---------------|-------------------------------|--------------------------------------|
|Any service layer |Speciffic      |IllegalArgumentException       |The data passed is invalid            |
|UserService       |Ambiguous      |FailedUserRegistrationEx       |Error while registering the user      |
|UserService       |Ambiguous      |FailedUserAuthenticationEx     |Error while authenticating the user   |
|SessionService    |Ambiguous      |FailedSessionAuthenticationEx  |Error while authenticating the sesion |
|SessionService    |Ambiguous      |FailedSessionUpdateEx          |Error while updating the sesion       |
|SessionService    |Ambiguous      |FailedSessionCreationEx        |Error while creating the sesion       |
|. . .


#### Exception hierarchy:
```
RuntimeException                                [java.lang]
├── AuthenticationException (Abstract)          [com.alfredcode.socialWebsite.security.exception]
│   ├── FailedAuthenticationException           [com.alfredcode.socialWebsite.service.session.exception]
│   ├── FailedSessionAuthenticationException    [com.alfredcode.socialWebsite.service.session.exception]
│   ├── FailesSessionCreationException          [com.alfredcode.socialWebsite.service.session.exception]
│   ├── FailedSessionUpdateException            [com.alfredcode.socialWebsite.service.session.exception]
│   └── FailesUserAuthenticationException       [com.alfredcode.socialWebsite.service.user.exception]
├── FailesUserRegistrationException             [com.alfredcode.socialWebsite.service.user.exception]
├── ForbiddenActionException                    [com.alfredcode.socialWebsite.exception]
└── IllgalArgumentException                     [java.lang]
```

#### Handling exceptions:
- Exceptions are handled by the `GlobalExceptionsController.java` controller when they are general exceptions.
- If they are exceptions speciffic to a service, then the controller of that service will have the handlers for it at the bottom of the class.

**Auth**:
- Catches: Service layer exceptions
- Throws: Auth exceptions
    - FailedAuthenticationException: Generic for when something goes wrong with Auth (Either comming from a the service layer in use or within Auth).
    - FailedSessionAuthentication: The authentication provided is invalid.
    - FailedSessionCreationException: A session for the user could not be created.

**Outdated Exceptions**:
- FailedUserAuthentication: We use FailedSessionAuthentication

### AOP, Controllers and Interceptor handlers - WIP
In order to implement an easy Authorization and Authentication system, we will be using AOP methods and Interceptor handlers in combination with some custom annotations.

**First layer is Authentication**:
- @SessionRequired: to be used on @Controller methods that require a valid session in order for the client to interact with its endpoints
- @NoSessionAllowed: to be usedon @Controller methods that require **no** valid session in order for the client to interact with its endpoints (login, register, ...)
- Auth.sessionRequired(): AOP method (@Before). Authenticates a client's session. Throws exceptions on failure.
- Auth.noSessionAllowed(): AOP method (@Before). Authenticates a client's session. Throws exceptions on success.

**Second layer are the controllers**:
- Process the HTTP request and come up with an HTTP response.

**Third layer is the Interceptor handler (SessionInterceptor)**:
- If method handler (controller method) has a @SessionRequired annotation: Update session
    - We know that the client had a valid session before hitting this point because of Auth
- If method handler (controller method) has a @NosessionAllowed annotation: do nothing
    - We know the user had no valid session before hitting this point because of Auth

**TODO:**
- Add the ability to define a exception to throw when Auth's AOP methods fail. Or the ability to define a redirect and SC. Have defaults in place.

#### Why use both AOP and Interceptor handlers, instead of either:
- Separation of concerns: Interceptors are the conventional place for HTTP request/response modifications while AOP are saved for cross-cutting concerns; that is tasks that involve several unrealted instances, each performing some operation.
- Ease of access: Interceptors make it really easy to access the HTTP req/res because they are intended to modify these things. They are even included in the servlet lifecycle. With AOP you do need to do some workaround to get to the servlets.
- Standards: Even tho we can do these things in either place, the standard seems to be the one already explained, so going against it makes the code harder to understand.