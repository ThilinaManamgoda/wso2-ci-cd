def call(script) {
    sh(
            script: '''
                    set +x
                    ${script}
                    ''',
            returnStatus: true
    )
}