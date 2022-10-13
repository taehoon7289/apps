package com.minikode.apps.vo

data class NotionDatabaseVo(
    val has_more: Boolean,
    val next_cursor: Any,
    val `object`: String,
    val page: Page,
    val results: List<Result>,
    val type: String
) {
    class Page

    data class Result(
        val archived: Boolean,
        val cover: Any,
        val created_by: CreatedBy,
        val created_time: String,
        val icon: Any,
        val id: String,
        val last_edited_by: LastEditedBy,
        val last_edited_time: String,
        val `object`: String,
        val parent: Parent,
        val properties: Properties,
        val url: String
    ) {
        data class CreatedBy(
            val id: String,
            val `object`: String
        )

        data class LastEditedBy(
            val id: String,
            val `object`: String
        )

        data class Parent(
            val database_id: String,
            val type: String
        )

        data class Properties(
            val createDate: CreateDate,
            val id: Id,
            val title: Title,
            val type: Type,
            val url: Url
        ) {
            data class CreateDate(
                val date: Date,
                val id: String,
                val type: String
            ) {
                data class Date(
                    val end: Any,
                    val start: String,
                    val time_zone: Any
                )
            }

            data class Id(
                val id: String,
                val number: Int,
                val type: String
            )

            data class Title(
                val id: String,
                val rich_text: List<RichText>,
                val type: String
            ) {
                data class RichText(
                    val annotations: Annotations,
                    val href: Any,
                    val plain_text: String,
                    val text: Text,
                    val type: String
                ) {
                    data class Annotations(
                        val bold: Boolean,
                        val code: Boolean,
                        val color: String,
                        val italic: Boolean,
                        val strikethrough: Boolean,
                        val underline: Boolean
                    )

                    data class Text(
                        val content: String,
                        val link: Any
                    )
                }
            }

            data class Type(
                val id: String,
                val title: List<Title>,
                val type: String
            ) {
                data class Title(
                    val annotations: Annotations,
                    val href: Any,
                    val plain_text: String,
                    val text: Text,
                    val type: String
                ) {
                    data class Annotations(
                        val bold: Boolean,
                        val code: Boolean,
                        val color: String,
                        val italic: Boolean,
                        val strikethrough: Boolean,
                        val underline: Boolean
                    )

                    data class Text(
                        val content: String,
                        val link: Any
                    )
                }
            }

            data class Url(
                val id: String,
                val type: String,
                val url: String
            )
        }
    }
}