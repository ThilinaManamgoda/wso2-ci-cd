node('master'){

        // Stages
        def LOAD_ENV='Load Envs'
        def PREPARATION='Preparation'
        def GENERATE_PACK='Generate Pack with configs'
        def GENERATE_AMI='Generate AMI'
        def PROD_DEPLOYMENT='Production deployment'
        def RUNNING_TEST='Running Tests'
        def STAGING_DEPLOYMENT='Staging Deployment'

        //Config Files
        def ENV_FILE='/home/jenkins/jenkins_env'
        def PUPPET_CONF_FILE="/home/jenkins/conf-home/script/apply-config.sh"
        def CF_FILE_DIR="/home/jenkins/cf"
        def CF_FILE="$CF_FILE_DIR/cf.yaml"
        def PACKER_MANIFEST='/home/jenkins/packer/manifest.json'
        env.PACKER_JSON='/home/jenkins/packer/packer-puppetmaster_kavindu.json'
        env.AWS_CREDS_FILE='/home/ubuntu/.aws/credentials'
        def WUM_CREDS='wum_creds'
        def AWS_CREDS='aws_creds1'
        def PUPET_CONF_DIR='/home/jenkins/conf-home/modules/'

        stage(LOAD_ENV) {
            echo "####################################### Loading Environment variables #######################################"
            file = load ENV_FILE
        }
        stage(PREPARATION) {
            echo "##################################### Cloning Git repositories #####################################"
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: env.GIT_REPO_ARTIFACTS]]])
            dir(CF_FILE_DIR) {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: env.GIT_REPO_CF]]])
            }
            dir(PUPET_CONF_DIR) {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: env.GIT_REPO_PUPPET]]])
            }
        }
        stage(GENERATE_PACK) {
          echo "##################################### Generate Pack with configs #####################################"
          env.ARTIFACT_LOC='/var/lib/jenkins/workspace/Test/carbon-home'
          withCredentials([usernamePassword(credentialsId: WUM_CREDS, passwordVariable: 'WUM_PASSWORD', usernameVariable: 'WUM_USERNAME')]) {
          BUILD_FULL = sh (
                                script: PUPPET_CONF_FILE,
                                returnStatus: true
                            )
          if (BUILD_FULL==1) {
                handleError(GENERATE_PACK,1)
            }
          }
        }

        stage(GENERATE_AMI) {
            echo "##################################### Generate AMI #####################################"
            env.PRODUCT_DIST="${PRODUCT}-${VERSION}.zip"
            BUILD_FULL = sh (
                                script: '''
                                 export AWS_SHARED_CREDENTIALS_FILE=$AWS_CREDS_FILE
                                 packer build  -var "product=$PRODUCT_DIST" $PACKER_JSON
                                ''',
                                returnStatus: true
                            )

            if (BUILD_FULL==1) {
                handleError(GENERATE_PACK,1)
            }

            def packer_post=readJSON file: PACKER_MANIFEST, text: ''
            def ami_info=packer_post.builds.artifact_id[0]
            def (value1, value2) = "$ami_info".tokenize( ':' )
            env.AMI_ID=value2
            echo "AMI_ID: $AMI_ID"
            sh "rm -f $PACKER_MANIFEST"
            env.REGION=sh (
                                script: '''
                                 ec2metadata --availability-zone | sed \'s/[a-z]$//\'
                                ''',
                                returnStdout: true
                            ).trim()
        }

        stage(STAGING_DEPLOYMENT) {
            echo "##################################### Deploying to Production ######################################"
            def STAGING_STACK="staging-stack"
            def WSO2InstanceType="WSO2InstanceType=${env.WSO2InstanceType}"
            def KeyPairName="KeyPairName=${env.KeyPairName}"
            def CertificateName="CertificateName=${env.CertificateName}"
            def DBUsername="DBUsername=${env.DBUsername}"
            def DBPassword="DBPassword=${env.DBPassword}"
            def JDKVersion="JDKVersion=${env.JDKVersion}"
            def AMIID="AMIid=${env.AMI_ID}"
            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: AWS_CREDS]]) {
                def AWSAccessKeyId="AWSAccessKeyId=${env.AWS_ACCESS_KEY_ID}"
                def AWSAccessKeySecret="AWSAccessKeySecret=${env.AWS_SECRET_ACCESS_KEY}"

                withAWS(credentials: AWS_CREDS,region: env.REGION) {
                  def outputs = cfnUpdate(stack: STAGING_STACK, file: CF_FILE, params:[AWSAccessKeyId, AWSAccessKeySecret,WSO2InstanceType, KeyPairName, CertificateName, DBUsername, DBPassword, JDKVersion, AMIID], timeoutInMinutes:20, pollInterval:1000)
                  env.TEST_URL = outputs.'ESBHttpUrl'
                  echo "$TEST_URL"
                //   echo "Deleting STACK"
                //   sleep time: 1, unit: 'MINUTES'
                //   cfnDelete(stack: PROD_STACK, pollInterval:1000)
                }
            }
        }

        stage(RUNNING_TEST) {
            // echo(env.AWS_ACCESS_KEY_ID)
            TEST_PASS = sh (
                                script: '''
                                 ./test.sh
                                ''',
                                returnStatus: true
                            )

            if (TEST_PASS==1) {
                handleError(RUNNING_TEST,1)
            }
        }

        // stage(PROD_DEPLOYMENT) {
        //     echo "##################################### Deploying to Production ######################################"
        //     def PROD_STACK="prod-stack"
        //     def WSO2InstanceType="WSO2InstanceType=${env.WSO2InstanceType}"
        //     def KeyPairName="KeyPairName=${env.KeyPairName}"
        //     def CertificateName="CertificateName=${env.CertificateName}"
        //     def DBUsername="DBUsername=${env.DBUsername}"
        //     def DBPassword="DBPassword=${env.DBPassword}"
        //     def JDKVersion="JDKVersion=${env.JDKVersion}"
        //     def AMIID="AMIid=${env.AMI_ID}"
        //     withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: AWS_CREDS]]) {
        //         def AWSAccessKeyId="AWSAccessKeyId=${env.AWS_ACCESS_KEY_ID}"
        //         def AWSAccessKeySecret="AWSAccessKeySecret=${env.AWS_SECRET_ACCESS_KEY}"
        //
        //         withAWS(credentials: AWS_CREDS,region: env.REGION) {
        //           def outputs = cfnUpdate(stack: PROD_STACK, file: CF_FILE, params:[AWSAccessKeyId, AWSAccessKeySecret,WSO2InstanceType, KeyPairName, CertificateName, DBUsername, DBPassword, JDKVersion, AMIID], timeoutInMinutes:20, pollInterval:1000)
        //           env.TEST_URL = outputs.'MgtConsoleUrl'
        //         //   echo "Deleting STACK"
        //         //   sleep time: 1, unit: 'MINUTES'
        //         //   cfnDelete(stack: PROD_STACK, pollInterval:1000)
        //         }
        //     }
        // }

}

def handleError(stage, code) {
    echo "The Pipeline is failed in $stage stage with the exit code $code"
    sh "exit 1"
}
