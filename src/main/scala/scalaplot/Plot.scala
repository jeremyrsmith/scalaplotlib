package scalaplot

import java.awt.Rectangle

import org.jfree.graphics2d.svg.SVGGraphics2D
import org.jfree.ui.Drawable

/**
  * Simply contains a drawable plot and desired drawing dimensions
  */
case class Plot(
  width: Int,
  height: Int,
  drawable: Drawable
) {
  lazy val svg: String = {
    val svgGraphics = new SVGGraphics2D(width, height)
    drawable.draw(
      svgGraphics,
      new Rectangle(0, 0, width, height)
    )
    val result = svgGraphics.getSVGElement
    svgGraphics.dispose()
    result
  }
}
