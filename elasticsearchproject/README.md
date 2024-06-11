# Employee Statistics API

## Overview
This API provides various endpoints to retrieve statistical information about employees. Below is a detailed description of each endpoint available in the API.

## Technologies Used
* Java SDK 17
* Spring Boot (https://start.spring.io/)
* ElasticSearch Server 6.8.0


## Pre-Requirement
1 . Make sure Elasticsearch 6.8.0 has been installed in your local computer.
2 . Clone and follow instruction in here https://gitlab.com/adik.darmadi/dataset-elastic
3 . Run ElasticSearch server


## Endpoints

### 1. Get Total Count of Employees
- **URL:** `/count`
- **Method:** `GET`
- **Description:** Returns the total number of employees.
- **Response:**
    - **Type:** `Map<String, Double>`
        - **Keys:** String type description (e.g., "Total Count Employee")
        - **Values:** Total employees in Long type.
    - **Example:** `{ "Total Count Employee": 50000 }`

### 2. Get Average Salary of Employees
- **URL:** `/average`
- **Method:** `GET`
- **Description:** Returns the average salary of all employees.
- **Response:**
    - **Type:** `Map<String, Double>`
        - **Keys:** String type description (e.g., "Average Salary of All Employees")
        - **Values:** Average salary in double.
    - **Example:** `{ "Average Salary of All Employees": 57843.9 }`

### 3. Get Minimum and Maximum Salary of Employees
- **URL:** `/min-max-salary`
- **Method:** `GET`
- **Description:** Returns the minimum and maximum salaries among all employees.
- **Response:**
    - **Type:** `MinMaxResponse`
        - **Fields:**
            - `minValue` (`Double`): Minimum salary.
            - `maxValue` (`Double`): Maximum salary.
    - **Example:** `{ "minValue": 30000.0, "maxValue": 120000.0 }`

### 4. Get Age Distribution of Employees
- **URL:** `/age-distribution`
- **Method:** `GET`
- **Description:** Returns the age distribution of employees in the form of a histogram.
- **Response:**
    - **Type:** `Histogram`
        - **Fields:**
            - `buckets` (`List<Bucket>`): List of age ranges with their corresponding counts.
    - **Example:** `{ "buckets": [ {"range": "20-30", "count": 50}, {"range": "31-40", "count": 70} ] }`

### 5. Get Gender Distribution of Employees
- **URL:** `/gender-distribution`
- **Method:** `GET`
- **Description:** Returns the distribution of employees by gender.
- **Response:**
    - **Type:** `Map<String, Long>`
        - **Keys:** Gender (e.g., "Male", "Female")
        - **Values:** Count of employees for each gender.
    - **Example:** `{ "male": 80, "female": 70 }`

### 6. Get Marital Status Distribution of Employees
- **URL:** `/marital-distribution`
- **Method:** `GET`
- **Description:** Returns the distribution of employees by marital status.
- **Response:**
    - **Type:** `Map<String, Long>`
        - **Keys:** Marital status (e.g., "Single", "Married")
        - **Values:** Count of employees for each marital status.
    - **Example:** `{ "unmarried": 90, "married": 60 }`

### 7. Get Date of Joining Distribution of Employees
- **URL:** `/join-date-distribution`
- **Method:** `GET`
- **Description:** Returns the distribution of employees based on their date of joining.
- **Response:**
    - **Type:** `Map<String, Long>`
        - **Keys:** Join date (formatted as a string, e.g., "2022-01")
        - **Values:** Count of employees who joined in each date.
    - **Example:** `{ "2022-01-31": 15, "2022-02-20": 10 }`

### 8. Get Top Interests Distribution of Employees
- **URL:** `/top-interest-distribution`
- **Method:** `GET`
- **Description:** Returns the distribution of top interests among employees.
- **Response:**
    - **Type:** `Map<String, Long>`
        - **Keys:** Interest (e.g., "Reading", "Sports")
        - **Values:** Count of employees with each interest.
    - **Example:** `{ "Reading": 30, "Sports": 25 }`

### 9. Get Designation Distribution of Employees
- **URL:** `/designation-distribution`
- **Method:** `GET`
- **Description:** Returns the distribution of employees by their designation.
- **Response:**
    - **Type:** `Map<String, Long>`
        - **Keys:** Designation (e.g., "Manager", "Developer")
        - **Values:** Count of employees for each designation.
    - **Example:** `{ "Manager": 20, "Developer": 50 }`

## Error Handling
All endpoints may return standard HTTP error codes as follows:
- `400 Bad Request`: The request could not be understood by the server due to malformed syntax.
- `404 Not Found`: The requested resource could not be found.
- `500 Internal Server Error`: The server encountered an unexpected condition which prevented it from fulfilling the request.

