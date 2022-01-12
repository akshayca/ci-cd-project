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
        DOCKER_REPO_SERVER = '664574038682.dkr.ecr.eu-west-3.amazonaws.com'
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
                    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
                    echo "############ ${env.IMAGE_NAME}"
                }
            }
        }


        stage('build Jar') {
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage('build and push image') {
            steps {
                script {
                    buildImage 'akshayca23/ci-cd-project:jma-2.0'
                    dockerLogin()
                    dockerPush 'akshayca23/ci-cd-project:jma-2.0'

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
            steps {
                script {
                    gv.deployApp()   
                }
            }
        }
    }
}
