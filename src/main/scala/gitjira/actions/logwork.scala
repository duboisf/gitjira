package gitjira.actions

import gitjira._

object LogWorkAction {
  def apply (work: String) = new LogWorkAction(work, 0)
}

case class LogWorkAction(work: String, number: Int) extends GitJiraAction {

  def execute(config: Configuration, jira: JIRA) {

    val issue = jira getIssue number

    // validate the syntax for the 'work' string
    val ValidWorkSyntax = """(\d+)(['h'|'d'|'w'])""".r
    val (n, u) = work match {
      case ValidWorkSyntax(a, b) => (a toInt, b)
      case _ => sys.error("invalid syntax '%s', expecting 4h, 2d, 1w etc" format work)
    }

    val unit = List("hour","day","week") find ( _ startsWith u ) get
    
    config log ("Logging work done on %s: %s %s" format (issue, n, if (n > 1) unit + "s" else unit))

    jira logWorkDone (issue, work)
  }
}