package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        final var data = service.all();
        setTypeAndPrint(response, data, new Gson());
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        final var data = service.getById(id);
        if (data != null) {
            setTypeAndPrint(response, data, new Gson());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            setTypeAndPrint(response, "Post not found", new Gson());
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        final var gson = new Gson();
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        setTypeAndPrint(response, data, gson);
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        final var data = service.getById(id);
        if (data != null) {
            service.removeById(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            setTypeAndPrint(response, "Post not found", new Gson());
        }
    }

    public void setTypeAndPrint(HttpServletResponse response, Object data, Gson gson) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.getWriter().print(gson.toJson(data));
    }
}
