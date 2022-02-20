package axon.example.lesson.web

import axon.example.lesson.command.TaskStatus
import axon.example.lesson.coreapi.CompleteTaskCommand
import axon.example.lesson.coreapi.CreateTaskCommand
import axon.example.lesson.coreapi.FindTaskQuery
import axon.example.lesson.coreapi.FindTasksOverviewQuery
import axon.example.lesson.coreapi.StartTaskCommand
import axon.example.lesson.query.TaskOverviewView
import axon.example.lesson.query.TaskView
import java.util.*
import java.util.concurrent.CompletableFuture
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes.instanceOf
import org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TaskController(
    val commandGateway: CommandGateway,
    val queryGateway: QueryGateway
) {
    @PostMapping("/tasks")
    fun create(@RequestBody users: Set<String>, @RequestParam author: String) {
        commandGateway.send<Any?>(CreateTaskCommand(author = author, users = users))
    }

    @PostMapping("/tasks/{taskId}/start")
    fun start(@RequestParam author: String, @PathVariable taskId: UUID) {
        commandGateway.send<Any?>(StartTaskCommand(taskId = taskId, author = author))
    }

    @PostMapping("/tasks/{taskId}/complete")
    fun complete(@RequestParam author: String, @PathVariable taskId: UUID) {
        commandGateway.send<Any?>(CompleteTaskCommand(taskId = taskId, author = author))
    }

    @GetMapping("/tasks/{taskId}")
    fun get(@PathVariable taskId: UUID): CompletableFuture<TaskView> =
        queryGateway.query(FindTaskQuery(taskId), instanceOf(TaskView::class.java))

    @GetMapping("/tasks/overview")
    fun overview(): CompletableFuture<Map<TaskStatus, Long>> =
        queryGateway.query(FindTasksOverviewQuery(), multipleInstancesOf(TaskOverviewView::class.java))
            .thenApply { list -> list.associate { it.status to it.count } }
}
