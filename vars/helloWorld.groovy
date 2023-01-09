def call(Map config = [:]) {
    sh "echo Hello ${config.name}, current month is ${config.monthOfTheYear}"
}
