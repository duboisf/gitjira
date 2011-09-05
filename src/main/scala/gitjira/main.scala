package gitjira

abstract class GitJiraMain extends App {

  val jira: JIRA // must be overridden in subclass

  val (config, action) = GitJiraCLI parse args

  config log ("git jira: executing %s" format action)

  action execute (config, jira)

  config log ("git jira: complete")
}