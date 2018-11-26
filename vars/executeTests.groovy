def call(script) {
    withEnv(["script=${script}"]) {
        sh(
                script: '''
                    set +x
                    ${script}
                    ''',
                returnStatus: true
        )
    }
}