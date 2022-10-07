package raf.projekat_back.utility;

import org.springframework.stereotype.Component;
import raf.projekat_back.dto.*;
import raf.projekat_back.model.Machine;
import raf.projekat_back.model.Status;

@Component
public class MachineMapper {

    public MachineMapper() {
    }

    public Machine createMachineToMachine(CreateMachineDto createMachineDto) {
        Machine newMachine = new Machine();
        newMachine.setName(createMachineDto.getName());
        newMachine.setCreatedBy(createMachineDto.getCreatedBy());
        newMachine.setStatus(Status.STOPPED);
        newMachine.setActive(true);
        return newMachine;
    }

    public MachineDto machineToMachineDto(Machine machine) {
        MachineDto newMachineDto = new MachineDto();
        newMachineDto.setId(machine.getId());
        newMachineDto.setCreatedBy(machine.getCreatedBy());
        newMachineDto.setName(machine.getName());
        newMachineDto.setStatus(machine.getStatus());
        return newMachineDto;
    }

}
