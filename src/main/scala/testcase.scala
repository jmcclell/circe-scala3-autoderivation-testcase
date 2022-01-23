import io.circe.{Encoder, Decoder, Error, HCursor}
import io.circe.parser._, io.circe.syntax._
//import io.circe.generic.semiauto._
import io.circe.generic.auto._

import cats.implicits._


sealed trait SharedParent

object SharedParent {
  given Encoder[SharedParent] = Encoder.instance {
    case o @ Bar(_, _, _, _) => o.asJson
  }

  given Decoder[SharedParent] =
    List[Decoder[SharedParent]](
      Decoder[Bar].widen,
    ).reduceLeft(_ or _)
}

case class Bar(a: String, b: String, c: String, d: D) extends SharedParent
case class D(
  p1: String, // Moving this parameter to last fixes it
  p2: ChildA,
  p3: ChildB
)

case class ChildA()
case class ChildB(a: ChildBChild)
case class ChildBChild(x: String, y: String, z: ChildBChildChild) // Re-arranging these parameters fixes it
case class ChildBChildChild(x: Int, y: Int)

/*
given Decoder[Bar] = deriveDecoder[Bar]
given Encoder[Bar] = deriveEncoder[Bar]

given Decoder[D] = deriveDecoder[D]
given Encoder[D] = deriveEncoder[D]

given Decoder[ChildA] = deriveDecoder[ChildA]
given Encoder[ChildA] = deriveEncoder[ChildA]

given Decoder[ChildB] = deriveDecoder[ChildB]
given Encoder[ChildB] = deriveEncoder[ChildB]

given Decoder[ChildBChild] = deriveDecoder[ChildBChild]
given Encoder[ChildBChild] = deriveEncoder[ChildBChild]

given Decoder[ChildBChildChild] = deriveDecoder[ChildBChildChild]
given Encoder[ChildBChildChild] = deriveEncoder[ChildBChildChild]
*/
