import org.wso2.util.Constants

def call(script) {
    withEnv(["script=${script}"]) {
        int status = sh(
                script: '''
                    set +x
                    ${script}
                    ''',
                returnStatus: true
        )

        if (status != Constants.ControlConstants.STATUS_COMPLETED) {
            throw new Exception("Test script failed")
        }
    }
}