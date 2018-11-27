
def RED = "\u001B[31m"
def GREEN = "\u001b[32m"

def err(msg) {
    ansiColor('xterm') {
        echo "${RED}INFO: $msg"
    }
}
def info(msg) {
    ansiColor('xterm') {
        echo "${GREEN}ERROR: $msg"
    }

}
