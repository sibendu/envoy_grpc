FROM openjdk:8
 
COPY build/distributions/OrderService.zip  OrderService.zip 
unzip -o OrderService.zip

EXPOSE 8980
ENTRYPOINT ["/OrderService/bin/order-server"]