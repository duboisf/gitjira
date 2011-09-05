package gitjira

abstract class JIRA {

  def getIssue(number: Int): JiraIssue
  def transition(issue: JiraIssue, resolution: String)
  def resolve(issue: JiraIssue)
  def getAssignedIssues: Seq[JiraIssue]
}

object JIRA {
  val IN_PROGRESS = "IN_PROGRESS" // TODO make this an enum or something

  // load certain configuration options from the Git config
  lazy val project = GitConfig("jira.project")
  lazy val instance = GitConfig("jira.instance")
  lazy val shortname = GitConfig("jira.shortname", project)
}

case class JiraIssue(key: String, number: Int, summary: String, description: String) {

  val id = "%s-%s" format (key, number)
}

object JiraIssue {

  def apply(issue: String): JiraIssue = {
    val parts = issue split "-"
    apply(parts(0), parts(1) toInt)
  }

  def apply(key: String, number: Int) = new JiraIssue(key, number, key + "-" + number, null)
}
