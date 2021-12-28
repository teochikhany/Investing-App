pipeline {
    agent any

    stages {

        stage('setup dev env') {
            steps {
                bat 'docker-compose -f ./docker-compose-dev.yaml up -d'
            }
        }

        stage('Test Backend') {
            steps {
                bat 'docker exec backend-dev mvn clean test'
            }
        }

        stage('Test Frontend') {
            steps {
                bat 'echo todo'
            }
        }

        stage('Build Backend') {
            steps {
                bat 'docker exec backend-dev mvn clean install -DskipTests'
            }
        }

        stage('Build Frontend') {
            steps {
                bat 'docker exec frontend-dev ng build --output-path=/output'
                bat 'docker exec frontend-dev rm -rf /home/angular/dist/'
                bat 'docker exec frontend-dev mkdir -p /home/angular/dist/'
                bat 'docker exec frontend-dev mv /output /home/angular/dist/investing-frontend'
            }
        }

        //TODO: should use withCredentials instead of environment variables
        stage('Deploy') {
            steps { 
                bat "docker login -u ${env.dockerUsername} -p ${env.dockerPassword}"
                bat 'docker-compose -f ./docker-compose-prod.yaml build'
                bat 'docker-compose -f ./docker-compose-prod.yaml push'
            }
        }
    }

    post {
        always {
            echo 'Done, cleaning up'
            bat 'docker-compose -f ./docker-compose-dev.yaml down'
            bat 'docker rmi investing-app-frontend-dev'
            bat 'docker rmi investing-app-backend-dev'
        }
        success {
            echo 'I succeeded!'
            bat 'docker rmi teochikhany/investing-app-backend-prod:0.1'
            bat 'docker rmi teochikhany/investing-app-frontend-prod:0.1'
        }
        unstable {
            echo 'I am unstable :/'
        }
        failure {
            echo 'I failed :('
        }
        changed {
            echo 'Things were different before...'
        }
    }
}