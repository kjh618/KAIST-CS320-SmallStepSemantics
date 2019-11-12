package smallstepsemantics

trait Value
case class NumVal(num: Int) extends Value
case class CloVal(param: String, body: Expression, cloEnv: Environment) extends Value
case class ContVal(k: Value => Value) extends Value