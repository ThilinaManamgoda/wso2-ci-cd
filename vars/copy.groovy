
def call(src, dest) {
   int status = sh(
        script: '''
                set +x
                cp -r ${src} ${dest}
                ''',
        returnStatus: true
    )

    if (status == 1) {
        throw new Exception("Copying failed")
    }
}