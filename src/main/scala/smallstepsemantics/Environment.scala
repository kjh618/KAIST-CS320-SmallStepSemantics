package smallstepsemantics

case class Environment(e: Map[String, Value]) {
  override def toString: String =
    if (e.isEmpty)
      "∅"
    else
      e.map({ case (n, v) => s"$n ↦ $v" }).mkString("[", ", ", "]")
}