String call(Map config) {
//    env.PRODUCT_DIST = "${config.product}-${config.version}.zip"
//    env.PACKER_BASE_IMAGE = "${config.image}"
//    env.PACKER_REGION = "${config.region}"
//    env.PACKER_JSON = "${config.packerJson}"
//    env.PACKER_MANIFEST = "${config.manifest}"
//    env.IMAGE_RESOURCES = "${config.imageResources}"


    withEnv(["PRODUCT_DIST=${config.product}-${config.version}.zip", "PACKER_BASE_IMAGE=${config.image}", "PACKER_REGION=${config.region}", "PACKER_JSON=${config.packerJson}", "PACKER_MANIFEST=${config.packerManifest}", "IMAGE_RESOURCES=${config.imageResources}", "AWS_CREDS_FILE=${config.awsCredsFile}"]) {
        BUILD_FULL = sh(
                script: """
                        export AWS_SHARED_CREDENTIALS_FILE=$AWS_CREDS_FILE 
                        packer build  -var "product=$PRODUCT_DIST" -var "region=$PACKER_REGION" -var "base_ami=$PACKER_BASE_IMAGE" -var "image_resources=$IMAGE_RESOURCES" -var "manifest=$PACKER_MANIFEST" $PACKER_JSON
                        """,
                returnStatus: true
        )
    }
    def packer_post = readJSON file: "${config.packerManifest}", text: ''
    def size = packer_post.builds.artifact_id.size()
    def ami_info = packer_post.builds.artifact_id[size - 1]
    def (value1, value2) = "$ami_info".tokenize(':')
    return value2
}