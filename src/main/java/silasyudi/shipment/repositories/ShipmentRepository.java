package silasyudi.shipment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import silasyudi.shipment.models.Shipment;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByOriginAndDestination(String origin, String destination);
}
