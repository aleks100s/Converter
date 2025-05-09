package com.alextos.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

data class TextBox(
    val rect: Rect,
    val text: String
)

class OverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val rectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        typeface = Typeface.DEFAULT_BOLD
    }

    private var textBoxes: List<TextBox> = emptyList()

    fun setTextBoxes(boxes: List<TextBox>) {
        this.textBoxes = boxes
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (box in textBoxes) {
            canvas.drawRect(box.rect, rectPaint)
            textPaint.textSize = box.rect.height().toFloat()
            canvas.drawText(
                box.text,
                box.rect.left.toFloat(),
                box.rect.top.toFloat() + textPaint.textSize,
                textPaint
            )
        }
    }
}