package gitjira.actions

import gitjira.{Configuration, JIRA}

abstract class GitJiraAction {
  def execute(config: Configuration, jira: JIRA)
}