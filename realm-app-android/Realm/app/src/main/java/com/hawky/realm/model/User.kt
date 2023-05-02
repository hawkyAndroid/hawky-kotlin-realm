package com.hawky.realm.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class User : BaseObservable() {
    var id: Long = 0

    @Bindable
    var name: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR._all)
        }

    @Bindable
    var email: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR._all)
        }
}