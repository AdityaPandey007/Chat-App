package com.example.communicate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(var context : Context, var userList : ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val userName = itemView.findViewById<TextView>(R.id.userName)
        val profile = itemView.findViewById<CircleImageView>(R.id.profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_profile,parent)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user = userList[position]
        holder.userName.text = user.name
        Glide.with(context).load(user.profileImage)
            .placeholder(R.drawable.journalist)
            .into(holder.profile)

        holder.itemView.setOnClickListener{

            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name" , user.name)
            intent.putExtra("image" , user.profileImage)
            intent.putExtra("uid" , user.uid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}