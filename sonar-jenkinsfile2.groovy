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
                timeout(time: 1, unit: 'MINUTES') {
                script {
                    withSonarQubeEnv('sonarqube') {
                         sh 'mvn sonar:sonar'
                    }
                    qualitygate = waitForQualityGate()                     
                    if (qualitygate.status != "OK") {                         
                    currentBuild.result = "UNSTABLE"
                    error "Pipeline aborted due to quality gate failure: ${qualitygate.status}"                     
                    } 
                }
                }
            }
        }
    }
}