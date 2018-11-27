
def err(msg) {
    ansiColor('xterm') {
        echo "\u001B[31mERROR: $msg"
    }
}
def info(msg) {
    ansiColor('xterm') {
        echo "\u001B[36mINFO: $msg"
    }

}
