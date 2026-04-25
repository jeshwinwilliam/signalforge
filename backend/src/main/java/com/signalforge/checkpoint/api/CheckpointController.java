package com.signalforge.checkpoint.api;

import com.signalforge.checkpoint.domain.ReleaseCheckpoint;
import com.signalforge.checkpoint.service.CheckpointService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkpoints")
public class CheckpointController {

    private final CheckpointService checkpointService;

    public CheckpointController(CheckpointService checkpointService) {
        this.checkpointService = checkpointService;
    }

    @GetMapping
    public List<ReleaseCheckpoint> findAll() {
        return checkpointService.findAll();
    }

    @GetMapping("/{id}")
    public ReleaseCheckpoint findById(@PathVariable UUID id) {
        return checkpointService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReleaseCheckpoint create(@Valid @RequestBody CreateCheckpointRequest request) {
        return checkpointService.create(request);
    }
}
