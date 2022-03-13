FROM ubuntu:latest

RUN apt-get update && apt-get upgrade -y

RUN apt-get install nginx -y

EXPOSE 80

# Last is the actual command to start up NGINX within our Container
CMD ["nginx", "-g", "daemon off;"]
