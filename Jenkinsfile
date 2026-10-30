
pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = 'naveed938/calculator-api'
        COMPOSE_PROJECT_NAME = 'mini_project'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: false
                }
            }
        }

        stage('Docker Hub Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'docker-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    sh '''
                        set +x
                        echo "$DOCKER_TOKEN" | docker login \
                            -u "$DOCKER_USER" --password-stdin
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t ${DOCKERHUB_REPO}:${BUILD_NUMBER} .

                    docker tag ${DOCKERHUB_REPO}:${BUILD_NUMBER} \
                        ${DOCKERHUB_REPO}:latest

                    docker tag ${DOCKERHUB_REPO}:${BUILD_NUMBER} \
                        calculator-api:1.0
                '''
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sh '''
                    docker push ${DOCKERHUB_REPO}:${BUILD_NUMBER}
                    docker push ${DOCKERHUB_REPO}:latest
                '''
            }
        }

        stage('Build Nginx Image') {
            steps {
                sh 'docker compose build nginx'
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh '''
                    docker compose up -d --no-build
                    docker compose ps
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                sh '''
                    echo "Checking deployment health..."

                    for i in $(seq 1 15); do

                        if docker exec mini_project-nginx-1 \
                            wget -qO- http://127.0.0.1/actuator/health; then

                            echo "Deployment verification successful!"
                            exit 0
                        fi

                        echo "Attempt $i failed. Retrying..."
                        sleep 2
                    done

                    echo "Deployment verification failed!"
                    exit 1
                '''
            }
        }
    }

//    post {
//        always {
//            sh 'docker logout || true'
//        }
//
//        success {
//            emailext(
//                to: 'syednaveedmohammed@gmail.com',
//                subject: "SUCCESS: Calculator CI/CD - Build #${BUILD_NUMBER}",
//                mimeType: 'text/plain',
//                body: """
//    Your Calculator CI/CD pipeline completed successfully!
//
//    Build Number: ${BUILD_NUMBER}
//    Build URL: ${BUILD_URL}
//
//    All stages completed successfully.
//    The application has been deployed using Docker Compose.
//    """
//            )
//        }
//
//        failure {
//            emailext(
//                to: 'syednaveedmohammed@gmail.com',
//                subject: "FAILURE: Calculator CI/CD - Build #${BUILD_NUMBER}",
//                mimeType: 'text/plain',
//                body: """
//    Your Calculator CI/CD pipeline has FAILED.
//
//    Build Number: ${BUILD_NUMBER}
//    Build URL: ${BUILD_URL}
//
//    Please check the Jenkins console output for details.
//    """
//            )
//        }
//    }
    post {
        success {
            echo 'Build passed'
            mail to: 'syednaveedmohammed@gmail.com',
                    subject: "Build PASSED: ${currentBuild.fullDisplayName}",
                    body: "Result: ${currentBuild.currentResult}\nDetails: ${env.BUILD_URL}"
        }
        failure {
            echo 'Build failed'
            mail to: 'syednaveedmohammed@gmail.com',
                    subject: "Build FAILED: ${currentBuild.fullDisplayName}",
                    body: "Result: ${currentBuild.currentResult}\nDetails: ${env.BUILD_URL}"
        }
    }
}
