package com.chat.entity;

import java.util.ArrayList;
import java.util.List;

public class RobotResp {
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;

    public RobotResp() {
        usage = new Usage();
        choices = new ArrayList<Choice>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Message {
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

    public static class Choice {
        private Message message;
        private String finish_reason;
        private int index;

        public Choice() {
            message = new Message();
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public String getFinish_reason() {
            return finish_reason;
        }

        public void setFinish_reason(String finish_reason) {
            this.finish_reason = finish_reason;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
        private int pre_token_count;
        private int pre_total;
        private int adjust_total;
        private int final_total;

        public int getPrompt_tokens() {
            return prompt_tokens;
        }

        public void setPrompt_tokens(int prompt_tokens) {
            this.prompt_tokens = prompt_tokens;
        }

        public int getCompletion_tokens() {
            return completion_tokens;
        }

        public void setCompletion_tokens(int completion_tokens) {
            this.completion_tokens = completion_tokens;
        }

        public int getTotal_tokens() {
            return total_tokens;
        }

        public void setTotal_tokens(int total_tokens) {
            this.total_tokens = total_tokens;
        }

        public int getPre_token_count() {
            return pre_token_count;
        }

        public void setPre_token_count(int pre_token_count) {
            this.pre_token_count = pre_token_count;
        }

        public int getPre_total() {
            return pre_total;
        }

        public void setPre_total(int pre_total) {
            this.pre_total = pre_total;
        }

        public int getAdjust_total() {
            return adjust_total;
        }

        public void setAdjust_total(int adjust_total) {
            this.adjust_total = adjust_total;
        }

        public int getFinal_total() {
            return final_total;
        }

        public void setFinal_total(int final_total) {
            this.final_total = final_total;
        }
    }

}
