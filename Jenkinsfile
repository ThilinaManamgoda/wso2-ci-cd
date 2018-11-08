node('master'){
        env.GIT_REPO='https://github.com/ThilinaManamgoda/wso2-ci-cd-artifacts.git';
        stage('Load Env') {
            file = load '/home/ubuntu/jenkins_env'
        }
        stage('Preparation') {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: env.GIT_REPO]]])
        }
        stage('Generate Pack with configs') {
          withCredentials([usernamePassword(credentialsId: 'wum_creds', passwordVariable: 'WUM_PASSWORD', usernameVariable: 'WUM_USERNAME')]) {
              sh '''
                set +x
                /usr/local/wum/bin/wum init -u $WUM_USERNAME -p $WUM_PASSWORD
              '''
          }
        }
        stage('Generate AMI') {
            echo('Generate AMI')
        }


        stage('extract') {
             def WSO2InstanceType="WSO2InstanceType=${env.WSO2InstanceType}"
             def KeyPairName="KeyPairName=${env.KeyPairName}"
             def CertificateName="CertificateName=${env.CertificateName}"
             def DBUsername="DBUsername=${env.DBUsername}"
             def DBPassword="DBPassword=${env.DBPassword}"
             def JDKVersion="JDKVersion=${env.JDKVersion}"

             withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws_creds']]) {
                 def AWSAccessKeyId="AWSAccessKeyId=${env.AWS_ACCESS_KEY_ID}"
                 def AWSAccessKeySecret="AWSAccessKeySecret=${env.AWS_SECRET_ACCESS_KEY}"
                 withAWS(credentials:'aws_creds',region: env.REGION) {
                   def outputs = cfnUpdate(stack:'my-stack', file:'/home/ubuntu/tmp.yaml', params:[AWSAccessKeyId, AWSAccessKeySecret,WSO2InstanceType, KeyPairName, CertificateName, DBUsername, DBPassword, JDKVersion], timeoutInMinutes:20, pollInterval:1000)
                 }
             }


         }
        stage('Deploy AWS Staging  ') {
              withAWS(credentials:'aws_creds') {
                  s3Upload(file:'test1.txt', bucket:'jenkinspipeline123', path:'test1.txt')
              }
        }
        stage('Running Tests') {

        }
        stage('Deploy AWS Production') {
                withAWS(credentials:'aws_creds') {
                    s3Upload(file:'test1.txt', bucket:'jenkinspipeline123', path:'test1.txt')
                }
        }

}
