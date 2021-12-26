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
                bat 'docker container attach backend-dev'
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Frontend') {
            steps {
                bat 'docker container attach frontend-dev'
                sh 'ng build'
            }
        }

        stage('Test Backend') {
            steps {
                bat 'docker container attach backend-dev'
                sh 'mvn test'
            }
        }

        stage('Test Frontend') {
            steps {
                bat 'docker container attach frontend-dev'
                sh 'ng test'
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