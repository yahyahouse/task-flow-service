package com.yahyahouse.taskflow.repository;

import com.yahyahouse.taskflow.model.entity.Task;
import com.yahyahouse.taskflow.model.enums.TaskPriority;
import com.yahyahouse.taskflow.model.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT t FROM Task t
            WHERE (:status IS NULL OR t.status = :status)
              AND (:priority IS NULL OR t.priority = :priority)
              AND (:keyword IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:dueDateFrom IS NULL OR t.dueDate >= :dueDateFrom)
              AND (:dueDateTo IS NULL OR t.dueDate <= :dueDateTo)
            ORDER BY t.createdAt DESC
            """)
    List<Task> findByFilters(
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            @Param("keyword") String keyword,
            @Param("dueDateFrom") LocalDate dueDateFrom,
            @Param("dueDateTo") LocalDate dueDateTo
    );

    @Query(value = """
            SELECT t.status AS status, COUNT(*) AS total
            FROM tasks t
            GROUP BY t.status
            """, nativeQuery = true)
    List<StatusCountProjection> countByStatusNative();

    @Query(value = """
            SELECT *
            FROM tasks t
            WHERE t.due_date < :today
              AND t.status <> 'DONE'
            ORDER BY t.due_date ASC
            """, nativeQuery = true)
    List<Task> findOverdueTasks(@Param("today") LocalDate today);
}

