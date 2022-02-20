package axon.example.lesson.command

import axon.example.lesson.coreapi.CompleteTaskCommand
import axon.example.lesson.coreapi.CreateTaskCommand
import axon.example.lesson.coreapi.StartTaskCommand
import axon.example.lesson.coreapi.TaskCompletedEvent
import axon.example.lesson.coreapi.TaskCreatedEvent
import axon.example.lesson.coreapi.TaskStartedEvent
import java.util.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class Task {
    @AggregateIdentifier
    lateinit var taskId: UUID
    lateinit var author: String
    lateinit var status: TaskStatus
    val users: MutableList<String> = mutableListOf()
    var executor: String? = null

    constructor()

    @CommandHandler
    constructor(command: CreateTaskCommand) {
        with(command) { TaskCreatedEvent(taskId, author, users) }.run(AggregateLifecycle::apply)
    }

    @CommandHandler
    fun handle(command: StartTaskCommand) {
        with(command) { TaskStartedEvent(taskId, author) }.let(AggregateLifecycle::apply)
    }

    @CommandHandler
    fun handle(command: CompleteTaskCommand) {
        with(command) { TaskCompletedEvent(taskId, author) }.let(AggregateLifecycle::apply)
    }

    @EventSourcingHandler
    fun on(event: TaskCreatedEvent) {
        taskId = event.taskId
        author = event.author
        status = TaskStatus.NEW
        users.addAll(event.users)
    }

    @EventSourcingHandler
    fun on(event: TaskStartedEvent) {
        if (event.author !in users)
            throw IllegalArgumentException("Взять в работу задачу может только участник")
        executor = event.author
        status = TaskStatus.IN_PROGRESS
    }

    @EventSourcingHandler
    fun on(event: TaskCompletedEvent) {
        if (executor != event.author)
            throw IllegalArgumentException("Только исполнитель может выполнять задачу")
        status = TaskStatus.COMPLETED
    }
}

enum class TaskStatus {
    NEW,
    IN_PROGRESS,
    COMPLETED
}