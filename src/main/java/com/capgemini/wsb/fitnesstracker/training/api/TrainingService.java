package com.capgemini.wsb.fitnesstracker.training.api;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface (API) for modifying operations on {@link Training} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface TrainingService {

    List<TrainingDto> getTrainingsByUserId(Long userId);

    List<TrainingDto> findTrainingsFinishedAfter(LocalDate date);
}
