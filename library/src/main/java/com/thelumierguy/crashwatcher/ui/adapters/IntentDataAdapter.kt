package com.thelumierguy.crashwatcher.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thelumierguy.crashwatcher.databinding.LayoutIntentDataListItemBinding

class IntentDataAdapter(
    private val intentKeys: List<String>,
    private val intentValues: List<String>
) : RecyclerView.Adapter<IntentDataAdapter.IntentDataViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntentDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutIntentDataListItemBinding.inflate(layoutInflater, parent, false)
        return IntentDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IntentDataViewHolder, position: Int) {
        val keyName = intentKeys[position]
        val value = intentValues[position]
        holder.viewBinding.apply {
            tvKeyName.text = keyName
            tvValue.text = value
        }
    }

    override fun getItemCount(): Int {
        return intentKeys.size
    }

    class IntentDataViewHolder(val viewBinding: LayoutIntentDataListItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}