package com.day.line.data.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppVersion(
    @SerialName("id") val id: String,
    @SerialName("version_code") val versionCode: Int,
    @SerialName("version_name") val versionName: String,
    @SerialName("changelog") val changelog: String? = null,
    @SerialName("is_critical") val forceUpdate: Boolean = false,
    @SerialName("min_supported_version") val minSupportedVersion: Int = 0,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("download_url") val downloadUrl: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)
