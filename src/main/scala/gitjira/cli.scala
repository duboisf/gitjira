package gitjira

import actions._
import de.downgra.scarg.{ValueMap, ConfigMap, ArgumentParser, DefaultHelpViewer}

// we want to store three values, a boolean and two strings
class Configuration(m: ValueMap) extends ConfigMap(m) {
  val verbose = ("verbose", false).as[Boolean]
  val force = ("force", false).as[Boolean]
  val actions = ("actions").asList[String]

  def log(message: String) { if ( verbose ) println( message ) }
}

object GitJiraCLI {

  // our argument parser which uses a factory to create our Configuration
  case class GitJiraParser() extends ArgumentParser(new Configuration(_)) with DefaultHelpViewer {
    override val programName = Some("git-jira") // set the program name for the help text

    // define our expected arguments
    !"-v" | "--verbose" |% "enable verbose output"            |> "verbose"
    !"-f" | "--force"   |% "force execution despite warnings" |> "force"
    ("-" >>> 60)
    +"action" |*> "actions"
  }

  def parse(args: Array[String]): (Configuration, GitJiraAction) = {
    GitJiraParser().parse(args) match {
      case Right(config) =>
        val action = config.actions match {
          case List("branch", jiraNumber) => new BranchAction(jiraNumber toInt)
          case List("resolve") => new ResolveAction(0)
          case List("resolve", jiraNumber) => new ResolveAction(jiraNumber toInt)
          case List("assigned") => new AssignedAction()
          case List("describe", jiraNumber) => new DescribeAction(jiraNumber toInt)
          case _ =>
            throw new RuntimeException("unexpected action: " + config.actions)
        }

        (config, action)

      // if arguments are bad, usage message will have been displayed automagically
      case Left(xs) => null
    }
  }
}