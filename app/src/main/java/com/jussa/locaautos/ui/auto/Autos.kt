package com.jussa.locaautos.ui.auto

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jussa.locaautos.data.DataAuto
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

class Autos {
    private var cont: Context? = null

    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
    private val nomeDB = "autos" //autos é o nome dó nó parent principal da base de dados no FireBase
    private val context: Context? = cont
    private val fBase = FirebaseDatabase.getInstance()
    private val fStorageInstance = FirebaseStorage.getInstance()
    private var fStorageRef = fStorageInstance.getReferenceFromUrl("gs://loca-auto-fiap.appspot.com")
    private val myRef = fBase.getReference(nomeDB)


    //Retorna o Path do Arquivo que foi feito Upload
    @OptIn(DelicateCoroutinesApi::class)
    fun uploadAutoImage(bMap: Bitmap?, vChassi: String): String {
        var strFinalPath: String = ""
        //var sd: File = Environment.getExternalStorageDirectory();
        //val uri: Uri = getImageUri(context!!, bMap)
        //var strFinalPath = File(getRealPathFromUri(uri))
        ///var drwb: Drawable = bMap.toDrawable(context.resources)
        val arrStream: ByteArrayOutputStream = ByteArrayOutputStream()
        bMap?.compress(Bitmap.CompressFormat.JPEG, 100, arrStream)
        val data: ByteArray = arrStream.toByteArray()
        val fileRef: StorageReference = fStorageRef.child("loca_autos_img/${vChassi}.jpg")

        try {
            if(bMap != null){
               // GlobalScope.launch {
               //     suspend {
            //            Log.d("coroutineScope", "#runs on ${Thread.currentThread().name}")

                        val t = fileRef.putBytes(data)
                            .addOnCompleteListener{
                                Log.d("COMPLETE_TASK_UPLOAD", it.result.storage.path) //+ it.result.storage.name
                            }
                            .addOnSuccessListener {
                                //strFinalPath = it.storage.path //+ it.storage.name
                                Log.d("SUCCESS_TASK_UPLOAD", it.storage.path)
                            }
                            .addOnFailureListener{
                                //Toast.makeText(context, "Erro ao efetuar o Upload da Imagem..:\n${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
                                Log.d("ERROR_UPLOAD", "Erro ao efetuar upload da imagem...")
                            }
                //while (t.isInProgress){
                //    Log.d("WHILE PROGRESS...", "whilhe ${t.toString()}")
                //}
              //          withContext(Dispatchers.Main) {
              //              Log.d("coroutineScope", "#runs on ${Thread.currentThread().name}")
              //          }
             //       }.invoke()
            //    }
            }
        }catch (ex: Exception){
            //Toast.makeText(context, "Erro ao efetuar o Upload da Imagem..:\n${ex.printStackTrace()}", Toast.LENGTH_SHORT).show()
            Log.d("ERROR_UPLOAD", "Erro ao efetuar upload da imagem...\n${ex.printStackTrace()}")
        }
        /////////////////// RETURN
        return fileRef.toString()
    }

    fun getImageUri(cont: Context, img: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val strPath: String = MediaStore.Images.Media.insertImage(cont.contentResolver, img, "Title", null)
        return Uri.parse(strPath)
    }

    fun getRealPathFromUri(uri: Uri): String{
        var strPath: String = ""
        val vResolver = context?.contentResolver
        if (vResolver != null){
            val vCursor: Cursor? = vResolver.query(uri, null, null, null, null)
            if (vCursor != null){
                vCursor.moveToFirst()
                val indx = vCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                strPath = vCursor.getString(indx)
                vCursor.close()
            }
        }
        return strPath
    }

    fun getAutoImage(imgPathChassi: String): StorageReference {
        val oneMegaByte: Long = 1024 * 1024

        fStorageRef.getBytes(oneMegaByte)
            .addOnSuccessListener {
                //it.
            }
         var img = fStorageRef.downloadUrl
             while (!img.isComplete){
            Log.d("DOWNLOADING....","Downloading Image....")
        }
        return fStorageRef
    }

    fun deleteAuto(pChassi: String) {
        if (pChassi != "null" && pChassi != "") {
            myRef.child(pChassi).removeValue()
                .addOnCompleteListener {
                }
                .addOnFailureListener {
                    Log.d("ERROR DELETE AUTO", "Erro ao tentar excluir o veículo!\n${it.printStackTrace()}")
                    //Toast.makeText(context, "Erro ao tentar excluir o veículo!\n${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun writeNewAuto(
        Chassi: String? = null,
        Descricao: String? = null,
        Imagem: String? = null,
        MarcaModelo: String? = null
    ) {
        //val autos = Autos(NomeAuto, MarcaModelo, Descricao, Imagem, NUMBER )

        val vAuto = DataAuto(
            Chassi,
            Imagem,
            Descricao,
            MarcaModelo
        )

            if (Chassi != null && Chassi != "null" && Chassi != "") {
                myRef.child(Chassi).setValue(vAuto)
                    .addOnSuccessListener {
                        Log.d("writeNewAuto_Jussa", "DADOS GRAVADOS COM SUCESSO!")
                        //Toast.makeText(context, "DADOS GRAVADOS COM SUCESSO!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Log.d("writeNewAuto_Jussa", "ERRO NA GRAVACAO DOS DADOS!")
                        //Toast.makeText(context, "ERRO NA GRAVACAO DOS DADOS!", Toast.LENGTH_SHORT).show()
                    }
            }
    }

}


