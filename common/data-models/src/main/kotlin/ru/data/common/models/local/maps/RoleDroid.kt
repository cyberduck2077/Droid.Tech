package ru.data.common.models.local.maps

import ru.data.common.models.res.TextApp


enum class RoleDroid() {
    SPOUSE(),
    CHILD(),
    SELF(),
    PARENT(),
    SIBLING();

    companion object {
        fun getEnum(status: Int?) = entries.getOrElse(status ?: 0) { SELF }
    }

    fun getTextAddMembers() = when (this) {
        SPOUSE  -> TextApp.textAddSpouse
        CHILD   -> TextApp.textAddChild
        SELF    -> TextApp.textAddSelf
        PARENT  -> TextApp.textAddParent
        SIBLING -> TextApp.textAddSibling
    }

    fun getTextRoleMembers() = when (this) {
        SPOUSE  -> TextApp.textSpouse
        CHILD   -> TextApp.textChild
        SELF    -> TextApp.textSelf
        PARENT  -> TextApp.textParent
        SIBLING -> TextApp.textSibling
    }
}
