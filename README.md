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
- **Backend**: The plan is to hav etwo branches.
    - Currently: Java + Spring Boot + Java's JDBC (Meaning no spring-data-jdbc. Manually configuring the DataSource with HikariCP's DataSource implementation. See `DataSourceConfigurator.java`)
    - Plans for a future branch: Instead of only Java's JDBC, use: Spring JDBC, JPA and Spring JPA.
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

**AccessController (MVC)**: These provide the registration and login interface.
|Endpoint      | Verb |Session?|Response                |Description|SC       |Req. Body |
|--------------|------|--------|------------------------|-----------|---------|----------|
|/register     |GET   |✖️      |register.html           |View       |200      |-         |
|/register     |GET   |✔️      |re to /                 |-          |303      |-         |
|/register     |POST  |✔️      |Error message           |-          |401      |-         |
|/register     |POST  |✖️      |Register user           |CRUD       |200, 400 |-         |
|/login        |GET   |✖️      |login.html              |View       |200      |-         |
|/login        |GET   |✔️      |re to /                 |-          |303      |-         |
|/login        |POST  |✔️      |Error message           |-          |401      |-         |
|/login        |POST  |✖️      |Log in user             |CRUD       |200, 400 |-         |

**PageController**: MVC endpoints related to serving HTML **pages**.
|Endpoint      | Verb  |Session?|Response                |Description|SC   |Req. Body    |
|--------------|-------|--------|------------------------|-----------|-----|-------------|
|/             | GET   |✖️      |welcome.html            |Page       |200  |-            |
|/             | GET   |✔️      |portal.html             |Page       |200  |-            |

**PostController (REST)**: RESTful endpoints related to posts.
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

**UserController (REST)**: RESTful endpoints related to users.
|Endpoint                                | Verb  |Response                |Description                      |SC                |Req. body    |
|----------------------------------------|-------|------------------------|---------------------------------|------------------|-------------|
|/user/{user_id}                         |GET    |UserModel               |Query user                       |200, 400, 401, 404|-            |
|/user/{user_id}                         |DELETE |UserModel               |Delete user                      |200, 400, 401, 404|-            |
|/user (WILL NOT DO, we got /register)   |POST   |UserModel               |Create user                      |201, 400, 401     |UserModel    |
|/user/{user_id}                         |PUT    |UserModel               |Full update on a user            |200, 400, 401, 404|UserModel    |
|/user/{user_id}                         |PATCH  |UserModel               |Partial update on a user         |200, 400, 401, 404|UserModel    |

**CommentController (REST)**: RESTful endpoints related to post's comments.
|Endpoint                                | Verb  |Response                |Description                      |SC                |Req. body    |
|----------------------------------------|-------|------------------------|---------------------------------|------------------|-------------|
|/comment/{comment_id}                   |GET    |CommentModel            |Query a comment                  |200, 400, 401, 404|-            |
|/comments/{post_id}                     |GET    |CommentModel[10]        |Query a post's comments (0 to 9) |200, 400, 401, 404|-            |
|/comments/{post_id}?page={P}            |GET    |CommentModel[10]        |^ (10\*P to 10\*P+10)            |200, 400, 401, 404|-            |
|/comments/{post_id}?page={P}&len={L}    |GET    |CommentModel[L]         |^ (L\*P to L\*P+L)               |200, 400, 401, 404|-            |
|/comment/{post_id}                      |POST   |CommentModel            |Create comment on post           |201, 400, 401     |CommentModel |
|/comment/{comment_id}                   |PUT    |CommentModel            |Full update on a comment         |200, 400, 401, 404|CommentModel |
|/comment/{comment_id}                   |PATCH  |CommentModel            |Partial update on a comment      |200, 400, 401, 404|CommentModel |

**ViewController (MVC)**: MVC endpoints that provide templates for rendering data.
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
|post     |post_id **INT**    |user_id **INT**    |title STR      |description STR  |content STR    |views NUM      |date DATE|
|comment  |comment_id NUM |parent_id NUM  |post_id NUM    |user_id NUM      |content STR    |date DATE      |
|session  |session_id STR |username NUM   |expires NUM    |refresh DATE     |
|user     |user_id **INT**    |username **VARCHAR(50)**   |pass_hash **VARCHAR(100)**|
|category |category STR   |post_id NUM    |
|hashtag  |hashtag STR    |post_id NUM    |
|likes    |post_id NUM    |user_id NUM    |
|saves    |saves_id NUM   |user_id NUM    |

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
WIP



