package com.example.fitness.ui.req_diet_plan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness.R
import com.example.fitness.databinding.ReqDietRecyclerItemBinding
import com.example.fitness.network.responses.LoginResponse
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AdapterReqDietPlan constructor(
    private val context: Context,
    private val menuListener: MenuListener,
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context),
) : RecyclerView.Adapter<AdapterReqDietPlan.ViewHolder>() {


    lateinit var scheduleList: List<LoginResponse>
    private val sdf = SimpleDateFormat("hh:mm:ss")

    class ViewHolder(val reqDietRecyclerItemBinding: ReqDietRecyclerItemBinding) :
        RecyclerView.ViewHolder(reqDietRecyclerItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(layoutInflater,
            R.layout.req_diet_recycler_item,
            parent,
            false))
    }

    fun refresh(list: List<LoginResponse>) {
        this.scheduleList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.reqDietRecyclerItemBinding.response = scheduleList[position]
        holder.reqDietRecyclerItemBinding.selectTime.isEnabled = false
        holder.reqDietRecyclerItemBinding.selectTime.setDefaultDate(sdf.parse(scheduleList[position].time))
        holder.reqDietRecyclerItemBinding.imgOptions.setOnClickListener { v ->
            //holder.reqDietRecyclerItemBinding.imgDone.visibility = View.VISIBLE
            menuListener.onOptionSelect(v,
                holder.reqDietRecyclerItemBinding.selectTime,
                holder.reqDietRecyclerItemBinding.editDesc,
                scheduleList[position].id,
                holder.reqDietRecyclerItemBinding.imgDone)
        }
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    interface MenuListener {
        fun onOptionSelect(
            v: View?,
            timer: SingleDateAndTimePicker?,
            edit_desc: EditText?,
            id: String?,
            img_done: ImageView,
        )
    }
}
