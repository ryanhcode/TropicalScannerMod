package com.ryanhcode.tropicalscanner

import club.sk1er.elementa.WindowScreen
import club.sk1er.elementa.components.*
import club.sk1er.elementa.components.input.UITextInput
import club.sk1er.elementa.components.inspector.Inspector
import club.sk1er.elementa.constraints.*
import club.sk1er.elementa.dsl.*
import club.sk1er.elementa.effects.OutlineEffect
import club.sk1er.elementa.state.BasicState
import java.awt.Color

class EConfig : WindowScreen() {

    val general: UIWrappedText
    val scan: UIWrappedText
    init {
        val background = UIBlock(Color(22, 22, 24)).constrain {
            width = RelativeConstraint(1f)
            height = RelativeConstraint(1f)
        } childOf window


        val outerContainer = UIContainer().constrain {
            x = RelativeConstraint(0.03f)
            y = RelativeConstraint(0.045f)
            width = RelativeConstraint(0.97f)
            height = RelativeConstraint(0.955f)
        } childOf window

        val sidebar = UIContainer().constrain {
            width = RelativeConstraint(0.2f)
            height = RelativeConstraint(1f)
        } childOf outerContainer


        val titleLabel = UIWrappedText("Tropical", shadow = false).constrain {
            textScale = 2f.pixels()
            width = 90.percent()
        } childOf sidebar


        general = UIWrappedText("General", shadow = false).constrain {
            textScale = 1.5f.pixels()
            y = SiblingConstraint(20f);
            width = 90.percent()
        } childOf sidebar
        scan = UIWrappedText("Scans", shadow = false).constrain {
            textScale = 1.5f.pixels()
            y = SiblingConstraint(5f);
            width = 90.percent()
        } childOf sidebar

        update()
        general.onMouseClick {
            cat = 0
            update()
        }

        scan.onMouseClick {
            cat = 1
            update()
        }



        val splitter = UIBlock(Color(50,50,50)).constrain {
            x = SiblingConstraint()
            width = 1.pixels()
            height = RelativeConstraint(0.955f)
        } childOf outerContainer

        val scrollContainer = UIContainer().constrain {
            y = SiblingConstraint() + 40.pixels()
            width = RelativeConstraint(1f) - 10.pixels()
            height = FillConstraint()
        } childOf sidebar

        val categoryScroller = ScrollComponent(pixelsPerScroll = 25f).constrain {
            width = RelativeConstraint(1f)
            height = RelativeConstraint(1f)
        } childOf scrollContainer

        val categoryScrollBar = UIBlock().constrain {
            x = 7.5f.pixels(true)
            width = 3.pixels()
            color = ConstantColorConstraint(Color.MAGENTA)
        } childOf scrollContainer
        Inspector(window).constrain {
            x = 10.pixels(true)
            y = 10.pixels(true)
        } childOf window

    }

    companion object {
        var cat: Int = 0
    }
    fun update(){
        general.setColor(ConstantColorConstraint(if(cat == 0) Color.GREEN else Color.WHITE))
        scan.setColor(ConstantColorConstraint(if(cat == 1) Color.GREEN else Color.WHITE))
    }

}