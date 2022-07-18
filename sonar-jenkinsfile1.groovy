pipeline {
    agent none
    stages {
        stage('Checkout SCM') {
            agent { label 'newnode1' }
            steps {
                git url: 'https://github.com/harishtallam/java-sonar.git', branch: 'main'
            }
        }

        stage('Build'){
            agent { label 'newnode1' }
            steps {
                sh 'mvn clean package'
            }
        }

        stage('SonarQube Analysis') {
            agent { label 'newnode1' }
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
    }
}