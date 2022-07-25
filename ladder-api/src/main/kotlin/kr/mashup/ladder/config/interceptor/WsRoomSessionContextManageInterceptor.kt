package kr.mashup.ladder.config.interceptor

import kr.mashup.ladder.config.context.WsRoomSessionContext
import kr.mashup.ladder.domain.room.domain.RoomMessageSubscriber
import kr.mashup.ladder.domain.room.domain.RoomTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class WsRoomSessionContextManageInterceptor(
    private val redisMessageListenerContainer: RedisMessageListenerContainer,
    private val roomMessageSubscriber: RoomMessageSubscriber,
) : ChannelInterceptor {
    /**
     * @link https://stackoverflow.com/questions/31634973/stomp-disconnects-its-processing-twice-in-channel-interceptor-and-simplebrokerme
     */
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)
        when {
            accessor.command == StompCommand.SUBSCRIBE && accessor.isDestinationRoom() -> {
                val roomId = accessor.getRoomId()
                if (WsRoomSessionContext.isEmpty(roomId)) {
                    redisMessageListenerContainer.addMessageListener(roomMessageSubscriber, RoomTopic(roomId))
                }
                WsRoomSessionContext.add(roomId, accessor.sessionId)
            }
            accessor.command == StompCommand.UNSUBSCRIBE && accessor.isDestinationRoom() -> {
                val roomId = accessor.getRoomId()
                WsRoomSessionContext.remove(roomId, accessor.sessionId)
                if (WsRoomSessionContext.isEmpty(roomId)) {
                    redisMessageListenerContainer.removeMessageListener(roomMessageSubscriber, RoomTopic(roomId))
                }
            }
            accessor.command == StompCommand.DISCONNECT -> {
                WsRoomSessionContext.remove(accessor.sessionId)
            }
            else -> {}
        }

        return message
    }
}
