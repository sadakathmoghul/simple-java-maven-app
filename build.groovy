pipeline {
	//agent { label 'new-linux-node' }
	agent any
    stages {
        stage('Pre-Requisites') {
            steps {
                script {
			
			
					sh '''
						hostname
						hostname -i
						/sbin/ifconfig | grep inet | grep broadcast
						echo $WORKSPACE
					
					'''

				}
            }
        }
		
		stage('Build Stage') {
            steps {
                script {
				
					sh '''
						
						cd $WORKSPACE
						mvn clean install
					
					'''

				}
            }
        }
		
		stage('Deploy Stage') {
            steps {
                script {
				
					sh '''
						ls -lrt						
						sudo cp -rv target/*.jar /home/ec2-user/apache-tomcat-7.0.32/webapps
						#sudo cp -rv $HOME/jenkins/SampleWebApp.war /usr/share/tomcat/webapps
					
					'''

				}
            }
        }
    }
	
	post {
		    always {
				mail to: "sadakath.moghul@gmail.com",
			    subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - ${currentBuild.currentResult}!",
			    body: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - ${currentBuild.currentResult}!"
				//mimeType: "text/html"
		    }
	    }
}
