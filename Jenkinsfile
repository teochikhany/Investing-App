pipeline {
    agent any
    
    stages {

        stage('setup dev env') {
            steps {
                bat 'docker-compose -f ./docker-compose-dev.yaml up -d'
            }
        }

        // TODO: why does this dowload stuff, even though everything should be installed per the previous step ?
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

        // TODO: delete all images and containers
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