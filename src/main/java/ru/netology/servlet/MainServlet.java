package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().equals("/api/posts")) {
            controller.all(resp);
        }
        if (req.getRequestURI().matches("/api/posts/\\d+")) {
            final var id = Long.parseLong(req.getRequestURI().substring(req.getRequestURI().lastIndexOf("/") + 1));
            controller.getById(id, resp);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().equals("/api/posts")) {
            controller.save(req.getReader(), resp);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().matches("/api/posts/\\d+")) {
            final var id = Long.parseLong(req.getRequestURI().substring(req.getRequestURI().lastIndexOf("/") + 1));
            controller.removeById(id, resp);
        }
    }

}
