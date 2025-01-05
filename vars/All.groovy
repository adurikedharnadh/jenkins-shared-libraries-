def buildproject() {
    echo 'Building project with Maven...'
    sh 'mvn clean package'
}

def checkoutcode() {
    echo 'Checking out code...'
    checkout scm
}

def cleanup() {
    echo 'Cleaning up...'
    sh 'pkill -f "mvn spring-boot:run" || true'
}

def cleanupworkspace() {
    echo 'Cleaning up the workspace...'
    deleteDir() // Deletes all files in the current workspace
}

def runapplication() {
    echo 'Running Spring Boot application...'
    sh 'nohup mvn spring-boot:run &'
    sleep(time: 15, unit: 'SECONDS')

    def publicIp = sh(script: "curl -s https://checkip.amazonaws.com", returnStdout: true).trim()
    echo "The application is running and accessible at: http://${publicIp}:8080"
}

def setupjava() {
    echo 'Setting up Java 17...'
    sh 'sudo apt update'
    sh 'sudo apt install -y openjdk-17-jdk'
}

def setupmvn() {
    echo 'Setting up Maven...'
    sh 'sudo apt install -y maven'
}

def stopmvn() {
    echo 'Gracefully stopping the Spring Boot application...'
    sh 'mvn spring-boot:stop'
}

def tagbuild(String tagName, String message = 'Build tagging') {
    echo "Tagging the build with tag: ${tagName}"
    
    // Tagging the current commit in Git
    sh """
        git tag -a ${tagName} -m '${message}'
        git push origin ${tagName}
    """
}

def aploadArtifcat(String artifactPath) {
    echo 'Uploading artifact...'
    archiveArtifacts artifacts: artifactPath, allowEmptyArchive: true
}

def validateapp() {
    echo 'Validating that the app is running...'
    def response = sh(script: 'curl --write-out "%{http_code}" --silent --output /dev/null http://localhost:8080', returnStdout: true).trim()
    if (response == "200") {
        echo 'The app is running successfully!'
    } else {
        echo "The app failed to start. HTTP response code: ${response}"
        error("The app did not start correctly!")
    }
}
