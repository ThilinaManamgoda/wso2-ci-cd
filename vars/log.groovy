
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
