pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn -B clean test'
            }
            post {
                always {
                    junit(
                        testResults: 'course-registration-app/target/surefire-reports/TEST-*.xml',
                        allowEmptyResults: false
                    )
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -B package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts(
                    artifacts: 'course-registration-app/target/*.jar',
                    fingerprint: true
                )
            }
        }
    }
}