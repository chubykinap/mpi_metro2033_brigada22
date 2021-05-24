package b22.metro2033.Service;

import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.SensorMessages;
import b22.metro2033.Entity.Army.SensorStatus;
import b22.metro2033.Repository.Army.MovementSensorRepository;
import b22.metro2033.Repository.Army.SensorMessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class SensorService {

    @Autowired
    private SensorMessagesRepository sensorMessagesRepository;

    @Autowired
    private MovementSensorRepository movementSensorRepository;

    private Random random = new Random();

    @Async
    public void createError(MovementSensor movementSensor) throws Exception {
        LocalDateTime time = LocalDateTime.now();

        SensorMessages sensorMessages = new SensorMessages();
        sensorMessages.setMovementSensor(movementSensor);
        sensorMessages.setMessages_date(time);

        //random.nextInt(max - min) + min;
        int chosenMessage = random.nextInt(2) ;

        if (chosenMessage == 0){
            sensorMessages.setMessages("ЗАМЕЧЕНО ДВИЖЕНИЕ");
            sensorMessages.setError(true);
            movementSensor.setSensorStatus(SensorStatus.ERROR);
            movementSensorRepository.save(movementSensor);
        }

        if(chosenMessage == 1){
            sensorMessages.setMessages("НАРУШЕНИЙ НЕТ");
            sensorMessages.setError(false);
        }

        sensorMessagesRepository.save(sensorMessages);
    }
}
