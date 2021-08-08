package com.gsoft.blogapp.ui.agregarPropiedad

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gsoft.blogapp.R

class PickFotosAdapter (listaFotos : MutableList<Uri>, ctx: Context) : RecyclerView.Adapter<PickFotosAdapter.PickFotosViewHolder>(){

    private var lista : MutableList<Uri> = listaFotos
    var contexto = ctx

    /*inner class objetoClickado{
        fun getItemInPosition(position:Int) : Uri {
            return lista[position]
        }
    }*/

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): PickFotosAdapter.PickFotosViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.foto_item, parent , false)
        return PickFotosViewHolder(vista,contexto)
    }

    override fun onBindViewHolder(holder: PickFotosAdapter.PickFotosViewHolder, position: Int) {
        holder.imagen.setImageURI(lista[position])
    }

    override fun getItemCount(): Int {
       return lista.size
    }


    inner class PickFotosViewHolder(itemView: View, ctx: Context) : RecyclerView.ViewHolder(itemView){
        var imagen : ImageView = itemView.findViewById(R.id.imagen)

        init {
            itemView.setOnClickListener(){
                val position : Int = adapterPosition
                val builder = AlertDialog.Builder(ctx)


                builder.setTitle("Eliminar")
                builder.setMessage("Eliminar Imagen?")
                builder.setPositiveButton("Si"){dialog, which ->
                    lista.removeAt(position)
                    notifyItemRemoved(position)
                }

                builder.setNegativeButton("No"){dialog,which ->
                    dialog.dismiss()
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()

            }
        }
    }

}