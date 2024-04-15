## Gym CRM Project

The Gym CRM (Customer Relationship Management) project is designed to facilitate the management of a gym's operations and interactions with its customers. This software system provides a centralized platform for managing various aspects of gym operations, including managing trainees, trainers, and training sessions.


### Getting start

1. Clone the repository


    git clone https://github.com/kol2104/gym-crm


2. Change the directory


    cd gym-crm


3. Build service


    mvn clean package

4. Run service


    java -jar target/gym-crm-0.0.1-SNAPSHOT.jar


Service launched!

Service launched on 8080 port.

## Next steps

You can log in on `/api/login` endpoint as trainee or trainer. 
You can register and manipulate trainee/trainer on `/api/trainees` and `/api/trainers` endpoints.
`/api/trainings` uses for add and get trainings.
To get all training types use `/api/training-types` endpoint.

You can see all possible endpoints with correct 
documentation on `/swagger-ui.html` endpoint or if you prefer JSON format use `/v3/api-docs` endpoint

