#!/usr/bin/env groovy

library identifier: 'jenkins-shared-library@master', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/akshayca/jenkins-shared-library.git',
         credentialsId: 'github-credentials'
        ]
)

def gv
pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    environment {
        DOCKER_REPO_SERVER = '745874038936.dkr.ecr.us-east-1.amazonaws.com'
        DOCKER_REPO = "${DOCKER_REPO_SERVER}/java-maven-app"
    }

    stages {
        stage('Init'){
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage('increment version') {
            steps {
                script {
                    incrementversion()
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    def repo = 'akshayca23/ci-cd-project'
                    env.IMAGE_NAME = "$repo:$version-$BUILD_NUMBER"
                    echo "Image name - > ${env.IMAGE_NAME}"
                }
            }
        }


        stage('build Jar') {
            steps {
                script {
                    echo "building the application for branch ${env.BRANCH_NAME}"
                    buildJar()
                }
            }
        }
        stage('build and push image') {
            steps {
                script {
                    buildImage(env.IMAGE_NAME)
                    dockerLogin()
                    dockerPush(env.IMAGE_NAME)
                }
            }
        }
        stage('test') {
            steps {
                script {
                    gv.testApp()
                }
            }
        }
        stage('deploy') {
            environment {
                AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
                AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
                APP_NAME = 'java-maven-app'
            }
            steps {
                script {
                    echo 'deploying docker image...'
                    sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -'
                    sh 'envsubst < kubernetes/service.yaml | kubectl apply -f -'
                }
            }
        }

        stage('commit version update') {
            steps {
                script {
                        withCredentials([string(credentialsId: 'github-pta', variable: 'SECRET')]) {
                        sh 'git config user.email "jenkins@example.com"'
                        sh 'git config user.name "Jenkins"'
                        sh "git remote set-url origin https://${SECRET}@github.com/akshayca/ci-cd-project.git"
                        sh 'git add .'
                        sh 'git commit -m "ci: version bump"'
                        sh 'git push origin HEAD:master'
                    }
                }
            }
        }

    }
}
