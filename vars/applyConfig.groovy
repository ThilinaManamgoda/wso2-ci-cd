def call(Map config) {
//    env.ARTIFACT_LOC = "${config.artifactsLoc}"
//    env.ZIP_OUTPUT_LOC = "${config.zipLoc}"

    withEnv(['ARTIFACT_LOC = "${config.artifactsLoc}", ZIP_OUTPUT_LOC = "${config.zipLoc}", WUM_HOME=${config.wumHome}']) {
        withCredentials([usernamePassword(credentialsId: "${config.wum_creds}", passwordVariable: 'WUM_PASSWORD', usernameVariable: 'WUM_USERNAME')]) {
            BUILD_FULL = sh(
                    script: "${config.puppetManifest}",
                    returnStatus: true
            )

        }
    }
}