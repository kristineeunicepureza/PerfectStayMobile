package com.android.theperfectstay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.theperfectstay.R
import com.android.theperfectstay.model.Hotel

class HotelAdapter(
    private val context: Context,
    private var hotelList: List<Hotel>
) : RecyclerView.Adapter<HotelAdapter.HotelViewHolder>() {

    private var listener: OnHotelActionListener? = null

    interface OnHotelActionListener {
        fun onHotelClicked(hotel: Hotel)
        fun onBookNowClicked(hotel: Hotel)
    }

    fun setOnHotelActionListener(listener: OnHotelActionListener) {
        this.listener = listener
    }

    fun updateHotelList(newList: List<Hotel>) {
        hotelList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hotel_list_item, parent, false)
        return HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = hotelList[position]
        holder.bind(hotel)
    }

    override fun getItemCount(): Int = hotelList.size

    inner class HotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hotelImage: ImageView = itemView.findViewById(R.id.img_hotel)
        private val hotelName: TextView = itemView.findViewById(R.id.tv_hotel_name)
        private val hotelLocation: TextView = itemView.findViewById(R.id.tv_location)
        private val hotelRating: TextView = itemView.findViewById(R.id.tv_rating)
        private val hotelReviews: TextView = itemView.findViewById(R.id.tv_reviews)
        private val hotelPrice: TextView = itemView.findViewById(R.id.tv_price)
        private val hotelDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val bookNowButton: Button = itemView.findViewById(R.id.btn_book_now)
        private val imgShower: ImageView = itemView.findViewById(R.id.ic_shower)
        private val imgHotTub: ImageView = itemView.findViewById(R.id.ic_hot_tub)
        private val imgAc: ImageView = itemView.findViewById(R.id.ic_ac)

        fun bind(hotel: Hotel) {
            hotelImage.setImageResource(hotel.imageResourceId)
            hotelName.text = hotel.name
            hotelLocation.text = hotel.location
            hotelRating.text = hotel.rating.toString()
            hotelReviews.text = "(${hotel.reviews})"
            hotelPrice.text = "$${hotel.price}/night"
            hotelDescription.text = hotel.description

            imgShower.alpha = if (hotel.hasShower) 1.0f else 0.3f
            imgHotTub.alpha = if (hotel.hasHotTub) 1.0f else 0.3f
            imgAc.alpha = if (hotel.hasAc) 1.0f else 0.3f

            itemView.setOnClickListener {
                listener?.onHotelClicked(hotel)
            }

            bookNowButton.setOnClickListener {
                listener?.onBookNowClicked(hotel)
            }
        }
    }
}