package gitjira.actions

import gitjira.{Configuration, JIRA}

case class DescribeAction(number: Int) extends GitJiraAction {

  def execute(config: Configuration, jira: JIRA) {

    val issue = jira getIssue number
    val output = """Issue: %s
      |Assigned: %s
      |Summary: %s
      |Description: %s
      |""".stripMargin

    println(output format (issue.id, "TODO", issue.summary, issue.description))
  }
}