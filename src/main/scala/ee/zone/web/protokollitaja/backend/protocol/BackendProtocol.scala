package ee.zone.web.protokollitaja.backend.protocol

import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
import org.apache.pekko.http.scaladsl.server.Route

object BackendProtocol {

  sealed trait BackendMsg

  final case class GetRoute(from: ActorRef[BackendMsg]) extends BackendMsg

  final case class SendRoute(route: Route) extends BackendMsg

  case class Start(system: ActorSystem[BackendMsg]) extends BackendMsg

}
