#!/usr/bin/env groovy

def call(repoUrl, directory) {
    dir(directory) {
        checkout([$class    : 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false,
                  extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: repoUrl]]])
    }
}
