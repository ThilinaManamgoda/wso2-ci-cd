
def call(Map config) {

//    env.SRC = "dsdsd"
    def SRC1 = "${config.src}"
    echo SRC1
    withEnv(['SRC="${SRC1}"']) {

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