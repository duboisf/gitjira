package gitjira.actions

import gitjira.{Configuration, Git, JIRA}
import gitjira.JIRA.IN_PROGRESS

case class BranchAction(number: Int) extends GitJiraAction {

  def execute(config: Configuration, jira: JIRA) {

    // need JIRA config in Git
    JIRA.project required "JIRA project must be configured"

    val issue = jira getIssue number
    config log ("Got JIRA: %s" format issue)

    val branchName = "%s-%s/%s" format(JIRA.shortname, number, issue summary)
    config log ("Branching for JIRA %s: %s" format (issue id, branchName))

    jira transition (issue, IN_PROGRESS)

    Git createBranch branchName
  }
}
