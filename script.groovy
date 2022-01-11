def buildJar(){
    echo "Building the application..."
    bat 'mvn clean package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        // bat "docker build -t ${DOCKER_REPO}:${IMAGE_NAME} ."
        // bat "echo $PASS | docker login -u $USER --password-stdin ${DOCKER_REPO_SERVER}"
        // bat   93333 "docker push ${DOCKER_REPO}:${IMAGE_NAME}"
        bat "docker build -t akshayca23/ci-cd-project:jma-1.0 ."
        bat "docker login -u %USER% -p %PASS%"
        //bat "echo %PASS% | docker login -u %USER% --password-stdin"  
        bat "docker push akshayca23/ci-cd-project:jma-1.0"
    }
}

def testApp(){
    echo "Testing the application..."
}

def deployApp(){
    echo "Deploying the application..."
}

return this