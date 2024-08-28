pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Check out the code from your repository
                    checkout scm
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Use a virtual environment for Python
                    sh 'python -m venv venv'
                    sh 'source venv/bin/activate'

                    // Install dependencies
                    sh 'pip install -r pydriller/requirements.txt'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Run your tests
                    sh 'cd pydriller && pytest'
                }
            }
        }
    }

    post {
        always {
            // Clean up
            sh 'deactivate'
        }
    }
}
