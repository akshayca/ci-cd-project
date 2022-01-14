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
        IMAGE_REPOV = 'akshayca23/ci-cd-project:jma-1.0'
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
//                     gv.deployApp()
                    // def shellCmd = "bash ./server-cmds.sh ${IMAGE_NAME}"
                    def shellCmd = "sudo docker run -p 3080:3080 -d ${IMAGE_REPOV}"
                    def ec2Instance = "ec2-user@3.83.47.208"

                   sshagent(['ec2-server-key']) {
                    //    sh "scp -o StrictHostKeyChecking=no server-cmds.sh ${ec2Instance}:/home/ec2-user"
                    //    sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:/home/ec2-user"
                       sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${shellCmd}"
                   }
   
                }
            }
        }
        stage('commit version update') {
            steps {
                script {
                        withCredentials([string(credentialsId: 'github-pta', variable: 'SECRET')]) {
                        sh 'git config user.email "jenkins@example.com"'
                        sh 'git config user.name "Jenkins"'
                        sh "git remote set-url origin https://${PASS}@github.com/akshayca/ci-cd-project.git"
                        sh 'git add .'
                        sh 'git commit -m "ci: version bump"'
                        sh 'git push origin HEAD:master'
                    }
                }
            }
        }

    }
}
