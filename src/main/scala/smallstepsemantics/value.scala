package smallstepsemantics

trait Value

case class NumVal(num: Int) extends Value {
  override def toString: String = num.toString
}

case class CloVal(param: String, body: Expression, cloEnv: Environment) extends Value {
  override def toString: String = s"⟨λ$param.$body, $cloEnv⟩"
}

case class ContVal(contStack: List[Continuation], valStack: List[Value]) extends Value {
  override def toString: String = {
    val contStackStr = contStack.foldLeft("")({ case (str, cont) => str + s"$cont :: "}) + "□"
    val valStackStr = valStack.foldLeft("")({ case (str, value) => str + s"$value :: "}) + "■"
    s"⟨$contStackStr, $valStackStr⟩"
  }
}