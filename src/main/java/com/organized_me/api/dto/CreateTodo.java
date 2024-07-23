package com.organized_me.api.dto;

import com.organized_me.api.validation.ValidTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class CreateTodo {
    @NotNull(message = "title cannot be empty")
    @Size(min = 1, max = 100)
    private String title;

    @NotNull(message = "description cannot be empty")
    @Size(min = 1, max = 500)
    private String description;

    // minimum now, maximum 1 year later
    @NotNull(message = "time cannot be empty")
    @ValidTime
    private Date time;

    public @NotNull @Size(min = 1, max = 100) String getTitle() {
        return title;
    }

    public void setTitle(@NotNull @Size(min = 1, max = 100) String title) {
        this.title = title;
    }

    public @NotNull @Size(min = 1, max = 500) String getDescription() {
        return description;
    }

    public void setDescription(@NotNull @Size(min = 1, max = 500) String description) {
        this.description = description;
    }

    public @NotNull Date getTime() {
        return time;
    }

    public void setTime(@NotNull Date time) {
        this.time = time;
    }
}
