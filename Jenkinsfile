
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
                    for i in $(seq 1 30); do
                        if curl -fsS \
                            "http://host.docker.internal/actuator/health" \
                            >/dev/null 2>&1; then

                            echo "Health check passed"
                            exit 0
                        fi

                        sleep 2
                    done

                    echo "Health check failed"
                    exit 1
                '''
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }

        success {
            echo "Pipeline succeeded: build ${BUILD_NUMBER}"
        }

        failure {
            echo "Pipeline failed: inspect the stage logs."
        }
    }
}
