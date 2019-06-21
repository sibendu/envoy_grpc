FROM openjdk:8
 
COPY build/distributions/ProfileService.zip  ProfileService.zip 

RUN unzip -o ProfileService.zip

EXPOSE 8980

ENTRYPOINT ["/ProfileService/bin/profile-server"]