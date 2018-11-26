
def call(src, dest) {
    sh(
        script: '''
                set +x
                cp -r ${src} ${dest}
                ''',
        returnStatus: true
    )
}