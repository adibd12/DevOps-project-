import groovy.json.JsonSlurper
pipeline {
   agent any
   stages {
      stage('git') {
           steps {
              git branch: "main",
              credentialsId: 'my-credentials',
              url: 'https://github.com/adibd12/DevOps-project-.git'
         }
      }
      stage('push to docker hub') {
         steps {
            build image: { 
               sh "docker build -t python-web-app -f python.Dockerfile ."
            },
            login: {
               sh "docker login -u $USER -p $PASSWORD docker.io"
            },
            push to docker hub: {
               sh "docker tag python-web-app $USER/python-web-app && docker push $USER/python-web-app"
            }
         }
      }
      stage('run web app & nginx') {
         steps {
            build: {
               sh "docker image build -t nginx -f nginx.Dockerfile ."
            },
            push to docker hub: {
               sh "docker tag nginx $USER/nginx && docker push $USER/nginx"
            },               
            run: {
               sh "docker run --name nginx-app -p 80:80 -v /home/jenkins/nginx.conf:/etc/nginx/nginx.conf -d nginx"
            },            
            containers list: {
               sh "docker ps --format "{{.Names}}" > contianers.txt"
            },
            web app: {
               sh "docker run --name flask-app -p 5000:5000 -d python-web-app"
            }               
         }
      }
      stage('Response') {
         steps {
            script {
               def response = httpRequest 'http://127.0.0.1:5000'
               def json = new JsonSlurper().parseText(response.content)
               echo "Status: ${response.status}"
            }
         }
      }
   }
}

