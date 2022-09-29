pipeline {
  agent any
  stages {
    stage('git') {
      steps {
         
         checkout scm
      }
    }
    
   stage('Build') {
            steps {
                echo '开始执行打包操作.......'
                sh "mvn clean package  deploy -U"
            }
        }

 
   }
}