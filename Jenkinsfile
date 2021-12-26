pipeline {
    agent any
    
    stages {

        stage('launch dev docker compose') {
            steps {
                bat 'docker-compose -f ./docker-compose-dev.yaml up -d'
            }
        }

        stage('Build Backend') {
            steps {
                bat 'docker exec backend-dev mvn clean install -DskipTests'
            }
        }

        stage('Build Frontend') {
            steps {
                bat 'docker exec frontend-dev ng build'
            }
        }

        stage('Test Backend') {
            steps {
                bat 'docker exec backend-dev mvn test'
            }
        }

        stage('Test Frontend') {
            steps {
                bat 'docker exec frontend-dev ng test'
            }
        }

        stage('Clean up') {
            steps {
                bat 'docker-compose -f ./docker-compose-dev.yaml down'
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker-compose -f ./docker-compose-prod.yaml up -d'
            }
        }
    }
}