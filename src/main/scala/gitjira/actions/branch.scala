package gitjira.actions

import gitjira.{Configuration, Git, JIRA}
import gitjira.JIRA.IN_PROGRESS

case class BranchAction(number: Int) extends GitJiraAction {

  def execute(config: Configuration, jira: JIRA) {

    val issue = jira getIssue number
    val branchName = "%s-%s/%s" format(JIRA.shortname.get, number, issue summary)

    config log ("Branching for JIRA %s: %s" format (issue.id, branchName))

    jira transition (issue, IN_PROGRESS)

    Git createBranch branchName
  }
}
