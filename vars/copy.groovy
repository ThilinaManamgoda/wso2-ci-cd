
def call(src, dest) {

//    env.SRC = "dsdsd"
    withEnv(['SRC = "ssss", DEST = "${dest}"']) {

        int status = sh(
                script: '''
                set +x
                echo  "ss ${SRC}" 
                cp -r ${SRC} ${DEST}
                ''',
                returnStatus: true
        )

        if (status == 1) {
            throw new Exception("Copying failed")
        }
    }
}