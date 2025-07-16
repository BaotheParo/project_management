pipeline {
    agent {
        label 'livesvbao'
    }
    environment {
        projectname = 'project_management'
        version = '0.0.1-SNAPSHOT'
        projectuser = 'admin'
        projectpath = '/project_management'
    }
    stages {
        stage('Build') {
            steps {
                script {
                    // Set Git strategy
                    env.GIT_STRATEGY = 'clone'
                    // Run Maven build
                    sh 'mvn clean install -DskipTests=true'
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Set Git strategy
                    env.GIT_STRATEGY = 'none'
                    // Copy the JAR file and run the application
                    sh """
                        sudo cp target/${projectname}-${version}.jar ${projectpath}
                        sudo chown -R ${projectuser}. ${projectpath}
                        sudo -u ${projectuser} nohup java -jar ${projectpath}/${projectname}-${version}.jar > nohup.out 2>&1 &
                    """
                }
            }
        }
    }
}