### Sequrity
I have a couple of basic things in place. No Spring Security added to the project (In my TOLEARN list)

These are the features I have implemented that I consider security-themed:
- **Authentication**: I have a system that handles basic authentication of users and sessions.
- **Hasing passwords**: I make sure I just store a hash to a password.
- **Expirable session IDs**: Session IDs have an expiration date, so a client cannot log in once and stay logged in forever.
- **Re-generating session IDs**: Session IDs have a refresh countdown, meaning even when logged in, the server is always re-generating the session ID every some time. This means that if a client is constantly online, the sessionId will still periodically change, stablishing a limited timeframe for any attacker who wants to try to guess the sessionId.
- **Sanitazing userame/password characters**: I do some basic sanitation before interacting with the DAO in order to look for SQL queries. WIP.
- **PreparedStatement**: I make sure to always use JDBC's Prepared statements for queries that involve user input. PreparedStatement's interface sanitizes input by design (setInt, setString, ...).

**Sequrity - TODO**:
- Adding some authorization so I can have admin accounts.
- Improve sanitation.
- HTTPS ?
- Check out JWT (JSON Web Token)



### Optmization
These are some things implemented that I consider optimization-themed:
- **Client-side input validation**: Saves time to the server. I do perform data validation at each layer over at the server because I imagine the client is not to be trusted, but for non bad actors this saves the potential back and forth with the server.
- **DataSource instead of DriverManager**: By using a DataSource that supports connection pooling (HikariCP) instead of the DriverManager, we make sure to not waste time closing and opening connections every time we want to execute a query.

**Optimization - TODO**:
- Find a single source of truth for input validation that the client and the backend can relate to. Database probably.



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

#### Me - tab
You can see:
- All posts you have created ordered by date
- Your profile section box
- Hovering a post will show a gear icon, when licked you will see buttons labeled as: pin, edit, delete.
- Hovering the profile section box will show a gear button that when clicked takes you to an edit profiel page.
- A button for creating a new post, takes you to the post creation page.

This is essentially the account page, and the edit options only appear if you are logged in as the user, otherwise this is how checking anyones user will look like (plus a subscribe button to subscribe to that user in particular)

#### Private - tab
A place where you can see all the posts from users you are subscribed to, ordered by creation time.

#### Public - tab
A place where you can see the most popular posts at the moment, also most popular tags and topics.
You can also globally search for posts with the following: Category, hashtag and keywords.

#### Collection - tab
Place where you can see and manage users you are subscribed to and posts you have saved.


### Exceptions and data transfer between layers - WIP

#### DAO:
DAO layers always return the following:
- Read: Data model with retrieved record data or `null` if nothing to read found.
- Create: Data model with created record data.
- Update: Data model with updated record data.
- Delete: `boolean` indicating operation success status.
- DAO instances throw no exceptions for the time being.

#### Service:
Service layers do throw exceptions:
- Exceptions to represent a faulty return from a CRUD operation.
- Exceptions to represent failure at the service layer.

#### Exception hierarchy:
```
RuntimeException                                [java.lang]
├── AuthenticationException (Abstract)          [com.alfredcode.socialWebsite.security.exception]
│   ├── FailedAuthenticationException           [com.alfredcode.socialWebsite.service.session.exception]
│   ├── FailedSessionAuthenticationException    [com.alfredcode.socialWebsite.service.session.exception]
│   ├── FailesSessionCreationException          [com.alfredcode.socialWebsite.service.session.exception]
│   ├── FailedSessionUpdateException            [com.alfredcode.socialWebsite.service.session.exception]
│   └── FailesUserAuthenticationException       [com.alfredcode.socialWebsite.service.user.exception]
├── FailedUserRegistrationException             [com.alfredcode.socialWebsite.service.user.exception]
├── UnauthorizedActionException                 [com.alfredcode.socialWebsite.security.exception]
└── IllgalArgumentException                     [java.lang]
```

