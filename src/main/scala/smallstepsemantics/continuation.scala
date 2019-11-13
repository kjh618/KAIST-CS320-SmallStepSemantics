package smallstepsemantics

trait Continuation

case class EvalCont(expr: Expression, env: Environment) extends Continuation {
  override def toString: String = s"$env ⊢ $expr"
}

case object AddCont extends Continuation {
  override def toString: String = "(+)"
}

case object SubCont extends Continuation {
  override def toString: String = "(-)"
}

case object AppCont extends Continuation {
  override def toString: String = "(@)"
}