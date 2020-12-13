package systemapp.tbblessing.repository;

import systemapp.tbblessing.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface TransaksiDb extends JpaRepository<TransaksiModel, Long>{
    Optional<TransaksiModel> findByIdTransaksi(Long id);
    List<TransaksiModel> findByNamaPembeli(String namaPembeli);
}