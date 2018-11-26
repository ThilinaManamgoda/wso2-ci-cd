
def call(Map config) {
   int status = sh(
        script: '''
                set +x
                cp -r ${config.src} ${config.dest}
                ''',
        returnStatus: true
    )

    if (status == 1) {
        throw new Exception("Copying failed")
    }
}