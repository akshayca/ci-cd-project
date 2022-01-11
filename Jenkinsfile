#!/usr/bin/env groovy

pipeline {
    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_REPO_SERVER = '664574038682.dkr.ecr.eu-west-3.amazonaws.com'
        DOCKER_REPO = "${DOCKER_REPO_SERVER}/java-maven-app"
    }

    agent any
    stages {
        stage('build app') {
            steps {
                script {
                    echo "Building the application..."
                    bat 'mvn clean package'
                }
            }
        }
        stage('build image') {
            steps {
                script {
                    echo "building the docker image..."
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        // bat "docker build -t ${DOCKER_REPO}:${IMAGE_NAME} ."
                        // bat "echo $PASS | docker login -u $USER --password-stdin ${DOCKER_REPO_SERVER}"
                        // bat   93333 "docker push ${DOCKER_REPO}:${IMAGE_NAME}"
                        bat "docker build -t akshayca23/ci-cd-project:jma-1.0 ."
                        bat "echo %PASS%"
                        bat "echo %PASS% | docker login -u %USER% --password-stdin"  
                        bat "docker push akshayca23/ci-cd-project:jma-1.0"
                    }
                }
            }
        }
        stage('test') {
            steps {
                script {
                    echo "Testing the application..."
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                    echo "Deploying the application..."
                }
            }
        }
    }
}
