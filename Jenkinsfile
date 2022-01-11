#!/usr/bin/env groovy

def gv
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
        stage('Init'){
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage('build app') {
            steps {
                script {
                    gv.buildJar()
                }
            }
        }
        stage('build image') {
            steps {
                script {
                    gv.buildImage()
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
