package smallstepsemantics

trait Continuation
case class EvalCont(expr: Expression, env: Environment) extends Continuation
case object AddCont extends Continuation
case object SubCont extends Continuation
case object AppCont extends Continuation