package scalaplot
package show

import java.awt.{Dimension, Graphics, Graphics2D}
import javax.swing.{JFrame, JPanel, WindowConstants}

import org.jfree.graphics2d.svg.SVGGraphics2D
import org.jfree.ui.Drawable

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

abstract class ShowPlot {
  def apply(plot: Plot): Unit
}

object ShowPlot {

  object window extends ShowPlot {
    def apply(plot: Plot): Unit = {
      val frame = new JFrame()
      val panel = new JPanel() {
        override def paint(g: Graphics): Unit = {
          super.paint(g)
          plot.drawable.draw(
            g.asInstanceOf[Graphics2D],
            this.getBounds()
          )
        }
      }
      val dimension = new Dimension(plot.width, plot.height)
      panel.setPreferredSize(dimension)
      frame.setSize(dimension)
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
      frame.setContentPane(panel)
      frame.setVisible(true)
    }
  }

  abstract class ShowSVG extends ShowPlot {
    def renderSVG(plot: Plot): String = plot.svg
  }

  object notebook extends ShowSVG {
    def apply(plot: Plot): Unit = {
      val str = renderSVG(plot)
      println(s"%html $str")
    }
  }

  /**
    * An attempt at a sensible default - if the graphics environment is headless, print out
    * "%html <svg...>" with the plot as SVG; otherwise pop up a window containing the plot.
    */
  implicit def default: ShowPlot = if(java.awt.GraphicsEnvironment.isHeadless)
    notebook
  else
    window
}
