node('master'){
    String repositoryName='wso2-ci-cd-artifacts';

        stage('Preparation') {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/ThilinaManamgoda/wso2-ci-cd-artifacts.git']]])
        }
        stage('Build') {

                sh 'echo "Hello World"'
                sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
                dir(repositoryName) {
                        sh 'pwd'
                        sh 'ls'


            }
        }

}
