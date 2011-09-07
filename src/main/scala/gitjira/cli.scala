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

  def isNumber (s: String) = s forall { "0123456789" contains _ }

  def parse(args: Array[String]): (Configuration, GitJiraAction) = {
    GitJiraParser().parse(args) match {
      case Right(config) =>
        val action = config.actions match {
          case List("assigned") => AssignedAction()
          case List("branch", j) => BranchAction(j toInt)
          case List("describe", j) => DescribeAction(j toInt)
          case List("log", work) => LogWorkAction(work)
          case List("logwork", work) => LogWorkAction(work)
          case List("log", j, work) => LogWorkAction(work, j toInt)
          case List("logwork", j, work) => LogWorkAction(work, j toInt)
          case List("resolve") => ResolveAction()
          case List("resolve", s) => {
            if (isNumber(s)) ResolveAction(s toInt)
            else LogWorkAction(s) :: ResolveAction()
          }
          case List("resolve", j, work) => LogWorkAction(work, j toInt) :: ResolveAction(j toInt)
          case _ => sys.error("unexpected action: " + config.actions)
        }

        (config, action)

      // if arguments are bad, usage message will have been displayed automagically
      case Left(xs) => null
    }
  }
}