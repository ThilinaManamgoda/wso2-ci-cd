import org.wso2.util.Constants

def err(msg) {
    ansiColor('xterm') {
        echo "\u001B[36mERROR: $msg"
    }
}
def info(msg) {
    ansiColor('xterm') {
        echo "\u001B[32mINFO: $msg"
    }

}
