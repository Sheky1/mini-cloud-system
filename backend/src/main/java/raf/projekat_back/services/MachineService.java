package raf.projekat_back.services;

import org.springframework.stereotype.Service;
import raf.projekat_back.dto.*;

import java.util.List;

@Service
public interface MachineService {

    List<MachineDto> getAllMachines(Integer userID);
    MachineDto getMachine(Integer userID, Integer id);

    MachineDto createMachine(CreateMachineDto createMachineDto);
    MachineDto destroyMachine(Integer userID, Integer id);
    MachineDto updateStatus(UpdateStatusDto updateStatusDto);

}
