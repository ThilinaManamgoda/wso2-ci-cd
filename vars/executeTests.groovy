boolean call(script) {
    return sh(
            script: '''
                    set +x
                    ${script}
                    ''',
            returnStatus: true
    ) == 0
}