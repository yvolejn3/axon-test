package axon.example.lesson.query

import axon.example.lesson.command.TaskStatus
import axon.example.lesson.coreapi.FindTaskQuery
import axon.example.lesson.coreapi.TaskCompletedEvent
import axon.example.lesson.coreapi.TaskCreatedEvent
import axon.example.lesson.coreapi.TaskStartedEvent
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class TaskProjector(
    val taskViewRepository: TaskViewRepository
) {
    @EventHandler
    fun on(event: TaskCreatedEvent) {
        TaskView(
            taskId = event.taskId,
            author = event.author,
            users = event.users,
            status = TaskStatus.NEW
        ).let(taskViewRepository::save)
    }

    @EventHandler
    fun on(event: TaskStartedEvent) {
        taskViewRepository.findByIdOrNull(event.taskId)?.apply {
            executor = event.author
            status = TaskStatus.IN_PROGRESS
        }?.let(taskViewRepository::save)
    }

    @EventHandler
    fun on(event: TaskCompletedEvent) {
        taskViewRepository.findByIdOrNull(event.taskId)?.apply {
            status = TaskStatus.COMPLETED
        }?.let(taskViewRepository::save)
    }

    @QueryHandler
    fun handle(query: FindTaskQuery) = taskViewRepository.findByIdOrNull(query.taskId)
}
