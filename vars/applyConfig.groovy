import org.wso2.util.Constants

def call(Map config) {
    withEnv(["ARTIFACT_LOC=${config.artifactsLoc}", "ZIP_OUTPUT_LOC=${config.zipLoc}", "WUM_HOME=${config.wumHome}", "PUPPET_CONF_LOC=${config.puppetConfLoc}"]) {
        withCredentials([usernamePassword(credentialsId: "${config.wum_creds}", passwordVariable: 'WUM_PASSWORD', usernameVariable: 'WUM_USERNAME')]) {
            int status = sh(
                    script: "${config.puppetManifest}",
                    returnStatus: true
            )
            if (status == Constants.ControlConstants.FAILED_WUM_UPDATE) {
                throw new Exception("Failed to apply updates")
            } else if (status == Constants.ControlConstants.FAILED_WUM_ADD) {
                throw new Exception("Wum add process failed")
            } else if (status == Constants.ControlConstants.FAILED_INPLACE_UPDATES) {
                throw new Exception("Inplace update process failed")
            } else if (status == Constants.ControlConstants.FAILED_PUPPET_APPLY) {
                throw new Exception("Puppet apply process failed")
            } else if (status == Constants.ControlConstants.FAILED_TO_MOVE_WUMMED_PRODUCT) {
                throw new Exception("Failed to move updated pack")
            } else if (status == Constants.ControlConstants.FAILED_UNZIP) {
                throw new Exception("Failed to unzip")
            } else if (status == Constants.ControlConstants.FAILED_RM_UNZIP) {
                throw new Exception("Failed to remove zip")
            } else if (status == Constants.ControlConstants.FAILED_ARTIFACT_APPLY) {
                throw new Exception("Failed to apply artefatcs")
            }
        }
    }
}

