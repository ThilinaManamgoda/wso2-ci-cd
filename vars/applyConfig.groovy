#!/usr/bin/env groovy

/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

import org.wso2.util.Constants

def call(Map config) {
    withEnv(["ARTIFACT_LOC=${config.artifactsLoc}", "ZIP_OUTPUT_LOC=${config.zipLoc}", "WUM_HOME=${config.wumHome}",
             "PUPPET_CONF_LOC=${config.puppetConfLoc}"]) {
        withCredentials([usernamePassword(credentialsId: "${config.wum_creds}", passwordVariable: 'WUM_PASSWORD',
                usernameVariable: 'WUM_USERNAME')]) {
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
            } else if (status == Constants.ControlConstants.FAILED_TO_MOVE_UPDATED_PRODUCT) {
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

