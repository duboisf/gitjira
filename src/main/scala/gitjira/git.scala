package gitjira

import sys.process._

object Git {

  def config(key: String, default: String = null): Option[String] = {

    try {
      // if this returns a non-zero exit code, it throws an exception
      ("git config --get " + key).!! trim match {
        case "" => None
        case s => Some(s)
      }
    } catch {
      // any exception running the command, assume the config value is absent and return None
      case _ => None
    }
  }

  def createBranch(branch: String) {

    // create the branch, or if it exists already, just switch to it
    val cmd = ("git checkout -q -b " + branch) #|| ("git checkout -q " + branch)

    // run it, and ignore the 'branch already exists' error
    val BranchAlreadyExists = """fatal: git checkout: branch (.*) already exists""".r
    cmd ! ProcessLogger(_ match {
      case BranchAlreadyExists(_) => /* ignore */
      case s => Console.err.println("Unexpected git output:\n%s" format s)
    })
  }

  // run "git branch", extract the current branch (prefixed with a *) and return the branch name (strip "* " prefix)
  def branch: Option[String] = ("git branch" !!) split ("\n") find (_ startsWith "*") map (_ substring 2)

  def changeBranch(branch: String) = ("git checkout -q %s" format branch).!

  def deleteBranch(branch: String) = ("git branch -d %s" format branch).!

  def merge (branch: String) { ("git merge --ff --stat %s" format branch).! }

  def rebase (branch: String) { ("git rebase --stat %s" format branch).! }

  def pull(remote: String, branch: String) { ("git pull --ff --stat --rebase %s %s" format (remote, branch)).! }

  def push(remote: String, branch: String) { ("git push %s %s" format (remote, branch)).! }

  def note(userid: String, jira: String) { ("git notes --ref=jira-%s add -f -m 'fixes:%s'" format (userid, jira)).! }
}