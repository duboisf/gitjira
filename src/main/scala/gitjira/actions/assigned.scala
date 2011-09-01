package gitjira.actions

import gitjira.{Configuration, JIRA}

case class AssignedAction() extends GitJiraAction {

  def execute(config: Configuration, jira: JIRA) {

    println("Assigned JIRA issues:")

    jira.getAssignedIssues foreach (issue => println("* %s: %s" format (issue.id, issue.summary)))

    println()
  }
}