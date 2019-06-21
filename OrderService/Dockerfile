FROM openjdk:8
 
COPY build/distributions/OrderService.zip  OrderService.zip 

RUN unzip -o OrderService.zip

EXPOSE 8980
ENTRYPOINT ["/OrderService/bin/profile-server"]