
boolean call(src, dest) {
   return sh(
        script: '''
                set +x
                cp -r ${src} ${dest}
                ''',
        returnStatus: true
    ) == 0
}