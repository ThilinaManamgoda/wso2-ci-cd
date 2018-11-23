String call() {
    return sh(
            script: '''
                    set +x
                    ec2metadata --availability-zone | sed \'s/[a-z]$//\'
                    ''',
            returnStdout: true
    ).trim()
}
