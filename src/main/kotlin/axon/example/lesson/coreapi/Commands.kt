package axon.example.lesson.coreapi

import java.util.*
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateTaskCommand(
    @TargetAggregateIdentifier
    val taskId: UUID = UUID.randomUUID(),
    val author: String,
    val users: Set<String>
)

data class StartTaskCommand(
    @TargetAggregateIdentifier
    val taskId: UUID,
    val author: String
)

data class CompleteTaskCommand(
    @TargetAggregateIdentifier
    val taskId: UUID,
    val author: String
)
