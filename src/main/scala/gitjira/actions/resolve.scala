package gitjira.actions

import gitjira.{JiraIssue, Git, Configuration, JIRA}

case class ResolveAction() extends GitJiraAction {

  def execute(config: Configuration, jira: JIRA) {

    val me = System getProperty "user.name"
    val branch = Git.branch.get
    val issue = JiraIssue(branch takeWhile (_ != '/'))

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
