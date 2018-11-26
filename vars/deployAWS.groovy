def call(Map config) {
    def WSO2InstanceType = "WSO2InstanceType=${config.wso2InstanceType}"
    def KeyPairName = "KeyPairName=${config.keyPairName}"
    def CertificateName = "CertificateName=${config.CertName}"
    def DBUsername = "DBUsername=${config.dbUsername}"
    def DBPassword = "DBPassword=${config.dbPassword}"
    def JDKVersion = "JDKVersion=${config.jdkVersion}"
    def AMIId = "AMIid=${config.amiID}"
    env.AWS_CREDS_FILE = "${config.awsCredsFile}"
    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: "${config.credID}"]]) {
        def AWSAccessKeyId = "AWSAccessKeyId=${env.AWS_ACCESS_KEY_ID}"
        def AWSAccessKeySecret = "AWSAccessKeySecret=${env.AWS_SECRET_ACCESS_KEY}"

        withAWS(credentials: "${config.credID}", region: "${config.region}") {
            def outputs = cfnUpdate(stack: "${config.environment}", file: "${config.cf}",
                    params: [AWSAccessKeyId, AWSAccessKeySecret, WSO2InstanceType, KeyPairName, CertificateName, DBUsername, DBPassword, JDKVersion, AMIId]
                    , timeoutInMinutes: 20, pollInterval: 1000)
             return outputs.'${config.testEndpoint}'

        }
    }
}