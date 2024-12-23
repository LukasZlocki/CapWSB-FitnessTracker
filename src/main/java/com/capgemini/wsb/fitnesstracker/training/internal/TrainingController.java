package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.exception.api.NotFoundException;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingInputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
class TrainingController {

    private final TrainingServiceImpl trainingService;

    @GetMapping
    public ResponseEntity<Object> getTrainings() {
        var trainings = trainingService.getAllTrainings();
        if (trainings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No training found");
        }
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getTrainingByUserId(@PathVariable("userId") Long userId) {
        var trainings = trainingService.getTrainingsByUserId(userId);
        if (trainings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No training found assigned to this user id");
        }
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/finished/{finished}")
    public ResponseEntity<Object> getTrainingsFinishedAfter(@PathVariable("finished") LocalDate date) {
        var trainings = trainingService.getTrainingsFinishedAfter(date);
        if (trainings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No training found");
        }
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/activityType")
    public ResponseEntity<Object> getUserByEmail(@RequestParam("activityType") String activityType) {
        var trainings = trainingService.getTrainingsOfActivityType(activityType);
        if(trainings.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No trainings with provided activity type found");
        }
        return ResponseEntity.ok(trainings);
    }

    @PostMapping
    public ResponseEntity<Object> createTraining(@RequestBody TrainingInputDto trainingInputDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(trainingService.createTraining(trainingInputDto));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTraining(@PathVariable("id") Long id, @RequestBody TrainingInputDto trainingInputDto) {

        try {
            return ResponseEntity.ok(trainingService.updateTraining(id, trainingInputDto));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
