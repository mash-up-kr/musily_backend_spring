package kr.mashup.ladder.domain.room.domain

import kr.mashup.ladder.domain.room.domain.emoji.EmojiType
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class RoomMood(
    @Column(length = 100) // TODO: not null
    var moodDescription: String,

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    var emojiType: EmojiType,
)
