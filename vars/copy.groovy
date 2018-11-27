import org.wso2.util.Constants

def call(Map config) {
    withEnv(["SRC=${config.src}", "DEST=${config.dest}"]) {

        int status = sh(
                script: '''
                set +x
                cp -r ${SRC} ${DEST}
                ''',
                returnStatus: true
        )

        if (status != Constants.ControlConstants.STATUS_COMPLETED) {
            throw new Exception("Copying failed. Source: ${config.src}, Destination: ${config.dest}")
        }
    }
}