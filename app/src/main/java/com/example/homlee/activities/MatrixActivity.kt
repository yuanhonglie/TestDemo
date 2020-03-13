package com.example.homlee.activities

import android.graphics.Matrix
import android.os.Bundle
import android.view.View
import com.example.homlee.R
import kotlinx.android.synthetic.main.activity_matrix.*

class MatrixActivity: BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnReset -> matrixView.reset()
            R.id.btnStart -> performAction()
        }
    }

    private fun performAction() {
        val endMatrix = Matrix(matrixView.displayMatrix)
        //endMatrix.setScale(2f, 2f)
        //endMatrix.postTranslate(100f, 100f)

        endMatrix.setScale(2f, 2f, -100f, -100f)
        //endMatrix.postTranslate(100f, 100f)
        matrixView.animateToMatrix(endMatrix)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix)
        btnReset.setOnClickListener(this)
        btnStart.setOnClickListener(this)
    }



}