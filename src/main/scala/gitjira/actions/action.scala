package gitjira.actions

import gitjira.{Configuration, JIRA}

abstract class GitJiraAction {

  def execute(config: Configuration, jira: JIRA)

  def :: (other: GitJiraAction) = new CompositeAction(List(other,this))
}

class CompositeAction(actions: Seq[GitJiraAction]) extends GitJiraAction {

  def execute(config: Configuration, jira: JIRA) {

    actions foreach { _ execute (config, jira) }
  }

  override def toString = actions mkString " + "
}