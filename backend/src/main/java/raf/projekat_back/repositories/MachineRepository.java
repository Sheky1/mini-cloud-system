package raf.projekat_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.projekat_back.model.Machine;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {

    public List<Machine> findAllByCreatedBy(Integer userID);
    public Optional<Machine> findMachineByCreatedByAndId(Integer userID, Integer id);

}
