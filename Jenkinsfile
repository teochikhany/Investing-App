pipeline {
    agent any

    stages {

        stage('setup dev env') {
            steps {
                bat 'docker-compose -f ./docker-compose-dev.yaml up -d'
            }
        }

        // TODO: fix: the jar it produces is not saved for later deployment
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

        stage('Test Backend') {
            steps {
                bat 'docker exec backend-dev mvn clean test'
            }
        }

        stage('Test Frontend') {
            steps {
                bat 'echo todo'
                // bat 'docker exec frontend-dev ng test'
            }
        }

        stage('Deploy') {
            steps { 
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