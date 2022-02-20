package axon.example.lesson.query

import axon.example.lesson.command.TaskStatus
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.Id

@Entity
data class TaskView(
    @Id
    val taskId: UUID,
    val author: String,
    @Enumerated(EnumType.STRING)
    var status: TaskStatus,
    var executor: String? = null,
    @ElementCollection(fetch = FetchType.EAGER)
    val users: Set<String>,
    @ElementCollection(fetch = FetchType.EAGER)
    val comments: MutableList<Pair<String, String>> = mutableListOf()
)

interface TaskViewRepository : JpaRepository<TaskView, UUID>
