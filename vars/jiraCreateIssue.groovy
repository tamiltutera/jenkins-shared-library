@NonCPS
def renderTemplate(input, binding) {
    def engine = new groovy.text.SimpleTemplateEngine()
    def template = engine.createTemplate(input).make(binding)
    return template.toString()
}

def call(Map config=[:]) {
  def rawBody = libraryResource 'com/tamiltutera/api/jira/createIssue.json'
  def binding = [
    key: "${config.key}",
    summary: "${config.summary}",
    description: "${config.description}",
    issueTypeName: "${config.issueTypeName}"
  ]
  def render = renderTemplate(rawBody,binding)
  sh('curl -D- -u $JIRA_CREDENTIALS -X POST --data "'+render+'" -H "Content-Type: application/json" $JIRA_URL/rest/api/2/issue')
}
