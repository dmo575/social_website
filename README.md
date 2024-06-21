## This project and codument are a WIP


**The idea** is to make a social media blogpost website.

- **Database**: SQLite
- **Backend**: Java + Spring Boot + JPA
- **Frontend**: JS/HTML/CSS + [BULMA](https://bulma.io/)
- **Build tool**: Maven

### Current structure:

```
                              Client
                         +-------------+
                         | HTML/CSS/JS |
                         +-------------+

              Server 1 (Frontend + Backend APIs)
+------------------------------------------------------------------+
|                     Controllers : HTTP, endpoints                |
+--------------------------+---------------------------------------+
|            Auth : Authentication & Authorization                 |
+--------------------------+---------------------------------------+
|                  Service : Business logic                        |
+--------------------------+---------------------------------------+
|                    DAO : CRUD, concurrency (session)             |
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

SC:
- 200 OK
- 201 Created
- 303 See Other
- 400 Bad Request
- 401 Unauthorized
- 403 Forbidden
- 404 Not found
- 500 Internal Server Error

**AccessController**: These provide the registration and loggin interface.
|Endpoint      | Verb  |Session?|Response                |Description|SC       |Req. Body |
|--------------|-------|--------|------------------------|-----------|---------|----------|
|/register     | GET   |✖️      |register.html           |View       |200      |-         |
|/register     | GET   |✔️      |re to /                 |-          |303      |-         |
|/register     | POST  |✔️      |Error message           |-          |403      |-         |
|/register     | POST  |✖️      |Register user           |CRUD       |200, 400 |-         |
|/login        | GET   |✖️      |login.html              |View       |200      |-         |
|/login        | GET   |✔️      |re to /                 |-          |303      |-         |
|/login        | POST  |✔️      |Error message           |-          |403      |-         |
|/login        | POST  |✖️      |Log in user             |CRUD       |200, 400 |-         |


**PageController**: Endpoints related to serving HTML pages.
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



### Database
Below are the tables (WIP):


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