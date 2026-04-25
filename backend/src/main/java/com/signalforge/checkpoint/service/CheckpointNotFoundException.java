package com.signalforge.checkpoint.service;

import java.util.UUID;

public class CheckpointNotFoundException extends RuntimeException {

    public CheckpointNotFoundException(UUID id) {
        super("Checkpoint not found: " + id);
    }
}
