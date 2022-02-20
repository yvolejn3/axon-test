package axon.example.lesson.query

import axon.example.lesson.command.TaskStatus
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class TaskOverviewView(
    @Id
    @Enumerated(EnumType.STRING)
    val status: TaskStatus,
    var count: Long = 0
)

interface TaskOverviewViewRepository: JpaRepository<TaskOverviewView, TaskStatus>
