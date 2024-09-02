// vars/attachFileToJira.groovy
def call(Map config = [:]) {
    // Validate input parameters
    if (!${config.jiraBaseUrl} || !${config.jiraIssueKey} || !${config.credentialsId} || !${config.filePath} ) {
        error "All parameters (jiraBaseUrl, jiraIssueKey, credentialsId, filePath) are required."
    }

    // Jira REST API endpoint to attach files to a specific issue
    String attachEndpoint = "${config.jiraBaseUrl}/rest/api/2/issue/${config.jiraIssueKey}/attachments"

    // Retrieve Jira credentials from Jenkins credentials store
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'JIRA_USER', passwordVariable: 'JIRA_PASS')]) {
        // Set headers required by Jira to upload attachments
        def customHeaders = [
            [
                maskValue: false, 
                name: 'X-Atlassian-Token', 
                value: 'no-check' // Required header to prevent XSRF checks
            ]
        ]

        // Send HTTP request to Jira API to attach the file
        def response = httpRequest(
            httpMode: 'POST',
            acceptType: 'APPLICATION_JSON',
            contentType: 'MULTIPART_FORM_DATA',
            url: attachEndpoint,
            authentication: credentialsId,
            uploadFile: filePath, // File to attach
            customHeaders: customHeaders
        )

        // Check the response status
        if (response.status == 200) {
            echo "File successfully attached to Jira issue ${config.jiraIssueKey}."
        } else {
            error "Failed to attach file to Jira issue. Status: ${response.status} - Response: ${response.content}"
        }
    }
}
