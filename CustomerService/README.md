# ABService

Provides an REST API aggregating data from two sub-APIs

Implemented in Spring Boot

GET /customer/{id}

Calls -
1. ServiceA over gRPC -> returns details of the customer
2. ServiceB over gRPC -> returns order history of the customer

