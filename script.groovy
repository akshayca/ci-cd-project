def buildJar(){
    echo "Building the application..."
    sh 'mvn clean package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t ${IMAGE_NAME} ."
        sh "echo $PASS | docker login -u $USER --password-stdin ${DOCKER_REPO_SERVER}"
        sh  "docker push ${IMAGE_NAME}"
    }
}

def testApp(){
    echo "Testing the application..."
}

def deployApp(){
    echo "Deploying the application..."
}

return this