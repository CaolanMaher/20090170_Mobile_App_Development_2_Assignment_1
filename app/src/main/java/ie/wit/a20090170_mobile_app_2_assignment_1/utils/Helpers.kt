package ie.wit.a20090170_mobile_app_2_assignment_1.utils

import android.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Transformation

private lateinit var auth: FirebaseAuth

fun customTransformation() : Transformation =
    RoundedTransformationBuilder()
        .borderColor(Color.WHITE)
        .borderWidthDp(2F)
        .cornerRadiusDp(35F)
        .oval(false)
        .build()
