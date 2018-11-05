pipeline {
    agent any
    String repositoryName='wso2-ci-cd-artifacts';
    stages {
        stage('Preparation') {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']],
                      doGenerateSubmoduleConfigurations: false,
                      extensions                       : [[$class: 'LocalBranch', localBranch: 'master'],
                                                          [$class: 'RelativeTargetDirectory', relativeTargetDir: repositoryName]],
                      submoduleCfg: [],
                      userRemoteConfigs: [[url: 'https://github.com/ThilinaManamgoda/wso2-ci-cd-artifacts']]]);
        }
        stage('Build') {
            steps {
                sh 'echo "Hello World"'
                sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
                dir(repositoryName) {
                        sh "pwd"

                }
            }
        }
    }
}
