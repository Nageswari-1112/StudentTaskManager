pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/nageswari-1112/StudentTaskManager.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t student-task-manager .'
            }
        }

        stage('Run Container') {
            steps {
                sh 'docker run -d -p 3000:3000 student-task-manager'
            }
        }
    }
}