#### Handling exceptions:
- Exceptions are handled by the `GlobalExceptionsController.java` controller when they are general or authentication/authorization related exceptions.
- If they are exceptions speciffic to a service, then the controller of that service will have the handlers for it at the bottom of the class.



### Authorization and authentication: AOP, Interceptor handlers and controllers
In order to implement an easy Authorization and Authentication system, we will be using AOP methods and Interceptor handlers in combination with some custom annotations.

**Annotations**:
- @SessionRequired: to be used on method handlers (@Controller methods) that require a valid session in order for the client to interact with its endpoints.
- @NoSessionAllowed: to be used on method handlers (@Controller methods) that require **no** valid session in order for the client to interact with its endpoints (login, register, ...)

**AOP methods (Auth.java)**:
- @Before, Auth.sessionRequired(): Authenticates a client's session. Triggered on @SessionRequired annotated methods.
- @Before, Auth.noSessionAllowed(): Authenticates a client's session. Triggered on @NoSessionAllowed annotated methods.

**Interceptor Handlers (SessionIncerceptor.java)**:
- SessionIncerceptor.preHandle():
    - If the method handler has a @SessionRequired annotation, tries to update the session.
    - If the method handler has a @NoSessionAllowed annotation, does nothing.

#### Why use both AOP and Interceptor handlers, instead of either one:
- Separation of concerns: Interceptors are the conventional place for HTTP request/response modifications while AOP are saved for cross-cutting concerns; that is tasks that involve several unrealted instances, each performing some operation.
- Ease of access: Interceptors make it really easy to access the HTTP req/res because they are intended to modify these things. They are even included in the servlet lifecycle. With AOP you do need to do some workaround to get to the servlets.
- Readability: Even tho we can do these things in either place, the standard **seems** to be the one already explained, so going against it makes the code harder to understand.

### Managing sessions (AOP, Interceptor handlers and @Schedule)

The authentication process is organized in a way that allows me to have two threads handle the sessions table at the same time without them stepping onto  eachother's feet:

- **Step 1: Client sends HTTP request.**
- **Step 2: Authentication (Auth's AOP methods)**:
    - Given the sessionId:
        - **(A)** If the session exists in the database and it is not expired, allow the request to move to the next step.
        - **(B)** If the session doesn't exist in the database or it is expired, deny access.

- **Step 3: SessionInterceptor's preHandle method**:
    - Given the sessionId:
        - **(C)** If the session exists in the database (whether or not is expired), **attempt** to update it: We allow the request to move to the next step on success, deny access on failure.
        - **(D)** If the session doesn't exist in the database anymore, deny access.

- **Step 4: The HTTP request gets processed.**

Those steps are carried over on that order in the main thread.

The second thread runs a DELETE query on the session table every X time. A couple of things to notie about that:
- MySQL may receive orders at the same time but it won't execute them at the same time, at least not orders that modify a specific record. Which means that the DELETE query might run just before or just after any of the main thread queries that modify the record.
- When we run the DELETE query, that query has a conditional, only expired queries will get deleted.

With that in mind, we consider these cases:

- **Case 1 - session deleted/expired before Step 2**: At step 2, the client contains the sessionId of a record that has been deleted due to it being expired. Auth's AOP method will try to retrieve the session and will fail, denying access.
 
- **Case 2 - session deleted/expired right after Step 2**: The session passes authentication (Step 2), but expires/removed shortly after. The SessionInterceptor's preHandle method (Step 3) then tries to again access the session. It find the session to be expired/deleted, so it denies access.

- **Case 3 - session deleted before updating it**: The SessionInterceptor's preHandle method retrieves the session and it is not expired. It then prepares the new updated values for the session and sends out the update query. However, when running the update query, MySQL finds that the exception no longer exists due to the second thread deleting it just a moment ago. The update fails, and the access is denied. The reason this is possible is because we can check if an update affected any row at all, and conclude based on that.

- **Case 4 - A delete query arrives just after updating the query**: No deletion will happen, because that deletion has a conditional within it that will fail if the session just got updated.

Because we make sure to authenticate and update the session before processing the HTTP request, we can allow ourselves to let a session pass authentication and expire before we get a chance to update the session.