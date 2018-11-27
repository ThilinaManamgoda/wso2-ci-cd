
def err(msg) {
    ansiColor('xterm') {
        echo "\u001B[31mINFO: $msg"
    }
}
def info(msg) {
    ansiColor('xterm') {
        echo "\u001B[32mERROR: $msg"
    }

}
