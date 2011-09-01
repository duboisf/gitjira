package gitjira

class DummyJira extends JIRA {
  def getIssue(number: Int) = JiraIssue("TEST", number)
  def transition(issue: JiraIssue, s: String) { println("transition %s to %s" format (issue.id, s))}
  def resolve(issue: JiraIssue) { println("resolve "+issue) }
  def getAssignedIssues = Seq(JiraIssue("TEST", 123), JiraIssue("TEST", 456))
}

trait DummyJiraProvider extends JiraProvider {

  override val jira = new DummyJira
}

object MockMain extends App with DummyJiraProvider {

  val (config, action) = GitJiraCLI parse args

  action execute(config, jira)
}