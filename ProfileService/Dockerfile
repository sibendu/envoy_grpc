FROM openjdk:8
 
COPY build/distributions/ProfileService.zip  ProfileService.zip 

unzip -o ProfileService.zip

EXPOSE 8980


ENTRYPOINT ["/AService/bin/profile-server"]