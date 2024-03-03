package com.ecoufpel.ecoufpelapp.domains.ufpel_data;

import org.springframework.web.socket.TextMessage;

public record CourseChangeDTO(String currentActivity, String classroomId, String courseId, String userCpf) {
    public TextMessage toTextMessage() {
        return new TextMessage(String.format("""
                {
                "type": "course_change",
                "current_activity": "%s",
                "room_id": "%s",
                "course_id": "%s",
                "user_cpf": "%s"
                }
                """, currentActivity, classroomId, courseId, userCpf));
    }
}
