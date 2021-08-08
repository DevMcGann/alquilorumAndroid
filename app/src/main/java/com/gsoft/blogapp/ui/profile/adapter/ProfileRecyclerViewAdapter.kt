package com.gsoft.blogapp.ui.propiedades.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gsoft.blogapp.core.BaseViewHolder
import com.gsoft.blogapp.data.models.Propiedad
import com.gsoft.blogapp.databinding.PropiedadItemRowBinding

class ProfileAdapter (private val context: Context, private var listaPropiedades : MutableList<Propiedad>)

    : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var propiedadesList = listaPropiedades




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBInding = PropiedadItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropiedadesScreenViewHolder(itemBInding)
    }

    override fun getItemCount(): Int {
        return  propiedadesList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is PropiedadesScreenViewHolder -> holder.bind(propiedadesList[position])
        }
    }


    private inner class PropiedadesScreenViewHolder(val binding: PropiedadItemRowBinding)
        : BaseViewHolder<Propiedad>(binding.root){
        override fun bind(item: Propiedad) = with(binding) {
            Glide.with(context).load(item.imagenes?.get(0))
                    .centerCrop().into(profileImage)
            tDireccion.text = item.direccion
            tPrecio.text = "$"+item.precio.toString()
            tDesc.text = item.desc
            tContacto.text = item.contacto
        }
        init {
            binding.root.setOnClickListener(){
                val position : Int = adapterPosition
                val builder = AlertDialog.Builder(context)


                builder.setTitle("Eliminar")
                builder.setMessage("Eliminar Imagen?")
                builder.setPositiveButton("Si"){dialog, which ->
                    propiedadesList.removeAt(position)
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