package eu.derfniw.mimir.client

import com.raquo.laminar.api.L.*
import org.scalajs.dom

@main def main(): Unit =
  def appContainer = dom.document.getElementById("app-container")
  val content      = div(h1("Hello, world!"))
  renderOnDomContentLoaded(appContainer, content)
