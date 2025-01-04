@Library('My-Shared-Library@main') _  // Correct syntax

pipeline {
    agent { label 'slave' }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        MAVEN_HOME = '/usr/share/maven'
        PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('All code') {
            steps {
                All()
            }
        }

        
    }

    post {
        always {
            cleanup()
        }
    }
}
