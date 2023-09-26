package ru.data.common.models.local.maps


data class FilterPosts(
    val publishedFrom: Long? = null,
    val publishedTo: Long? = null,
    val withPolling: Boolean? = null,
    val withText: Boolean? = null,
    val withPhoto: Boolean? = null,
    val withVideo: Boolean? = null,
) {

    fun getPollingChoose(): Boolean? {
        if (checkAllTrue()) return null
        if (checkAllNoTrue()) return null
        return if (this.withPolling == false) null else true
    }

    fun getTextChoose(): Boolean? {
        if (checkAllTrue()) return null
        if (checkAllNoTrue()) return null
        return if (this.withText == false) null else true
    }

    fun getPhotoChoose(): Boolean? {
        if (checkAllTrue()) return null
        if (checkAllNoTrue()) return null
        return if (this.withPhoto == false) null else true
    }

    fun getVideoChoose(): Boolean? {
        if (checkAllTrue()) return null
        if (checkAllNoTrue()) return null
        return if (this.withVideo == false) null else true
    }


    fun getPollingChooseForeUI(): Boolean {
        if (checkAllNull()) return true
        return this.withPolling ?: true
    }

    fun getTextChooseForeUI(): Boolean {
        if (checkAllNull()) return true
        return this.withText ?: true
    }

    fun getPhotoChooseForeUI(): Boolean {
        if (checkAllNull()) return true
        return this.withPhoto ?: true
    }

    fun getVideoChooseForeUI(): Boolean {
        if (checkAllNull()) return true
        return this.withVideo ?: true
    }


    private fun checkAllNull() = this.withPolling == null
            && this.withText == null
            && this.withPhoto == null
            && this.withVideo == null


    private fun checkAllTrue() = this.withPolling == true
            && this.withText == true
            && this.withPhoto == true
            && this.withVideo == true

    private fun checkAllNoTrue() = this.withPolling != true
            && this.withText != true
            && this.withPhoto != true
            && this.withVideo != true
}