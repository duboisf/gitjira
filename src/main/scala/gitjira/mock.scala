package gitjira

class DummyJira extends JIRA {
  def getIssue(number: Int) = JiraIssue("TEST", number)
  def transition(i: JiraIssue, s: String) { println("Mock transition %s to %s" format (i.id, s))}
  def resolve(i: JiraIssue) { println("resolve "+ i) }
  def getAssignedIssues = Seq(JiraIssue("TEST", 123), JiraIssue("TEST", 456))
  def logWorkDone (i: JiraIssue, w: String) { println("Mock '%s' of work done on issue %s" format (w, i))}
}

trait DummyJiraProvider {
  val jira = new DummyJira
}

object MockMain extends GitJiraMain with DummyJiraProvider {
}