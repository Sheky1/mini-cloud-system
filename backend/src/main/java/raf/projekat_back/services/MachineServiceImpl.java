package raf.projekat_back.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import raf.projekat_back.dto.*;
import raf.projekat_back.exceptions.ActionNotAllowedException;
import raf.projekat_back.exceptions.CustomException;
import raf.projekat_back.exceptions.ForbiddenException;
import raf.projekat_back.exceptions.NotFoundException;
import raf.projekat_back.model.Machine;
import raf.projekat_back.model.Permission;
import raf.projekat_back.model.Status;
import raf.projekat_back.model.User;
import raf.projekat_back.repositories.MachineRepository;
import raf.projekat_back.repositories.UserRepository;
import raf.projekat_back.utility.MachineMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {

    private MachineRepository machineRepository;
    private MachineMapper machineMapper;
    private UserService userService;
    private UserRepository userRepository;

    public MachineServiceImpl(MachineRepository machineRepository, MachineMapper machineMapper, UserService userService, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public List<MachineDto> getAllMachines(Integer userID) {
        checkEverything("can_search_machines", "search machines");
//        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
//        int flag = 0;
//        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_search_machines")) flag = 1;
//        if(flag == 0) throw new ForbiddenException("You don't have the permission to search machines.");
        List<MachineDto> allMachinesDto = new ArrayList<>();
        for(Machine machine: machineRepository.findAllByCreatedBy(userID)) {
            allMachinesDto.add(machineMapper.machineToMachineDto(machine));
        }
        return allMachinesDto;
    }

    @Override
    public MachineDto getMachine(Integer userID, Integer id) {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_search_machines")) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to search machines.");
        Machine machine = machineRepository.findMachineByCreatedByAndId(userID, id).orElseThrow(() -> new NotFoundException(String.format("No machine found.")));
        return machineMapper.machineToMachineDto(machine);
    }

    @Override
    public MachineDto createMachine(CreateMachineDto createMachineDto) {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_create_machines")) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to create machines.");
        Machine newMachine = machineMapper.createMachineToMachine(createMachineDto);
        machineRepository.save(newMachine);
        return machineMapper.machineToMachineDto(newMachine);
    }

    public MachineDto destroyMachine(Integer userID, Integer id) {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_destroy_users")) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to destroy users.");
        Machine machine = machineRepository.findMachineByCreatedByAndId(userID, id).orElseThrow(() -> new NotFoundException(String.format("No machine found.")));
        MachineDto machineDto = machineMapper.machineToMachineDto(machine);
        machineRepository.delete(machine);
        return machineDto;
    }

    @Override
    public MachineDto updateStatus(UpdateStatusDto updateStatusDto) {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_" + updateStatusDto.getAction() + "_machines")) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to " + updateStatusDto.getAction() + " machines.");
        Machine machine = machineRepository.findMachineByCreatedByAndId(updateStatusDto.getUserID(), updateStatusDto.getId()).orElseThrow(() -> new NotFoundException(String.format("No machine found.")));
        if(updateStatusDto.getAction().equals("start")) {
            if(machine.getStatus() != Status.STOPPED) throw new ActionNotAllowedException("You cannot start a machine that is not stopped.");
            machine.setStatus(Status.RUNNING);
        }else if(updateStatusDto.getAction().equals("stop")){
            if(machine.getStatus() != Status.RUNNING) throw new ActionNotAllowedException("You cannot stop a machine that is not running.");
            machine.setStatus(Status.STOPPED);
        }else {
            if(updateStatusDto.getRestartPhase().equals("first")) {
                if(machine.getStatus() != Status.RUNNING) throw new ActionNotAllowedException("You cannot restart a machine that is not running.");
                machine.setStatus(Status.STOPPED);
            } else {
                machine.setStatus(Status.RUNNING);
            }
        }
        machineRepository.save(machine);
        return machineMapper.machineToMachineDto(machine);
    }

    private void checkEverything(String action, String errorMessage) throws CustomException {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals(action)) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to " + errorMessage + ".");
    }

}
