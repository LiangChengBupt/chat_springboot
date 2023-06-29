package com.chat.entity;

import java.util.ArrayList;
import java.util.List;

public class RobotReq {
    private String model;
    private RobotContent[] messages;
    private boolean safe_mode;

    public RobotReq() {
        messages = new RobotContent[1];
        messages[0] = new RobotContent();
    }

    public String getModel() {
        return model;
    }

    public RobotContent[] getMessages() {
        return messages;
    }

    public void setMessages(RobotContent[] messages) {
        this.messages = messages;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public void setRole(String role) {
        this.messages[0].role = role;
    }

    public void setContent(String content) {
        this.messages[0].content = content;
    }

    public boolean isSafe_mode() {
        return safe_mode;
    }

    public void setSafe_mode(boolean safe_mode) {
        this.safe_mode = safe_mode;
    }

    public static class RobotContent {
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}