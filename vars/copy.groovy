
def call(src, dest) {
    withEnv(['SRC = "${src}", DEST = "${dest}"']) {
        echo (src)
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