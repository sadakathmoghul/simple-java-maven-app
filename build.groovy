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
						mvnn clean install
					
					'''

				}
            }
        }
		
		stage('Deploy Stage') {
            steps {
                script {
				
					sh '''
												
						sudo cp -rv target/*.jar /usr/share/tomcat/webapps
						#sudo cp -rv $HOME/jenkins/SampleWebApp.war /usr/share/tomcat/webapps
					
					'''

				}
            }
        }
    }
	
	post {
		    always {
				mail to: "tvharish513@gmail.com",
			    subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - ${currentBuild.currentResult}!",
			    body: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - ${currentBuild.currentResult}!"
				//mimeType: "text/html"
		    }
	    }
}