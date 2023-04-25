package com.arda.campuslink.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.core.content.ContextCompat
import com.arda.campuslink.App
import com.arda.campuslink.App.Companion.context
import java.io.ByteArrayOutputStream


object ImageProcessUtils {

    fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension
        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth =
                (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight =
                (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }

    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {

        val baos = ByteArrayOutputStream()
        val scaledBitMap = scaleBitmapDown(bitmap, 320)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos)
        val data = baos.toByteArray();
        return data
    }

    fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size);
        return bitmap
    }

    fun convertBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
        return base64encoded
    }

    fun convertUriToBitmap(uri: Uri): Bitmap {
        var bitmap: Bitmap
        uri!!.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(App.context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(App.context.contentResolver, it)
                bitmap = ImageDecoder.decodeBitmap(source)
            }
        }
        return bitmap
    }
    fun convertBitmapToUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(App.context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
    fun getBitmapFromImage(drawable: Int): Bitmap {

        // on below line we are getting drawable
        val db = ContextCompat.getDrawable(context, drawable)

        // in below line we are creating our bitmap and initializing it.
        val bit = Bitmap.createBitmap(
            db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
        )

        // on below line we are
        // creating a variable for canvas.
        val canvas = Canvas(bit)

        // on below line we are setting bounds for our bitmap.
        db.setBounds(0, 0, canvas.width, canvas.height)

        // on below line we are simply
        // calling draw to draw our canvas.
        db.draw(canvas)

        // on below line we are
        // returning our bitmap.
        return bit
    }
}