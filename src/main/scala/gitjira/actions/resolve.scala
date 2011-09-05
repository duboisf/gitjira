package gitjira.actions

import gitjira._

case class ResolveAction(number: Int) extends GitJiraAction {

  def execute(config: Configuration, jira: JIRA) {

    // need JIRA config in Git, make sure it's there
    JIRA.project required "JIRA project must be configured"

    val me = System getProperty "user.name"

    // JIRA issue number is in the name of the branch (allow override in command line arguments)
    val branch = Git.branch.get
    val ExpectedBranchSyntax = """(\w+-\d+)/(.*)""".r
    val issueId = if ( number > 0 ) JIRA.shortname + "-" + number
    else branch match {
      case ExpectedBranchSyntax(id,summary) => id
      case _ => {
          println("Invalid branch syntax '%s', expecting TEST-123/blabla" format branch)
          throw new IllegalArgumentException("cannot determine JIRA issue to resolve")
      }
    }

    // Download the full issue details from JIRA
    val issue = JiraIssue(issueId) downloadFrom jira

    config log ("JIRA Resolve: %s" format issue)

    // update master branch from origin (rebasing if needed)
    Git changeBranch "master"
    Git pull ("origin", "master")

    // then return to our branch, and rebase with changes from master
    Git changeBranch branch
    Git rebase "master"

    // now our branch is fully up to date, collapse with master and delete the branch
    Git changeBranch "master"
    Git merge branch // should do FF since we just rebased
    Git deleteBranch branch

    // add Jira notes
    Git note (me, issue.id)

    // and finally push changes to the main repository
    Git push ("origin", "master")

    // push commit notes to origin
    Git push ("origin", "refs/notes/jira-%s" format me)

    // all done, change the Jira issue status to 'resolved'
    jira resolve issue
  }
}
