package axon.example.lesson.coreapi

import java.util.*

data class TaskCreatedEvent(
    val taskId: UUID,
    val author: String,
    val users: Set<String>
)

data class TaskStartedEvent(
    val taskId: UUID,
    val author: String
)

data class TaskCompletedEvent(
    val taskId: UUID,
    val author: String
)
