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
        IMAGE_REPO = 'akshayca23/ci-cd-project'
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
                    buildImage "${IMAGE_REPO}:${IMAGE_NAME}"
                    dockerLogin()
                    dockerPush "${IMAGE_REPO}:${IMAGE_NAME}"

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
        stage('commit version update') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        bat 'git config user.email "jenkins@example.com"'
                        bat 'git config user.name "Jenkins"'
                        bat "git remote set-url origin https://${USER}:${PASS}@github.com/akshayca/ci-cd-project.git"
                        bat 'git add .'
                        bat 'git commit -m "ci: version bump"'
                        bat 'git push origin HEAD:master'
                    }
                }
            }
        }

    }
}
