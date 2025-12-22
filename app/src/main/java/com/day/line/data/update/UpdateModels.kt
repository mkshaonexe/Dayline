package com.day.line.data.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppVersion(
    @SerialName("id") val id: Long,
    @SerialName("version_code") val versionCode: Int,
    @SerialName("version_name") val versionName: String,
    @SerialName("changelog") val changelog: String,
    @SerialName("force_update") val forceUpdate: Boolean,
    @SerialName("created_at") val createdAt: String
)
