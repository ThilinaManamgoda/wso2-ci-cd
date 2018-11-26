def call(Map config) {
//    env.ARTIFACT_LOC = "${config.artifactsLoc}"
//    env.ZIP_OUTPUT_LOC = "${config.zipLoc}"

    withEnv(["ARTIFACT_LOC=${config.artifactsLoc}", "ZIP_OUTPUT_LOC=${config.zipLoc}", "WUM_HOME=${config.wumHome}", "PUPPET_CONF_LOC=${config.puppetConfLoc}"]) {
        withCredentials([usernamePassword(credentialsId: "${config.wum_creds}", passwordVariable: 'WUM_PASSWORD', usernameVariable: 'WUM_USERNAME')]) {
            int status = sh(
                    script: "${config.puppetManifest}",
                    returnStatus: true
            )
            if (status == 10) {
                throw new Exception("Failed to apply updates")
            } else if (status == 11) {
                throw new Exception("Wum add process failed")
            } else if (status == 12) {
                throw new Exception("Inplace update process failed")
            } else if (status == 13) {
                throw new Exception("Puppet apply process failed")
            } else if (status == 14) {
                throw new Exception("Failed to move updated pack")
            } else if (status == 15) {
                throw new Exception("Failed to unzip")
            } else if (status == 16) {
                throw new Exception("Failed to remove zip")
            } else if (status == 17) {
                throw new Exception("Failed to apply artefatcs")
            }
        }
    }
}

