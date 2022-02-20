package axon.example.lesson.query

import axon.example.lesson.command.TaskStatus.COMPLETED
import axon.example.lesson.command.TaskStatus.IN_PROGRESS
import axon.example.lesson.command.TaskStatus.NEW
import axon.example.lesson.coreapi.FindTasksOverviewQuery
import axon.example.lesson.coreapi.TaskCompletedEvent
import axon.example.lesson.coreapi.TaskCreatedEvent
import axon.example.lesson.coreapi.TaskStartedEvent
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class TaskOverviewProjector(
    val taskOverviewViewRepository: TaskOverviewViewRepository
) {
    @EventHandler
    fun on(event: TaskCreatedEvent) {
        val taskOverviewView = taskOverviewViewRepository.findByIdOrNull(NEW)
            ?: TaskOverviewView(NEW)
        taskOverviewView.count += 1
        taskOverviewViewRepository.save(taskOverviewView)
    }

    @EventHandler
    fun on(event: TaskStartedEvent) {
        val newTaskOverviewView = taskOverviewViewRepository.findByIdOrNull(NEW)
            ?: TaskOverviewView(NEW)
        newTaskOverviewView.count -= 1
        val startTaskOverviewView = taskOverviewViewRepository.findByIdOrNull(IN_PROGRESS)
            ?: TaskOverviewView(IN_PROGRESS)
        startTaskOverviewView.count += 1
        taskOverviewViewRepository.saveAll(listOf(newTaskOverviewView, startTaskOverviewView))
    }

    @EventHandler
    fun on(event: TaskCompletedEvent) {
        val newTaskOverviewView = taskOverviewViewRepository.findByIdOrNull(IN_PROGRESS)
            ?: TaskOverviewView(IN_PROGRESS)
        newTaskOverviewView.count -= 1
        val startTaskOverviewView = taskOverviewViewRepository.findByIdOrNull(COMPLETED)
            ?: TaskOverviewView(COMPLETED)
        startTaskOverviewView.count += 1
        taskOverviewViewRepository.saveAll(listOf(newTaskOverviewView, startTaskOverviewView))
    }

    @QueryHandler
    fun handle(query: FindTasksOverviewQuery): List<TaskOverviewView> = taskOverviewViewRepository.findAll()
}
