## README.md - Login API Endpoints

This document describes the Login API endpoints exposed by the `LoginController` class. These endpoints facilitate user registration and authentication for your application.


## Technologies Used
* Java SDK 17
* Spring Boot (https://start.spring.io/)
* JWT (JSON Web Token) for authentication
* Hibernate
* MySQL

## Pre-Requirement
1 . Make sure MySql has been installed in your local computer. 
2 . Go to `application.properties` and set password and username of your MySql before run the app

## Endpoints

### NOTES
```NOTES
in Postman, for all required authentication endpoints, do login first from login endpoint and 
copy jwt token generated from its response, choose tab Authorization and choses Bearer Token, paste token to 
that fields

```
### 1. Public Signup

#### HTTP Method: `POST`

#### URL: `/public/signup`

#### Request Body:

* Content-Type: `application/json`
* Expected format: `UserEntity` object containing user registration details (e.g., username, password, email)

#### Example Request:

```json
{
  "username": "woka",
  "password": "secret-password",
  "email": "woka@email.com"
}
```

#### Example Response:

```json
{
  "id": 1,
  "name": "woka3",
  "email": "woka3@gmail.com",
  "password": "$2a$10$NiwqsGj.Uw4U8gkJDnmJ4Ow9K/UrtmlCmq3Q4ItWXquU4BOPVFoxe",
  "enabled": true,
  "authorities": [],
  "username": "woka3@gmail.com",
  "accountNonLocked": true,
  "credentialsNonExpired": true,
  "accountNonExpired": true
}
```

### 2. Public Login

#### HTTP Method: `POST`

#### URL: `/public/login`

#### Request Body:

* Content-Type: `application/json`
* Expected format: `LoginRequest` object containing user login credentials (username/email and password)


#### Example Request:

```json
{
  "username": "woka",
  "password": "secret-password"
}
```

#### Example Response:

```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3b2thM0BnbWFpbC5jb20iLCJyb2xlcyI6Ik1FTUJFUiIsImV4cCI6MTcxODA3MTkzMSwiaWF0IjoxNzE4MDcwOTMxfQ.muTQojqElyE7Ix0tZnBxD-lTklAdkhKzRKH01tJAidI",
  "expiresIn": 40000000
}
```

## REDAME.md - Post Management Endpoints

This document describes the endpoints exposed by the `PostEntityController` class, which manages blog posts. These endpoints cover creating, retrieving, updating, and deleting posts. Authentication is required for all endpoints except retrieving all posts.

**Authentication:**

These endpoints leverage Spring Security for authentication. A valid JWT token needs to be provided in the request header to access protected endpoints.

### Endpoints

#### 1. Create New Post (`POST /api/content`)

* **HTTP Method:** `POST`
* **URL:** `/api/content`
* **Request Body:**
  * Content-Type: `application/json`
  * Expected format: `PostEntity` object containing the new post details (e.g., title, content)
* **Authorization:** Valid JWT token in the request header


* **Example Request:**

```json
{
  "title": "My New Blog Post",
  "content": "This is the content of my new blog post."
}
```

```json

{
    "id": 1,
    "authorId": 1,
    "content": "marriage is about being a right person version 4",
    "createdAt": "2024-06-11T08:56:53.275312",
    "updateAt": "2024-06-11T08:56:53.275312"
}
```
### 2. Get All Posts (`GET /public/content/all-content`)

This endpoint retrieves all existing blog posts. Authentication is not required for this action.

* **HTTP Method:** `GET`
* **URL:** `/api/content/all-content`
* **Authorization:** Not required

* **Example Response:**

```json
[
  {
    "id": 1,
    "content": "This is the content of my new blog post.",
    "authorId": 1,
    "createdAt": "2024-06-11T08:56:53.275312",
    "updatedAt": "2024-06-11T08:56:53.275312"
  }
]
```

### 3. Get Post by ID (`GET /public/content/{id}`)

This endpoint retrieves a specific blog post based on its unique identifier. Authentication with a valid JWT token is required for this action.

* **HTTP Method:** `GET`
* **URL:** `/api/content/{id}`
* **Path Variable:**
  * `{id}`: The unique identifier of the post (replace with an actual number)

* **Authorization:** Valid JWT token in the request header


* **Example Response:**
```
  {
    "id": 1,
    "content": "This is the content of my new blog post.",
    "authorId": 1,
    "createdAt": "2024-06-11T08:56:53.275312",
    "updatedAt": "2024-06-11T08:56:53.275312" 
  }

```

### 4. Update Post by ID (`PUT /api/content/{id}`)

This endpoint allows updating the content of a specific blog post based on its unique identifier. Authentication with a valid JWT token is required. Additionally, the user attempting the update must be the author of the post for successful execution.

* **HTTP Method:** `PUT`
* **URL:** `/api/content/{id}`
* **Path Variable:**
  * `{id}`: The unique identifier of the post (replace with an actual number)

* **Request Body:**
  * Content-Type: `application/json`
  * Expected format: Partial `PostEntity` object containing the updated content fields (e.g., "content")

* **Authorization:** Valid JWT token in the request header

* **Example Request:**

```json
{
  "content": "This is the updated content of my blog post."
}
```

### 5. Delete Post by ID (`DELETE /api/content/{id}`)

This endpoint allows deleting a specific blog post based on its unique identifier. Authentication with a valid JWT token is mandatory. Additionally, the user attempting the deletion must be the author of the post for successful execution.

* **HTTP Method:** `DELETE`
* **URL:** `/api/content/{id}`
* **Path Variable:**
  * `{id}`: The unique identifier of the post (replace with an actual number)

* **Authorization:** Valid JWT token in the request header


* **Example Response:**
```json
{
  "message": "Delete Success",
  "status": 200
}
```

Information about status code:

* **Response:**
  * Status Code:
    * `200 OK`: Retrieval successful. Response body contains the desired object for the requested post.
    * `401 Unauthorized`: Invalid or missing JWT token.
    * `404 Not Found`: Data requested doesn't exist.
    * `403 Forbidden`: Endpoint need authentication to be accessed