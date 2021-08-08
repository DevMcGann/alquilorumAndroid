package com.gsoft.blogapp.ui.propiedades.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gsoft.blogapp.core.BaseViewHolder
import com.gsoft.blogapp.data.models.Propiedad
import com.gsoft.blogapp.databinding.PropiedadItemRowBinding

class PropiedadesScreenAdapter (
    private val context: Context,
    private val itemClickListener: OnPropiedadClickListener)
    : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var propiedadesList = listOf<Propiedad>()

    interface OnPropiedadClickListener {
        fun onPropiedadClick(propiedad: Propiedad, position: Int)
    }

    fun setListaDePropiedades(lista: List<Propiedad>) {
        this.propiedadesList = lista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
       val itemBInding = PropiedadItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val holder = PropiedadesScreenViewHolder(itemBInding)

        itemBInding.root.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
            itemClickListener.onPropiedadClick(propiedadesList[position], position)
        }

        return holder
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
    }

}