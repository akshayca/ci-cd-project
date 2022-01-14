def buildJar(){
    echo "Building the application..."
    sh 'mvn clean package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        // sh "docker build -t ${DOCKER_REPO}:${IMAGE_NAME} ."
        // sh "echo $PASS | docker login -u $USER --password-stdin ${DOCKER_REPO_SERVER}"
        // sh   93333 "docker push ${DOCKER_REPO}:${IMAGE_NAME}"
        sh "docker build -t akshayca23/ci-cd-project:jma-1.0 ."
        // sh "docker login -u %USER% -p %PASS%"
        sh "echo %PASS% | docker login -u %USER% --password-stdin"  
        sh "docker push akshayca23/ci-cd-project:jma-1.0"
    }
}

def testApp(){
    echo "Testing the application..."
}

def deployApp(){
    echo "Deploying the application..."
}

return this