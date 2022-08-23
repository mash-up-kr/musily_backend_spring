package kr.mashup.ladder.common.dto.response

enum class WsResponseType {
    ERROR,
    CHAT,
    EMOJI,
    PLAYLIST_ITEM_REQUEST,
    PLAYLIST_ITEM_REQUEST_DECLINE,
    PLAYLIST_ITEM_ADD,
    PLAYLIST_ITEM_REMOVE,
    PLAYLIST_ITEM_CHANGE_ORDER,
    PLAY_INFORMATION_UPDATE,
}
