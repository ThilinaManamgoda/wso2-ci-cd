
def call(src, dest) {
    withEnv(['SRC = "${src}", DEST = "${dest}"']) {
        int status = sh(
                script: '''
                set +x
                cp -r ${SRC} ${DEST}
                ''',
                returnStatus: true
        )

        if (status == 1) {
            throw new Exception("Copying failed")
        }
    }
}