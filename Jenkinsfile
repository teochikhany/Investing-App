pipeline {
    agent any
    
    stages {

        stage('launch dev docker compose') {
            steps {
                sh 'docker-compose -f ./docker-compose-dev.yaml up'
            }
        }

        stage('Build Backend') {
            steps {
                sh 'docker container attach backend-dev'
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Frontend') {
            steps {
                sh 'docker container attach frontend-dev'
                sh 'ng build'
            }
        }

        stage('Test Backend') {
            steps {
                sh 'docker container attach backend-dev'
                sh 'mvn test'
            }
        }

        stage('Test Frontend') {
            steps {
                sh 'docker container attach frontend-dev'
                sh 'ng test'
            }
        }

        stage('Clean up') {
            steps {
                sh 'docker-compose -f ./docker-compose-dev.yaml down'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose -f ./docker-compose-prod.yaml up'
            }
        }
    }
}