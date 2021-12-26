pipeline {
    agent any
    
    stages {

        stage('Build Backend') {
            agent {
                dockerfile {
                    filename 'Dockerfile-Dev'
                    dir './backend'
                    args '-v ./backend/:/home/springboot/ mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            agent {
                dockerfile {
                    filename 'Dockerfile-Dev'
                    dir './frontend'
                    args '-v ./frontend/:/home/angular/ ng build'
                }
            }
        }

        stage('Test Backend') {
            agent {
                dockerfile {
                    filename 'Dockerfile-Dev'
                    dir './backend'
                    args '-v ./backend/:/home/springboot/ mvn test'
                }
            }
        }

        stage('Test Frontend') {
            agent {
                dockerfile {
                    filename 'Dockerfile-Dev'
                    dir './frontend'
                    args '-v ./frontend/:/home/angular/ ng test'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose -f ./docker-compose-prod.yaml up'
            }
        }
    }
}