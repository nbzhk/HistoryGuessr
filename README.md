
# History Guessr

History Guessr is an interactive web game inspired by GeoGuessr and TimeGuessr. This is my final project for the Spring Advanced Course @SoftUni. Players are presented with a historical pictures and must guess the time period and its location. Players can compete against one another with the daily challenge.

## Features
* Guess the time period and location of historical pictures.
* Daily challenges with a leaderboard.
* User authentication and role-based access.
* Google Maps integration for location-based guesses.
* Dropbox integration for storing the pictures.

## Technologies Used
* Java 17: The primary programming language.
* Spring Boot 3.2.4: For building the application.
* Spring Data JPA: For database interactions.
* Spring Security: For securing the application.
* Thymeleaf: For server-side rendering of web pages.
* MySQL: For persistent storage of game data.
* Hibernate Validator: For validating inputs.
* ModelMapper: For object mapping.
* Google Maps API: For integrating map-based functionality.
* Dropbox API: For storing and retrieving pictures.
* Maven: For project management and dependency handling.
* JUnit: For testing.

## Installation
Java: Ensure you have Java 17 or later installed.  
Maven: Make sure Maven is installed for dependency management.  
MySQL: Set up a MySQL database to use with the project. 

### Steps
1.  **Clone the repository**:
   ```bash
   git clone https://github.com/nbzhk/HistoryGuessr.git 
   ```
2. **Navigate to the directory**
   ```bash
   cd HistoryGuessr
   ```
3. **Configure the Database**  
    * Create a database to store the game data
    * Update the following environment variables with your database details
    ```bash
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```
4. **Configure API keys**  
    * Obtain your keys for the Google Maps API and the Dropbox API
    * Update the Properties accordingly     
    ```bash
    #Google Maps
    google.maps.key=${GOOGLE_MAP_KEY}

    #Dropbox Properties
    dropbox.appKey=${DROPBOX_APP_KEY}
    dropbox.appSecret=${DROPBOX_APP_SECRET}
    ```
 5. Configure Additional Environment Variables
To ensure that the scheduler and REST client function correctly, you need to set the BASE_URL environment variable:
 ```bash
#Example for localhost:8080
BASE_URL=http://localhost:8080
```

7. **Build the project:**  
Compile and package the project using Maven:  
    ```bash
    mvn clean install
    ````
    
8. **Run the application**  
    ```bash
    mvn spring-boot:run
    ```
9. **Access the application**  
Once the application is running, open your web browser and navigate to: 

    http://localhost:8080
    
## Default Admin Account
An admin user is created during initialization. You can log in with the following credentials:

    Username: admin
    Password: admin

You can also create a new account, though it will not have admin rights by default. Only an admin can promote other users to admin status.

## Deployed Test Version
You can try out a deployed test version of the History Guessr application at the following URL:  

[https://historyguessr-production.up.railway.app/](https://historyguessr-1001685189936.europe-west1.run.app/)

Feel free to explore the app and test its features. Please note that this is a test environment and may have limited functionality.


    
