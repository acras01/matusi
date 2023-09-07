package ua.od.acros.matusi.domain.model

import com.google.android.gms.maps.model.LatLng

data class Parent(
    val id: String,
    var nickname: String,
    var facebook: String,
    var skype: String,
    var viber: String,
    var twitter: String,
    var location: LatLng?,
    var radius: Long?,
    var kids: ArrayList<Child>,
    var karma: Long,
    var readyToJoin: Boolean,
    var schedule: ArrayList<CareSchedule>
    )